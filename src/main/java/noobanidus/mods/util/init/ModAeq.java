package noobanidus.mods.util.init;

import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.compound.type.group.ICompoundTypeGroup;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import noobanidus.mods.util.UtilTags;
import noobanidus.mods.util.aeq.ValueGroupType;
import noobanidus.mods.util.aeq.ValueType;

import java.util.HashMap;
import java.util.Map;

import static noobanidus.mods.util.Util.REGISTRATE;

public class ModAeq {
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

  public static final Map<ITag.INamedTag<Item>, RegistryEntry<ICompoundType>> MAP = new HashMap<>();

  static {
    MAP.put(UtilTags.VALUE1, VALUE1);
    MAP.put(UtilTags.VALUE2, VALUE2);
    MAP.put(UtilTags.VALUE3, VALUE3);
    MAP.put(UtilTags.VALUE4, VALUE4);
    MAP.put(UtilTags.VALUE5, VALUE5);
    MAP.put(UtilTags.VALUE6, VALUE6);
    MAP.put(UtilTags.VALUE7, VALUE7);
    MAP.put(UtilTags.VALUE8, VALUE8);
    MAP.put(UtilTags.VALUE9, VALUE9);
    MAP.put(UtilTags.VALUE10, VALUE10);
    MAP.put(UtilTags.VALUE15, VALUE15);
    MAP.put(UtilTags.VALUE20, VALUE20);
    MAP.put(UtilTags.VALUE25, VALUE25);
    MAP.put(UtilTags.VALUE50, VALUE50);
    MAP.put(UtilTags.VALUE75, VALUE75);
    MAP.put(UtilTags.VALUE100, VALUE100);
  }

  public static void load() {

  }
}
