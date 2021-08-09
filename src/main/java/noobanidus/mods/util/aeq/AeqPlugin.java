package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.compound.type.group.ICompoundTypeGroup;
import com.ldtteam.aequivaleo.api.plugin.AequivaleoPlugin;
import com.ldtteam.aequivaleo.api.plugin.IAequivaleoPlugin;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.UtilTags;

import javax.annotation.Nullable;

import static noobanidus.mods.util.Util.REGISTRATE;

@AequivaleoPlugin
public class AeqPlugin implements IAequivaleoPlugin {
  @AequivaleoPlugin.Instance
  public static final AeqPlugin INSTANCE = new AeqPlugin();

  public static final RegistryEntry<ICompoundTypeGroup> VALUE = REGISTRATE.simple("value", ICompoundTypeGroup.class, ValueGroupType::new);
  public static final RegistryEntry<ICompoundType> VALUE1 = REGISTRATE.simple("value1", ICompoundType.class, () -> new ValueType(UtilTags.VALUE1, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE2 = REGISTRATE.simple("value2", ICompoundType.class, () -> new ValueType(UtilTags.VALUE2, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE3 = REGISTRATE.simple("value3", ICompoundType.class, () -> new ValueType(UtilTags.VALUE3, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE4 = REGISTRATE.simple("value4", ICompoundType.class, () -> new ValueType(UtilTags.VALUE4, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE5 = REGISTRATE.simple("value5", ICompoundType.class, () -> new ValueType(UtilTags.VALUE5, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE6 = REGISTRATE.simple("value6", ICompoundType.class, () -> new ValueType(UtilTags.VALUE6, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE7 = REGISTRATE.simple("value7", ICompoundType.class, () -> new ValueType(UtilTags.VALUE7, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE8 = REGISTRATE.simple("value8", ICompoundType.class, () -> new ValueType(UtilTags.VALUE8, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE9 = REGISTRATE.simple("value9", ICompoundType.class, () -> new ValueType(UtilTags.VALUE9, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE10 = REGISTRATE.simple("value10", ICompoundType.class, () -> new ValueType(UtilTags.VALUE10, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE15 = REGISTRATE.simple("value15", ICompoundType.class, () -> new ValueType(UtilTags.VALUE15, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE20 = REGISTRATE.simple("value20", ICompoundType.class, () -> new ValueType(UtilTags.VALUE20, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE25 = REGISTRATE.simple("value25", ICompoundType.class, () -> new ValueType(UtilTags.VALUE25, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE50 = REGISTRATE.simple("value50", ICompoundType.class, () -> new ValueType(UtilTags.VALUE50, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE75 = REGISTRATE.simple("value75", ICompoundType.class, () -> new ValueType(UtilTags.VALUE75, VALUE));
  public static final RegistryEntry<ICompoundType> VALUE100 = REGISTRATE.simple("value100", ICompoundType.class, () -> new ValueType(UtilTags.VALUE100, VALUE));

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
  public void onReloadStartedFor(ServerWorld world) {
    SERVER.clear();
  }

  @Override
  public void onDataSynced(RegistryKey<World> worldRegistryKey) {
    CLIENT.clear();
  }
}
