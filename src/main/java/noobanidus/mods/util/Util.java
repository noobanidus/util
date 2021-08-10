package noobanidus.mods.util;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.mods.util.aeq.AeqPlugin;
import noobanidus.mods.util.commands.*;
import noobanidus.mods.util.config.ConfigManager;
import noobanidus.mods.util.init.ModAeq;
import noobanidus.mods.util.setup.ClientInit;
import noobanidus.mods.util.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static net.minecraftforge.common.BiomeDictionary.Type;

@Mod("util")
public class Util {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "util";
  public static CustomRegistrate REGISTRATE;

  public Util() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    modBus.addListener(CommonSetup::init);
    modBus.addListener(ConfigManager::onLoading);
    modBus.addListener(ConfigManager::onReload);

    REGISTRATE = CustomRegistrate.create(MODID);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::biomeLoad);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onRightClickBlock);
    ModAeq.load();
  }

  private static final Set<ResourceLocation> ENTITIES_TO_REMOVE = Sets.newHashSet(new ResourceLocation("upgrade_aquatic", "pike"));

  private EntityType<?> getUnicorn(String name) {
    return ForgeRegistries.ENTITIES.getValue(new ResourceLocation("ultimate_unicorn_mod", name));
  }

  private static boolean compare(Supplier<ConfiguredFeature<?, ?>> a, Supplier<ConfiguredFeature<?, ?>> b) {
    Optional<JsonElement> jA = ConfiguredFeature.field_242763_a.encode(a.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();
    Optional<JsonElement> jB = ConfiguredFeature.field_242763_a.encode(b.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

    if (!jA.isPresent() || !jB.isPresent()) {
      return false;
    }

    return jA.get().equals(jB.get());
  }

  private static Supplier<ConfiguredFeature<?, ?>> CHERRY_TREE = null;

  public void biomeLoad(BiomeLoadingEvent event) {
    if (ConfigManager.shouldCherry() && CommonSetup.CHERRYWOOD_TREE_CONFIGURED != null && ModList.get().isLoaded("forbidden_arcanus")) {
      if (CHERRY_TREE == null) {
        for (Supplier<ConfiguredFeature<?, ?>> feat : event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION)) {
          ConfiguredFeature<?, ?> cf = feat.get();
          IFeatureConfig config = cf.config;
          while (config instanceof DecoratedFeatureConfig) {
            config = ((DecoratedFeatureConfig) config).feature.get().config;
          }

          if (config instanceof BaseTreeFeatureConfig) {
            BaseTreeFeatureConfig treeConfig = (BaseTreeFeatureConfig) config;
            String log = Objects.requireNonNull(treeConfig.trunkProvider.getBlockState(ThreadLocalRandom.current(), BlockPos.ZERO).getBlock().getRegistryName()).toString();
            String leaves = Objects.requireNonNull(treeConfig.leavesProvider.getBlockState(ThreadLocalRandom.current(), BlockPos.ZERO).getBlock().getRegistryName()).toString();
            if (log.equals("forbidden_arcanus:cherrywood_log") && leaves.equals("forbidden_arcanus:cherrywood_leaves")) {
              CHERRY_TREE = feat;
            }
          }
        }
      }
    }
    if (CHERRY_TREE != null) {
      event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).removeIf(o -> compare(CHERRY_TREE, o));
      if (event.getCategory() == Biome.Category.PLAINS) {
        event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> CommonSetup.CHERRYWOOD_TREE_CONFIGURED);
      }
    }
    for (EntityClassification classification : EntityClassification.values()) {
      event.getSpawns().getSpawner(classification).removeIf(o -> ENTITIES_TO_REMOVE.contains(o.type.getRegistryName()));
      if (event.getName() != null && ConfigManager.shouldUnicorn()) {
        event.getSpawns().getSpawner(classification).removeIf(o -> o.type.getRegistryName().getNamespace().equals("ultimate_unicorn_mod"));
        if (classification == EntityClassification.CREATURE) {
          RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
          Set<Type> types = BiomeDictionary.getTypes(key);
          if (types.contains(Type.SWAMP) || types.contains(Type.BEACH) || types.contains(Type.RIVER)) {
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("hippocamp"), ConfigManager.unicornChance(), 1, 2));
          } else if (types.contains(Type.SANDY) || types.contains(Type.WASTELAND) || types.contains(Type.MESA) || types.contains(Type.SPOOKY) || types.contains(Type.DEAD)) {
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("destrier"), ConfigManager.unicornChance(), 1, 1));
          } else if (types.contains(Type.MAGICAL) || types.contains(Type.MUSHROOM) || types.contains(Type.LUSH) || types.contains(Type.FOREST)) {
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("unicorn"), ConfigManager.unicornChance(), 1, 2));
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("reindeer"), 6, 1, 3));
          } else if (types.contains(Type.PLAINS) || types.contains(Type.SAVANNA)) {
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("kirin"), ConfigManager.unicornChance(), 1, 1));
          } else if (types.contains(Type.MOUNTAIN) || types.contains(Type.HILLS) || types.contains(Type.PLATEAU)) {
            event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(getUnicorn("pegasus"), ConfigManager.unicornChance(), 1, 2));
          }
        }
      }
    }
  }

  public void serverStarting(RegisterCommandsEvent event) {
    CommandItems.register(event.getDispatcher());
    CommandEntities.register(event.getDispatcher());
    CommandConfig.register(event.getDispatcher());
    CommandItemKill.register(event.getDispatcher());
    CommandToolLevel.register(event.getDispatcher());
  }

  public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    if (ConfigManager.blockSpawners()) {
      if (event.getItemStack().getItem() instanceof SpawnEggItem) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof SpawnerBlock) {
          event.setUseItem(Event.Result.DENY);
        }
      }
    }
  }
}
