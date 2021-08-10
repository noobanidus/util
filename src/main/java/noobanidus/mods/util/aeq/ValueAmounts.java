package noobanidus.mods.util.aeq;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ValueAmounts implements INBTSerializable<IntArrayNBT> {
  public static final ValueAmounts EMPTY = new ValueAmounts();

/*  public static final Codec<ValueAmounts> CODEC = RecordCodecBuilder.create(instance ->
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
      ).apply(instance, ValueAmounts::new));*/

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
    System.arraycopy(incoming.getIntArray(), 0, elements, 0, 16);
  }

  public static class Builder {
    private final Object2IntOpenHashMap<String> valueMap = new Object2IntOpenHashMap<>();

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
