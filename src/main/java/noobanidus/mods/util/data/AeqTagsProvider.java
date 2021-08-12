package noobanidus.mods.util.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.util.Util;
import noobanidus.mods.util.UtilTags;

import javax.annotation.Nullable;

public class AeqTagsProvider extends ItemTagsProvider {
  @Override
  protected void registerTags() {
    getOrCreateBuilder(UtilTags.VALUE1).add(Items.COAL, Items.QUARTZ);    // Coal, Iron Nugget, Quartz
    getOrCreateBuilder(UtilTags.VALUE2).add(Items.SLIME_BALL, Items.NETHER_WART);    // Slime Ball, Nether Wart
    getOrCreateBuilder(UtilTags.VALUE3).add(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE, Items.LAPIS_LAZULI, Items.EXPERIENCE_BOTTLE, Items.PRISMARINE_CRYSTALS);    // Gunpowder, Redstone, Glowstone, Lapis Lazuli, Experience Bottle
    getOrCreateBuilder(UtilTags.VALUE4).add(Items.EMERALD);    // Gold Nugget, Blaze Powder, Emerald
    getOrCreateBuilder(UtilTags.VALUE5).add(Items.IRON_INGOT, Items.PRISMARINE_SHARD);    // Iron Ingot, Prismarine?, Honey Bottle
    getOrCreateBuilder(UtilTags.VALUE6).add(Items.HONEY_BOTTLE);    // Dragon Breath
    getOrCreateBuilder(UtilTags.VALUE7).add(Items.SCUTE, Items.PHANTOM_MEMBRANE);    // Scute, Phantom Memberane
    getOrCreateBuilder(UtilTags.VALUE8).add(Items.DRAGON_BREATH);    // Gold Ingot
    getOrCreateBuilder(UtilTags.VALUE9).add(Items.GOLD_INGOT, Items.RABBIT_FOOT);    // Blaze Rod, Rabbit Foot
    getOrCreateBuilder(UtilTags.VALUE10).add(Items.BLAZE_ROD, Items.ENDER_PEARL, Items.GHAST_TEAR, Items.SHULKER_SHELL, Items.ENCHANTED_BOOK); // Ender Pearl, Ghast Tear, Shulker Shell, Enchanted Book
    getOrCreateBuilder(UtilTags.VALUE15).add(Items.DIAMOND, Items.HEART_OF_THE_SEA, Items.NAUTILUS_SHELL, Items.CRYING_OBSIDIAN);   // Diamond, Nautilus Shell, Crying Obsidan
    getOrCreateBuilder(UtilTags.VALUE20).add(Items.NETHERITE_SCRAP);   // Netherite Scrap
    getOrCreateBuilder(UtilTags.VALUE25).add(Items.ANCIENT_DEBRIS);   // Ancient Debris
    getOrCreateBuilder(UtilTags.VALUE50).add(Items.DRAGON_HEAD, Items.ELYTRA);   // Dragon Head, Elytra
    getOrCreateBuilder(UtilTags.VALUE75).add(Items.DRAGON_EGG);   // Dragon Egg
    getOrCreateBuilder(UtilTags.VALUE100).add(Items.ENCHANTED_GOLDEN_APPLE, Items.TOTEM_OF_UNDYING, Items.NETHER_STAR);  // Enchanted Golden Apple, Totem of Undying
  }

  public AeqTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(dataGenerator, blockTagProvider, Util.MODID, existingFileHelper);
  }

  public static class AeqBlockTagsProvider extends BlockTagsProvider {
    public AeqBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
      super(generatorIn, Util.MODID, existingFileHelper);
    }
  }
}
