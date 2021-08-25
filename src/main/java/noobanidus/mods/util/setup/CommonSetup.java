package noobanidus.mods.util.setup;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.util.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class CommonSetup {
  public static ConfiguredFeature<?, ?> CHERRYWOOD_TREE_CONFIGURED = null;

  public static void init(FMLCommonSetupEvent event) {
    Block cherrywood_log = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forbidden_arcanus", "cherrywood_log"));
    Block cherrywood_leaves = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forbidden_arcanus", "cherrywood_leaves"));
    if (cherrywood_log != null && cherrywood_leaves != null) {
      CHERRYWOOD_TREE_CONFIGURED = Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(cherrywood_log.getDefaultState()), new SimpleBlockStateProvider(cherrywood_leaves.getDefaultState()), new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new ForkyTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2))).setIgnoreVines().build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(ConfigManager.cherryCount(), ConfigManager.cherryExtra(), ConfigManager.cherryExtraCount())));
    }
    event.enqueueWork(() -> {
      if (ConfigManager.shouldCherry()) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation("util", "cherrywood_tree_configured"), CHERRYWOOD_TREE_CONFIGURED);
      }
    });
  }

  public static void onLoad (FMLLoadCompleteEvent event) {
    if (ConfigManager.shouldModifyStables()) {
      event.enqueueWork(() -> {
        if (EffectiveSide.get() == LogicalSide.SERVER) {
          ResourceLocation rl = new ResourceLocation("stables", "large_barn");
          Map<Structure<?>, StructureSeparationSettings> currentSettings = new HashMap<>(DimensionStructuresSettings.field_236191_b_);
          Structure<?> barn = Structure.NAME_STRUCTURE_BIMAP.get(rl.toString());
          if (barn != null) {
            StructureSeparationSettings cur = currentSettings.get(barn);
            int seed = cur == null ? 671023157 : cur.func_236673_c_();
            currentSettings.put(barn, new StructureSeparationSettings(ConfigManager.getStableSpacing(), ConfigManager.getStableSeparation(), seed));
            DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(currentSettings).build();
          }
        }
      });
    }
  }
}
