package noobanidus.libs.util;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.commands.CommandEntities;
import noobanidus.libs.util.commands.CommandItemKill;
import noobanidus.libs.util.commands.CommandItems;
import noobanidus.libs.util.config.ConfigManager;
import noobanidus.libs.util.setup.ClientInit;
import noobanidus.libs.util.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type;

@Mod("util")
public class Util {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "util";

  public Util() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    modBus.addListener(CommonSetup::init);
    modBus.addListener(ConfigManager::onLoading);
    modBus.addListener(ConfigManager::onReload);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    MinecraftForge.EVENT_BUS.addListener(this::biomeLoad);
  }

  private static final Set<ResourceLocation> ENTITIES_TO_REMOVE = Sets.newHashSet(new ResourceLocation("upgrade_aquatic", "pike"));

  private EntityType<?> getUnicorn(String name) {
    return ForgeRegistries.ENTITIES.getValue(new ResourceLocation("ultimate_unicorn_mod", name));
  }

  public void biomeLoad(BiomeLoadingEvent event) {
    for (EntityClassification classification : EntityClassification.values()) {
      event.getSpawns().getSpawner(classification).removeIf(o -> ENTITIES_TO_REMOVE.contains(o.type.getRegistryName()));
      if (event.getName() != null && ConfigManager.shouldUnicorn()) {
        event.getSpawns().getSpawner(classification).removeIf(o -> o.type.getRegistryName().getNamespace().equals("ultimate_unicorn_mod"));
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

  private CommandItems itemsCommand;
  private CommandEntities entitiesCommand;
  private CommandItemKill itemKillCommand;

  public void serverStarting(RegisterCommandsEvent event) {
    itemsCommand = new CommandItems(event.getDispatcher()).register();
    entitiesCommand = new CommandEntities(event.getDispatcher()).register();
    itemKillCommand = new CommandItemKill(event.getDispatcher()).register();
  }
}
