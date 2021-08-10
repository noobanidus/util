package noobanidus.mods.util.data;

import com.ldtteam.aequivaleo.api.compound.CompoundInstance;
import com.ldtteam.aequivaleo.api.compound.information.datagen.ForcedInformationProvider;
import com.ldtteam.aequivaleo.api.compound.type.ICompoundType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.init.ModAeq;

import java.util.Map;

public class AeqData extends ForcedInformationProvider {
  public AeqData(DataGenerator dataGenerator) {
    super(Util.MODID, dataGenerator);
  }

  @Override
  public void calculateDataToSave() {
    for (Map.Entry<ITag.INamedTag<Item>, RegistryEntry<ICompoundType>> val : ModAeq.MAP.entrySet()) {
      save(specFor(val.getKey()).withCompounds(new CompoundInstance(val.getValue().get(), 1)));
    }
  }
}
