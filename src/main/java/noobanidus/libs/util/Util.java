package noobanidus.libs.util;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityClassification;
import net.minecraft.loot.LootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.commands.CommandEntities;
import noobanidus.libs.util.commands.CommandItemKill;
import noobanidus.libs.util.commands.CommandItems;
import noobanidus.libs.util.config.ConfigManager;
import noobanidus.libs.util.setup.ClientInit;
import noobanidus.libs.util.setup.ClientSetup;
import noobanidus.libs.util.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

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

  public void biomeLoad (BiomeLoadingEvent event) {
    for (EntityClassification classification : EntityClassification.values()) {
      event.getSpawns().getSpawner(classification).removeIf(o -> ENTITIES_TO_REMOVE.contains(o.type.getRegistryName()));
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
