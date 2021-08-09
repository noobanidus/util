package noobanidus.mods.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import noobanidus.mods.util.aeq.AeqPlugin;
import noobanidus.mods.util.aeq.ConversionCache;

public class CommandToolLevel {
  public static void register (CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(builder(Commands.literal("tl").requires((o) -> o.hasPermissionLevel(2))));
  }

  public static LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      World world = server.getWorld(World.OVERWORLD);
      ConversionCache cache = AeqPlugin.get(world);
      c.getSource().sendFeedback(new StringTextComponent("Value of blaze powder: " + cache.getAmount(new ItemStack(Items.BLAZE_POWDER, 4), true)), true);
      return 1;
    });
    return builder;
  }
}
