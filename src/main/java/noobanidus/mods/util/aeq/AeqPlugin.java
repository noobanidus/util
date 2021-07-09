package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.compound.CompoundInstance;
import com.ldtteam.aequivaleo.api.compound.container.ICompoundContainer;
import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.compound.type.group.ICompoundTypeGroup;
import com.ldtteam.aequivaleo.api.mediation.IMediationCandidate;
import com.ldtteam.aequivaleo.api.mediation.IMediationEngine;
import com.ldtteam.aequivaleo.api.plugin.AequivaleoPlugin;
import com.ldtteam.aequivaleo.api.plugin.IAequivaleoPlugin;
import com.ldtteam.aequivaleo.api.recipe.equivalency.IEquivalencyRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.UtilTags;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

  public static final ConversionCache CLIENT = new ConversionCache();
  public static final ConversionCache SERVER = new ConversionCache();

  public static ConversionCache get(@Nullable World world) {
    return world != null && world.isRemote ? CLIENT : SERVER;
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

  @Override
  public void onConstruction() {
    ConversionCache.aeqGetter = ValueConversionCache::new;
  }

  public static class ValueGroupType extends ForgeRegistryEntry<ICompoundTypeGroup> implements ICompoundTypeGroup {

    @Override
    public IMediationEngine getMediationEngine() {
      return context -> context
          .getCandidates()
          .stream()
          .min((o1, o2) -> {
            if (o1.isSourceIncomplete() && !o2.isSourceIncomplete())
              return 1;

            if (!o1.isSourceIncomplete() && o2.isSourceIncomplete())
              return -1;

            if (o1.getValues().isEmpty() && !o2.getValues().isEmpty())
              return 1;

            if (!o1.getValues().isEmpty() && o2.getValues().isEmpty())
              return -1;

            return Double.compare(o1.getValues().stream().mapToDouble(CompoundInstance::getAmount).sum(),
                o2.getValues().stream().mapToDouble(CompoundInstance::getAmount).sum());
          })
          .map(IMediationCandidate::getValues);
    }

    @Override
    public boolean shouldIncompleteRecipeBeProcessed(IEquivalencyRecipe recipe) {
      return false;
    }

    @Override
    public boolean canContributeToRecipeAsInput(IEquivalencyRecipe recipe, CompoundInstance compoundInstance) {
      return true;
    }

    @Override
    public boolean canContributeToRecipeAsOutput(IEquivalencyRecipe recipe, CompoundInstance compoundInstance) {
      return true;
    }

    @Override
    public boolean isValidFor(ICompoundContainer<?> wrapper, CompoundInstance compoundInstance) {
      return true;
    }

    @Override
    public Optional<ValueAmounts> convertToCacheEntry(Set<CompoundInstance> instances) {
      if (instances.isEmpty()) {
        return Optional.empty();
      }

      ValueAmounts.Builder builder = new ValueAmounts.Builder();
      for (CompoundInstance i : instances) {
        ICompoundType t = i.getType();
        if (i.getType() instanceof ValueType) {
          builder.put(((ValueType) i.getType()).getId(), i.getAmount().intValue());
        }
      }
      return Optional.of(builder.build());
    }
  }

  public static class ValueType extends ForgeRegistryEntry<ICompoundType> implements ICompoundType {
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

  public static class ValueAmounts implements INBTSerializable<IntArrayNBT> {
    public static final ValueAmounts EMPTY = new ValueAmounts();

    public static final Codec<ValueAmounts> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("value1").forGetter(i -> i.get(0)),
            Codec.INT.fieldOf("value2").forGetter(i -> i.get(1)),
            Codec.INT.fieldOf("value3").forGetter(i -> i.get(2)),
            Codec.INT.fieldOf("value4").forGetter(i -> i.get(3)),
            Codec.INT.fieldOf("value5").forGetter(i -> i.get(4)),
            Codec.INT.fieldOf("value6").forGetter(i -> i.get(5)),
            Codec.INT.fieldOf("value7").forGetter(i -> i.get(6)),
            Codec.INT.fieldOf("value8").forGetter(i -> i.get(7)),
            Codec.INT.fieldOf("value9").forGetter(i -> i.get(8)),
            Codec.INT.fieldOf("value10").forGetter(i -> i.get(9)),
            Codec.INT.fieldOf("value15").forGetter(i -> i.get(10)),
            Codec.INT.fieldOf("value20").forGetter(i -> i.get(11)),
            Codec.INT.fieldOf("value25").forGetter(i -> i.get(12)),
            Codec.INT.fieldOf("value50").forGetter(i -> i.get(13)),
            Codec.INT.fieldOf("value75").forGetter(i -> i.get(14)),
            Codec.INT.fieldOf("value100").forGetter(i -> i.get(15))
        ).apply(instance, ValueAmounts::new));

    public int[] elements = new int[16];

    public ValueAmounts() {
    }

    public ValueAmounts(int value1, int value2, int value3, int value4, int value5, int value6, int value7, int value8, int value9, int value10, int value15, int value20, int value25, int value50, int value75, int value100) {
      elements[0] = value1;
      elements[1] = value2;
      elements[2] = value3;
      elements[3] = value4;
      elements[4] = value5;
      elements[5] = value6;
      elements[6] = value7;
      elements[7] = value8;
      elements[8] = value9;
      elements[9] = value10;
      elements[10] = value15;
      elements[11] = value20;
      elements[12] = value25;
      elements[13] = value50;
      elements[14] = value75;
      elements[15] = value100;
    }

    private ValueAmounts(final ValueAmounts other) {
      this(other.elements);
    }

    private ValueAmounts(int[] incoming) {
      System.arraycopy(incoming, 0, elements, 0, 16);
    }

    private ValueAmounts(IntArrayNBT tag) {
      deserializeNBT(tag);
    }

    public int get(int i) {
      if (i < 0 || i >= 16) {
        throw new IndexOutOfBoundsException();
      }

      return elements[i];
    }

    public int sum() {
      int result = 0;
      for (int x : elements) {
        result += x;
      }
      return result;
    }

    @Override
    public IntArrayNBT serializeNBT() {
      return new IntArrayNBT(elements);
    }

    @Override
    public void deserializeNBT(IntArrayNBT incoming) {
      System.arraycopy(incoming, 0, elements, 0, 16);
    }

    public static class Builder {
      private Object2IntOpenHashMap<String> valueMap = new Object2IntOpenHashMap<>();

      public Builder() {
        valueMap.put("value1", 0);
        valueMap.put("value2", 0);
        valueMap.put("value3", 0);
        valueMap.put("value4", 0);
        valueMap.put("value5", 0);
        valueMap.put("value6", 0);
        valueMap.put("value7", 0);
        valueMap.put("value8", 0);
        valueMap.put("value9", 0);
        valueMap.put("value10", 0);
        valueMap.put("value15", 0);
        valueMap.put("value20", 0);
        valueMap.put("value25", 0);
        valueMap.put("value30", 0);
        valueMap.put("value50", 0);
        valueMap.put("value75", 0);
        valueMap.put("value100", 0);
        valueMap.put("value150", 0);
      }

      public ValueAmounts build() {
        return new ValueAmounts(new int[]{valueMap.getInt("value1"), valueMap.getInt("value2"), valueMap.getInt("value3"), valueMap.getInt("value4"), valueMap.getInt("value5"), valueMap.getInt("value6"), valueMap.getInt("value7"), valueMap.getInt("value8"), valueMap.getInt("value9"), valueMap.getInt("value10"), valueMap.getInt("value15"), valueMap.getInt("value20"), valueMap.getInt("value25"), valueMap.getInt("value30"), valueMap.getInt("value50"), valueMap.getInt("value75"), valueMap.getInt("value100"), valueMap.getInt("value150")});
      }

      public void put(String name, int value) {
        if (!valueMap.containsKey(name)) {
          throw new IndexOutOfBoundsException();
        }

        valueMap.put(name, value);
      }
    }
  }

  public static class ConversionCache {
    public static ConversionCache get(@Nullable World world) {
      if (aeqGetter != null) {
        return aeqGetter.apply(world);
      }

      throw new RuntimeException();
    }

    public static Function<World, ConversionCache> aeqGetter = null;

    private Map<Item, ValueAmounts> mappings = new HashMap<>();

    public void setMappings(Map<Item, ValueAmounts> mappings) {
      this.mappings = mappings;
    }

    public int getAmount(ItemStack stack, boolean wholeStack) {
      int count = stack.getCount();
      if (count > 1) {
        stack = stack.copy();
        stack.setCount(1);
      }

      ValueAmounts amount = getAmounts(stack.getItem());
      return wholeStack && count > 1 ? amount.sum() * count : amount.sum();
    }

    public ValueAmounts getAmounts(Item item) {
      return mappings.getOrDefault(item, ValueAmounts.EMPTY);
    }

    public void clear() {
      this.mappings.clear();
    }
  }

  public static class ValueConversionCache {
    private final
  }
}
