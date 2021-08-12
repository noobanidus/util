package noobanidus.mods.util.aeq;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class ValueAmounts implements INBTSerializable<ListNBT> {
  public static final ValueAmounts EMPTY = new ValueAmounts();

/*  public static final Codec<ValueAmounts> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(
          Codec.DOUBLE.fieldOf("value1").forGetter(i -> i.get(0)),
          Codec.DOUBLE.fieldOf("value2").forGetter(i -> i.get(1)),
          Codec.DOUBLE.fieldOf("value3").forGetter(i -> i.get(2)),
          Codec.DOUBLE.fieldOf("value4").forGetter(i -> i.get(3)),
          Codec.DOUBLE.fieldOf("value5").forGetter(i -> i.get(4)),
          Codec.DOUBLE.fieldOf("value6").forGetter(i -> i.get(5)),
          Codec.DOUBLE.fieldOf("value7").forGetter(i -> i.get(6)),
          Codec.DOUBLE.fieldOf("value8").forGetter(i -> i.get(7)),
          Codec.DOUBLE.fieldOf("value9").forGetter(i -> i.get(8)),
          Codec.DOUBLE.fieldOf("value10").forGetter(i -> i.get(9)),
          Codec.DOUBLE.fieldOf("value15").forGetter(i -> i.get(10)),
          Codec.DOUBLE.fieldOf("value20").forGetter(i -> i.get(11)),
          Codec.DOUBLE.fieldOf("value25").forGetter(i -> i.get(12)),
          Codec.DOUBLE.fieldOf("value50").forGetter(i -> i.get(13)),
          Codec.DOUBLE.fieldOf("value75").forGetter(i -> i.get(14)),
          Codec.DOUBLE.fieldOf("value100").forGetter(i -> i.get(15))
      ).apply(instance, ValueAmounts::new));*/

  public double[] elements = new double[16];

  public ValueAmounts() {
  }

  public ValueAmounts(double value1, double value2, double value3, double value4, double value5, double value6, double value7, double value8, double value9, double value10, double value15, double value20, double value25, double value50, double value75, double value100) {
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

  private ValueAmounts(double[] incoming) {
    System.arraycopy(incoming, 0, elements, 0, 16);
  }

  private ValueAmounts(ListNBT tag) {
    deserializeNBT(tag);
  }

  public double get(int i) {
    if (i < 0 || i >= 16) {
      throw new IndexOutOfBoundsException();
    }

    return elements[i];
  }

  public double sum() {
    double result = 0;
    for (double x : elements) {
      result += x;
    }
    return result;
  }

  @Override
  public ListNBT serializeNBT() {
      final ListNBT listNBT = new ListNBT();

      for (final double element : elements)
      {
          listNBT.add(DoubleNBT.valueOf(element));
      }

    return listNBT;
  }

  @Override
  public void deserializeNBT(ListNBT incoming) {
      for (int i = 0; i < elements.length; i++)
      {
          elements[i] = incoming.getDouble(i);
      }
  }

  public static class Builder {
    private final Map<String, Double> valueMap = new HashMap<>();

    public Builder() {
      valueMap.put("value1", 0d);
      valueMap.put("value2", 0d);
      valueMap.put("value3", 0d);
      valueMap.put("value4", 0d);
      valueMap.put("value5", 0d);
      valueMap.put("value6", 0d);
      valueMap.put("value7", 0d);
      valueMap.put("value8", 0d);
      valueMap.put("value9", 0d);
      valueMap.put("value10", 0d);
      valueMap.put("value15", 0d);
      valueMap.put("value20", 0d);
      valueMap.put("value25", 0d);
      valueMap.put("value50", 0d);
      valueMap.put("value75", 0d);
      valueMap.put("value100", 0d);
    }

    public ValueAmounts build() {
      return new ValueAmounts(new double[]{valueMap.get("value1"), valueMap.get("value2"), valueMap.get("value3"), valueMap.get("value4"), valueMap.get("value5"), valueMap.get("value6"), valueMap.get("value7"), valueMap.get("value8"), valueMap.get("value9"), valueMap.get("value10"), valueMap.get("value15"), valueMap.get("value20"), valueMap.get("value25"), valueMap.get("value50"), valueMap.get("value75"), valueMap.get("value100")});
    }

    public void put(String name, double value) {
      if (!valueMap.containsKey(name)) {
        throw new IndexOutOfBoundsException();
      }

      valueMap.put(name, value);
    }
  }
}
