package noobanidus.libs.util.setup;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.config.ConfigManager;

public class CommonSetup {
  public static ConfiguredFeature<?, ?> CHERRYWOOD_TREE_CONFIGURED = null;

  public static void init(FMLCommonSetupEvent event) {
    if (ConfigManager.shouldCherry()) {
      Block cherrywood_log = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forbidden_arcanus", "cherrywood_log"));
      Block cherrywood_leaves = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forbidden_arcanus", "cherrywood_leaves"));
      if (cherrywood_log != null && cherrywood_leaves != null) {
        CHERRYWOOD_TREE_CONFIGURED = Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(cherrywood_log.getDefaultState()), new SimpleBlockStateProvider(cherrywood_leaves.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2))).setIgnoreVines().build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(ConfigManager.cherryCount(), ConfigManager.cherryExtra(), ConfigManager.cherryExtraCount())));

        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation("util", "cherrywood_tree_configured"), CHERRYWOOD_TREE_CONFIGURED);
      }
    }
  }
}
