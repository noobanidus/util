package noobanidus.mods.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

public class CommandItems {
  public static void register (CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(builder(Commands.literal("itemlist").requires((o) -> o.hasPermissionLevel(2))));
  }

  private static String format(double value) {
    return String.format("%.2f", value);
  }

  public static LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      List<RegistryKey<World>> unloaded = new ArrayList<>();
      Map<RegistryKey<World>, List<ItemEntity>> itemStorage = new HashMap<>();
      for (RegistryKey<World> dim : server.func_240770_D_()) {
        ServerWorld world = server.getWorld(dim);
        if (world == null) {
          unloaded.add(dim);
        } else {
          List<ItemEntity> itemList = itemStorage.computeIfAbsent(dim, (o) -> new ArrayList<>());
          world.getEntities().forEach(e -> {
            if (e instanceof ItemEntity) {
              itemList.add((ItemEntity) e);
            }
          });
          itemList.sort(Comparator.comparingDouble(o -> o.getDistanceSq(0, 0, 0)));
        }
      }
      c.getSource().sendFeedback(new StringTextComponent("Unloaded dimensions that were not considered: " + unloaded.stream().map(RegistryKey<World>::toString).collect(Collectors.joining(","))), true);
      for (Map.Entry<RegistryKey<World>, List<ItemEntity>> v : itemStorage.entrySet()) {
        if (v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("Dimension " + v.getKey() + " contains no ItemEntities."), true);
        }
      }
      for (Map.Entry<RegistryKey<World>, List<ItemEntity>> v : itemStorage.entrySet()) {
        if (!v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("====== Dimension " + v.getKey() + " ====="), true);
          int i = 0;
          for (ItemEntity item : v.getValue()) {
            c.getSource().sendFeedback(new StringTextComponent("#" + i + " " + item.getItem().toString() + " at ").append(TextComponentUtils.wrapWithSquareBrackets(new StringTextComponent(format(item.getPosX()) + ", " + format(item.getPosZ()))).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + item.getPosX() + " " + item.getPosY() + " " + item.getPosZ())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Teleport to location"))))), true);
            i++;
          }
        }
      }
      return 1;
    });
    return builder;
  }
}
