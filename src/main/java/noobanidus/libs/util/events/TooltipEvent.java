package noobanidus.libs.util.events;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.util.Util;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Util.MODID)
public class TooltipEvent {
  @SubscribeEvent(priority = EventPriority.LOW)
  public static void onTooltipEvent(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    if (!stack.isDamageable() || stack.isDamaged()) {
      return;
    }

    if (event.getFlags().isAdvanced()) {
      event.getToolTip().add(new TranslationTextComponent("item.durability", stack.getMaxDamage(), stack.getMaxDamage()));
    }
  }
}
