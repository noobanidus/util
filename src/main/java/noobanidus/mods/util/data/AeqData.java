package noobanidus.mods.util.data;

import com.ldtteam.aequivaleo.api.compound.CompoundInstance;
import com.ldtteam.aequivaleo.api.compound.information.datagen.ForcedInformationProvider;
import net.minecraft.data.DataGenerator;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.UtilTags;
import noobanidus.mods.util.aeq.AeqPlugin;

public class AeqData extends ForcedInformationProvider {
  public AeqData(DataGenerator dataGenerator) {
    super(Util.MODID, dataGenerator);
  }

  @Override
  public void calculateDataToSave() {
    save(specFor(UtilTags.VALUE1).withCompounds(new CompoundInstance(AeqPlugin.VALUE1.get(), 1)));
  }
}
