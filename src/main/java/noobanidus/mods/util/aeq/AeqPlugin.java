/*
package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.plugin.AequivaleoPlugin;
import com.ldtteam.aequivaleo.api.plugin.IAequivaleoPlugin;
import com.ldtteam.aequivaleo.vanilla.api.tags.ITagEquivalencyRegistry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.UtilTags;
import noobanidus.mods.util.init.ModAeq;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static noobanidus.mods.util.init.ModAeq.*;

@AequivaleoPlugin
public class AeqPlugin implements IAequivaleoPlugin {
  @AequivaleoPlugin.Instance
  public static final AeqPlugin INSTANCE = new AeqPlugin();

  public final ConversionCache CLIENT = new ConversionCache(World.OVERWORLD);
  public final ConversionCache SERVER = new ConversionCache(World.OVERWORLD);

  public static ConversionCache get(@Nullable World world) {
    return world != null && world.isRemote ? INSTANCE.CLIENT : INSTANCE.SERVER;
  }

  @Override
  public String getId() {
    return Util.MODID;
  }

    @Override
    public void onCommonSetup()
    {
        ModAeq.MAP.keySet().forEach(ITagEquivalencyRegistry.getInstance()::addTag);
    }

    @Override
      public void onReloadStartedFor(ServerWorld world) {
        SERVER.clear();
      }

  @Override
  public void onDataSynced(RegistryKey<World> worldRegistryKey) {
    CLIENT.clear();
  }
}
*/
