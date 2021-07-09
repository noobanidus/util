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

public class CommandConfig {
  public static void register(CommandDispatcher<CommandSource> builder) {
   builder.register(builder(Commands.literal("genconfig").requires((o) -> o.hasPermissionLevel(2))));
  }

  private static String format(double value) {
    return String.format("%.2f", value);
  }

  public static LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      return 1;
    });
    return builder;
  }
}
