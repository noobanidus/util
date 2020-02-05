package noobanidus.libs.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import noobanidus.libs.util.init.ModRecipes;
import noobanidus.libs.util.registrate.CustomRegistrate;
import noobanidus.libs.util.setup.ClientSetup;
import noobanidus.libs.util.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("util")
public class Util {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "util";

  public static CustomRegistrate REGISTRATE;

  public Util() {
    /*    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Util.MODID + "-common.toml"));*/
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::init);

    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
      modBus.addListener(ClientSetup::init);
    });

    REGISTRATE = CustomRegistrate.create(MODID);
    ModRecipes.load();
  }
}
