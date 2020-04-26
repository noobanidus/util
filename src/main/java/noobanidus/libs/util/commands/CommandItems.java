package noobanidus.libs.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

import java.util.*;
import java.util.stream.Collectors;

public class CommandItems {
  private final CommandDispatcher<CommandSource> dispatcher;

  public CommandItems(CommandDispatcher<CommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public CommandItems register() {
    this.dispatcher.register(builder(Commands.literal("itemlist").requires((o) -> o.hasPermissionLevel(2))));
    return this;
  }

  private String format(double value) {
    return String.format("%.2f", value);
  }

  public LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      List<DimensionType> unloaded = new ArrayList<>();
      Map<DimensionType, List<ItemEntity>> itemStorage = new HashMap<>();
      for (DimensionType dim : DimensionType.getAll()) {
        ServerWorld world = DimensionManager.getWorld(server, dim, false, false);
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
      c.getSource().sendFeedback(new StringTextComponent("Unloaded dimensions that were not considered: " + unloaded.stream().map(DimensionType::toString).collect(Collectors.joining(","))), true);
      for (Map.Entry<DimensionType, List<ItemEntity>> v : itemStorage.entrySet()) {
        if (v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("Dimension " + v.getKey() + " contains no ItemEntities."), true);
        }
      }
      for (Map.Entry<DimensionType, List<ItemEntity>> v : itemStorage.entrySet()) {
        if (!v.getValue().isEmpty()) {
          c.getSource().sendFeedback(new StringTextComponent("====== Dimension " + v.getKey() + " ====="), true);
          int i = 0;
          for (ItemEntity item : v.getValue()) {
            c.getSource().sendFeedback(new StringTextComponent("#" + i + " " + item.getItem().toString() + " at ").appendSibling(TextComponentUtils.wrapInSquareBrackets(new StringTextComponent(format(item.posX) + ", " + format(item.posZ))).setStyle(new Style().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + item.posX + " " + item.posY + " " + item.posZ)).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Teleport to location"))))), true);
            i++;
          }
        }
      }
      return 1;
    });
    return builder;
  }
}
