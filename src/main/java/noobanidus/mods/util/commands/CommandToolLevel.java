package noobanidus.mods.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import noobanidus.mods.util.aeq.AeqPlugin;
import noobanidus.mods.util.aeq.ConversionCache;
import noobanidus.mods.util.init.ModAeq;

public class CommandToolLevel {
  public static void register (CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(builder(Commands.literal("tl").requires((o) -> o.hasPermissionLevel(2))));
  }

  public static LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      World world = server.getWorld(World.OVERWORLD);
      ConversionCache cache = AeqPlugin.get(world);
      for (ITag.INamedTag<Item> tag : ModAeq.MAP.keySet()) {
        for (Item i : tag.getAllElements()) {
          double amount = cache.getAmount(new ItemStack(i, 1), true);
          c.getSource().sendFeedback(new TranslationTextComponent("util.blah", new TranslationTextComponent(i.getTranslationKey()), new StringTextComponent("" + amount)), true);
        }
      }
      return 1;
    });
    return builder;
  }
}
