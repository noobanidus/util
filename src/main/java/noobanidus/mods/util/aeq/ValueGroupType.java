package noobanidus.mods.util.aeq;

import com.ldtteam.aequivaleo.api.compound.CompoundInstance;
import com.ldtteam.aequivaleo.api.compound.container.ICompoundContainer;
import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.ldtteam.aequivaleo.api.compound.type.group.ICompoundTypeGroup;
import com.ldtteam.aequivaleo.api.mediation.IMediationCandidate;
import com.ldtteam.aequivaleo.api.mediation.IMediationEngine;
import com.ldtteam.aequivaleo.api.recipe.equivalency.IEquivalencyRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;
import java.util.Set;

public class ValueGroupType extends ForgeRegistryEntry<ICompoundTypeGroup> implements ICompoundTypeGroup {

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
    public Optional<?> mapEntry(final ICompoundContainer<?> container, final Set<CompoundInstance> instances)
    {
        if (instances.isEmpty()) {
            return Optional.empty();
        }

        ValueAmounts.Builder builder = new ValueAmounts.Builder();
        for (CompoundInstance i : instances) {
            ICompoundType t = i.getType();
            if (t instanceof ValueType) {
                builder.put(((ValueType) t).getId(), i.getAmount());
            }
        }
        return Optional.of(builder.build());
    }
}
