/*
package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.IAequivaleoAPI;
import com.ldtteam.aequivaleo.api.results.IEquivalencyResults;
import com.ldtteam.aequivaleo.api.results.IResultsInformationCache;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import noobanidus.mods.util.init.ModAeq;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConversionCache {
  private final RegistryKey<World> worldKey;

  public ConversionCache(RegistryKey<World> worldKey) {
    this.worldKey = worldKey;
  }

  private final Map<Item, ValueAmounts> mappings = new HashMap<>();

  public double getAmount(ItemStack stack, boolean wholeStack) {
    int count = stack.getCount();
    if (count > 1) {
      stack = stack.copy();
      stack.setCount(1);
    }

    ValueAmounts amount = getAmounts(stack);
    return (wholeStack && count > 1 ? amount.sum() * count : amount.sum());
  }

  public ValueAmounts getAmounts(ItemStack stack) {
    ValueAmounts result = mappings.get(stack.getItem());
    if (result == null) {
      IEquivalencyResults cache = IAequivaleoAPI.getInstance().getEquivalencyResults(worldKey);
      Optional<ValueAmounts> am = cache.mappedDataFor(ModAeq.VALUE.get(), stack.getItem());
      result = am.orElse(ValueAmounts.EMPTY);
      mappings.put(stack.getItem(), result);
    }
    return result;
*/
/*    return mappings.computeIfAbsent(stack.getItem(), item -> {
      IResultsInformationCache aeqCache = IAequivaleoAPI.getInstance().getResultsInformationCache(worldKey);
      Optional<ValueAmounts> am = aeqCache.getCacheFor(ModAeq.VALUE.get(), item);
      return am.orElse(ValueAmounts.EMPTY);
    });*//*

  }

  public void clear() {
    this.mappings.clear();
  }
}
*/
