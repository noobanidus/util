package noobanidus.libs.util;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.commands.CommandItems;
import noobanidus.libs.util.events.PotionHandler;
import noobanidus.libs.util.init.ModRecipes;
import noobanidus.libs.util.setup.ClientSetup;
import noobanidus.libs.util.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod("util")
public class Util {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "util";

  public Util() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::init);

    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
      modBus.addListener(ClientSetup::init);
    });

    modBus.addListener(this::loadComplete);
    ModRecipes.recipeRegistry.register(modBus);
    MinecraftForge.EVENT_BUS.addListener(PotionHandler::onPotionAdded);
    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
  }

  private static final Set<ResourceLocation> ENTITIES_TO_REMOVE = Sets.newHashSet(new ResourceLocation("quark", "stoneling"), new ResourceLocation("quark", "frog"), new ResourceLocation("quark", "foxhound"));

  public void loadComplete (FMLLoadCompleteEvent event) {
    for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
      for (EntityClassification classification : EntityClassification.values()) {
        List<Biome.SpawnListEntry> spawns = biome.getSpawns(classification);
        spawns.removeIf(o -> ENTITIES_TO_REMOVE.contains(o.entityType.getRegistryName()));
      }
    }
  }

  public void joinWorld (EntityJoinWorldEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType().equals(EntityType.ZOMBIE)) {
      ZombieEntity zombie = (ZombieEntity) entity;
      zombie.goalSelector.goals.removeIf(o -> o.getGoal().getClass().equals(ZombieEntity.AttackTurtleEggGoal.class));
    }
  }

  private CommandItems itemsCommand;

  public void serverStarting (FMLServerStartingEvent event) {
    CommandItems itemsCommand = new CommandItems(event.getCommandDispatcher()).register();
  }
}
