package noobanidus.libs.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
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
      List<DimensionType> unloaded = new ArrayList<>();
      Map<DimensionType, List<Entity>> entityStorage = new HashMap<>();
      for (DimensionType dim : DimensionType.getAll()) {
        ServerWorld world = DimensionManager.getWorld(server, dim, false, false);
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
      c.getSource().sendFeedback(new StringTextComponent("Unloaded dimensions that were not considered: " + unloaded.stream().map(DimensionType::toString).collect(Collectors.joining(","))), true);
      for (Map.Entry<DimensionType, List<Entity>> v : entityStorage.entrySet()) {
        if (v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("Dimension " + v.getKey() + " contains no ItemEntities."), true);
        }
      }
      for (Map.Entry<DimensionType, List<Entity>> v : entityStorage.entrySet()) {
        if (!v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("====== Dimension " + v.getKey() + " ====="), true);
          int i = 0;
          for (Entity e : v.getValue()) {
            c.getSource().sendFeedback(new StringTextComponent("#" + i + " " + entity.getRegistryName().toString() + " ").appendSibling(e.getDisplayName()).appendSibling(new StringTextComponent(" at ")).appendSibling(TextComponentUtils.wrapInSquareBrackets(new StringTextComponent(format(e.posX) + ", " + format(e.posZ))).setStyle(new Style().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + e.posX + " " + e.posY + " " + e.posZ)).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Teleport to location"))))), true);

            i++;
          }
        }
      }
      return 1;
    })));
    return builder;
  }
}
