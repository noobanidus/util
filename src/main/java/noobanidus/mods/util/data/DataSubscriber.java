package noobanidus.mods.util.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import noobanidus.mods.util.Util;

@Mod.EventBusSubscriber(modid= Util.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataSubscriber {
  @SubscribeEvent
  public static void gatherData (GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    if (event.includeServer()) {
      AeqTagsProvider.AeqBlockTagsProvider blockProvider = new AeqTagsProvider.AeqBlockTagsProvider(generator, event.getExistingFileHelper());
      generator.addProvider(new AeqTagsProvider(generator, blockProvider, event.getExistingFileHelper()));
 /*     generator.addProvider(new AeqData(generator));*/
    }
  }
}
