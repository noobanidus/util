package noobanidus.libs.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class CommandEntities {
  private final CommandDispatcher<CommandSource> dispatcher;

  public CommandEntities(CommandDispatcher<CommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public CommandEntities register() {
    this.dispatcher.register(builder(Commands.literal("entitylist").requires((o) -> o.hasPermissionLevel(2))));
    return this;
  }

  private String format(double value) {
    return String.format("%.2f", value);
  }

  public LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    ForgeRegistries.ENTITIES.getValues().forEach(entity -> builder.then(Commands.literal(Objects.requireNonNull(entity.getRegistryName()).toString()).executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      List<RegistryKey<World>> unloaded = new ArrayList<>();
      Map<RegistryKey<World>, List<Entity>> entityStorage = new HashMap<>();
      for (RegistryKey<World> dim : server.func_240770_D_()) {
        ServerWorld world = server.getWorld(dim);
        if (world == null) {
          unloaded.add(dim);
        } else {
          List<Entity> entityList = entityStorage.computeIfAbsent(dim, (o) -> new ArrayList<>());
          world.getEntities().forEach(e -> {
            if (e.getType().equals(entity)) {
              entityList.add(e);
            }
          });
          entityList.sort(Comparator.comparingDouble(o -> o.getDistanceSq(0, 0, 0)));
        }
      }
      c.getSource().sendFeedback(new StringTextComponent("Unloaded dimensions that were not considered: " + unloaded.stream().map(RegistryKey<World>::toString).collect(Collectors.joining(","))), true);
      for (Map.Entry<RegistryKey<World>, List<Entity>> v : entityStorage.entrySet()) {
        if (v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("Dimension " + v.getKey() + " contains no ItemEntities."), true);
        }
      }
      for (Map.Entry<RegistryKey<World>, List<Entity>> v : entityStorage.entrySet()) {
        if (!v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("====== Dimension " + v.getKey() + " ====="), true);
          int i = 0;
          for (Entity e : v.getValue()) {
            c.getSource().sendFeedback(new StringTextComponent("#" + i + " (ID: " + e.getEntityId() + ", UUID: " + e.getCachedUniqueIdString() + ") " + entity.getRegistryName().toString() + " ").append(e.getDisplayName()).append(new StringTextComponent(" at ")).append(TextComponentUtils.wrapWithSquareBrackets(new StringTextComponent(format(e.getPosX()) + ", " + format(e.getPosZ()))).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + e.getPosZ() + " " + e.getPosY() + " " + e.getPosZ())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Teleport to location"))))), true);

            i++;
          }
        }
      }
      return 1;
    })));
    return builder;
  }
}
