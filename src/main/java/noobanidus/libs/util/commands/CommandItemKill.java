package noobanidus.libs.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.libs.util.config.ConfigManager;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandItemKill {
  private final CommandDispatcher<CommandSource> dispatcher;

  public CommandItemKill(CommandDispatcher<CommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public CommandItemKill register() {
    this.dispatcher.register(builder(Commands.literal("itemkill").requires((o) -> o.hasPermissionLevel(2))));
    return this;
  }

  private String format(double value) {
    return String.format("%.2f", value);
  }

  public LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      Set<Item> blacklist = ConfigManager.getItemBlacklist();
      MinecraftServer server = c.getSource().getServer();
      for (RegistryKey<World> dim : server.func_240770_D_()) {
        ServerWorld world = server.getWorld(dim);
        if (world != null) {
          AtomicInteger count = new AtomicInteger(0);
          world.getEntities().forEach(e -> {
            if (e instanceof ItemEntity) {
              ItemEntity i = (ItemEntity) e;
              ItemStack stack = i.getItem();
              Item item = stack.getItem();
              if (blacklist.contains(item)) {
                c.getSource().sendFeedback(new StringTextComponent("Remove item entity " + ((ItemEntity) e).getItem() + " at " + format(e.getPosX()) + ", " + format(e.getPosY()) + ", " + format(e.getPosZ())), false);
                e.remove();
                count.getAndIncrement();
              }
            }
          });
          if (count.get() == 0) {
            c.getSource().sendFeedback(new StringTextComponent("Removed no items from dimension: " + dim.toString()), false);
          }
        }
      }
      return 1;
    });
    return builder;
  }
}
