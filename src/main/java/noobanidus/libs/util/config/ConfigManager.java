package noobanidus.libs.util.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.Util;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static ForgeConfigSpec COMMON_CONFIG;
  public static ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_BLACKLIST;

  static {
    COMMON_BUILDER.push("Item Blacklist");

    ITEM_BLACKLIST = COMMON_BUILDER.comment("List of items to blacklist for deletion in the form of modname:itemname").defineList("item_blacklist", new ArrayList<>(Arrays.asList("minecraft:egg", "minecraft:glowstone", "quark:root_item", "druidcraft:elderberries", "minecraft:bone", "minecraft:arrow", "minecraft:ink_sac", "minecraft:rotten_flesh", "swampexpansion:cattail_seeds", "minecraft:dirt", "mysticalworld:silk_cocoon", "minecraft:powered_rail", "minecraft:rail", "botania:white_mushroom", "botania:orange_mushroom", "botania:magenta_mushroom", "botania:light_blue_mushroom", "botania:yellow_mushroom", "botania:lime_mushroom", "botania:pink_mushroom", "botania:gray_mushroom", "botania:light_gray_mushroom", "botania:cyan_mushroom", "botania:purple_mushroom", "botania:blue_mushroom", "botania:brown_mushroom", "botania:green_mushroom", "botania:red_mushroom", "botania:black_mushroom", "minecraft:sugar_cane")), (s) -> ((String) s).contains(":"));
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }

  public static void onReload(ModConfig.ConfigReloading event) {
    reset(event.getConfig());
  }

  public static void onLoading(ModConfig.Loading event) {
    reset(event.getConfig());
  }

  private static Set<Item> blacklist = null;

  public static Set<Item> getItemBlacklist () {
    if (blacklist == null) {
      blacklist = new HashSet<>();
      for (String i : ITEM_BLACKLIST.get()) {
        ResourceLocation rl = new ResourceLocation(i);
        Item item = ForgeRegistries.ITEMS.getValue(rl);
        if (item == null || item == Items.AIR) {
          Util.LOG.error("Unable to find specified item in config: " + i);
        } else {
          blacklist.add(item);
        }
      }
    }
    return blacklist;
  }

  private static void reset(ModConfig config) {
    if (config.getType() == ModConfig.Type.COMMON) {
      COMMON_CONFIG.setConfig(config.getConfigData());
      blacklist = null;
    }
  }
}
