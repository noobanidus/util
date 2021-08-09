package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.compound.type.group.ICompoundTypeGroup;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ValueType extends ForgeRegistryEntry<ICompoundType> implements ICompoundType {
  public final Tags.IOptionalNamedTag<Item> TAG;
  public final RegistryEntry<ICompoundTypeGroup> groupSupplier;
  public final int value;

  public ValueType(Tags.IOptionalNamedTag<Item> TAG, RegistryEntry<ICompoundTypeGroup> groupSupplier) {
    this.TAG = TAG;
    this.groupSupplier = groupSupplier;
    this.value = Integer.parseInt(getId().split("value")[1]);
  }

  @Override
  public ICompoundTypeGroup getGroup() {
    return groupSupplier.get();
  }

  public Tags.IOptionalNamedTag<Item> getTag() {
    return TAG;
  }

  public String getId() {
    return TAG.getName().getPath();
  }

  public int getValue() {
    return value;
  }
}
