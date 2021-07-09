package noobanidus.mods.util.init;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.item.Items;
import noobanidus.mods.util.UtilTags;

import static noobanidus.mods.util.Util.REGISTRATE;

public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, p -> {
      p.getOrCreateBuilder(UtilTags.VALUE1).add(Items.COAL, Items.IRON_NUGGET, Items.QUARTZ);    // Coal, Iron Nugget, Quartz
      p.getOrCreateBuilder(UtilTags.VALUE2).add(Items.SLIME_BALL, Items.NETHER_WART);    // Slime Ball, Nether Wart
      p.getOrCreateBuilder(UtilTags.VALUE3).add(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE, Items.LAPIS_LAZULI, Items.EXPERIENCE_BOTTLE, Items.PRISMARINE_CRYSTALS);    // Gunpowder, Redstone, Glowstone, Lapis Lazuli, Experience Bottle
      p.getOrCreateBuilder(UtilTags.VALUE4).add(Items.GOLD_NUGGET, Items.BLAZE_POWDER, Items.EMERALD);    // Gold Nugget, Blaze Powder, Emerald
      p.getOrCreateBuilder(UtilTags.VALUE5).add(Items.IRON_INGOT, Items.HONEY_BOTTLE, Items.PRISMARINE_SHARD);    // Iron Ingot, Prismarine?, Honey Bottle
      p.getOrCreateBuilder(UtilTags.VALUE6).add(Items.DRAGON_BREATH);    // Dragon Breath
      p.getOrCreateBuilder(UtilTags.VALUE7).add(Items.SCUTE, Items.PHANTOM_MEMBRANE);    // Scute, Phantom Memberane
      p.getOrCreateBuilder(UtilTags.VALUE8).add(Items.GOLD_INGOT);    // Gold Ingot
      p.getOrCreateBuilder(UtilTags.VALUE9).add(Items.BLAZE_ROD, Items.RABBIT_FOOT);    // Blaze Rod, Rabbit Foot
      p.getOrCreateBuilder(UtilTags.VALUE10).add(Items.ENDER_PEARL, Items.GHAST_TEAR, Items.SHULKER_SHELL, Items.ENCHANTED_BOOK); // Ender Pearl, Ghast Tear, Shulker Shell, Enchanted Book
      p.getOrCreateBuilder(UtilTags.VALUE15).add(Items.DIAMOND,Items.HEART_OF_THE_SEA,Items.NAUTILUS_SHELL, Items.CRYING_OBSIDIAN);   // Diamond, Nautilus Shell, Crying Obsidian
      p.getOrCreateBuilder(UtilTags.VALUE20).add(Items.NETHERITE_SCRAP);   // Netherite Scrap
      p.getOrCreateBuilder(UtilTags.VALUE25).add(Items.ANCIENT_DEBRIS);   // Ancient Debris
      p.getOrCreateBuilder(UtilTags.VALUE50).add(Items.DRAGON_HEAD, Items.ELYTRA);   // Dragon Head, Elytra
      p.getOrCreateBuilder(UtilTags.VALUE75).add(Items.DRAGON_EGG);   // Dragon Egg
      p.getOrCreateBuilder(UtilTags.VALUE100).add(Items.ENCHANTED_GOLDEN_APPLE, Items.TOTEM_OF_UNDYING, Items.NETHER_STAR);  // Enchanted Golden Apple, Totem of Undying
    });
  }

  public static void load() {

  }
}
