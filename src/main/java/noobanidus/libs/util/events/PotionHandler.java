package noobanidus.libs.util.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;

public class PotionHandler {
  public static final ResourceLocation DRUID = new ResourceLocation("firefly", "effect_druids_delight");

  public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      return;
    }

    EffectInstance old = event.getOldPotionEffect();
    if (old != null) {
      if (old.getPotion().getRegistryName().equals(DRUID)) {
        event.setCanceled(true);
        return;
      }
    }

    EffectInstance pot = event.getPotionEffect();
    if (pot.getPotion().getRegistryName().equals(DRUID)) {
      event.setCanceled(true);
      return;
    }
  }
}
