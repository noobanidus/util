package noobanidus.mods.util.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.util.Util;

import java.nio.file.Path;
import java.util.*;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static ForgeConfigSpec COMMON_CONFIG;
  public static ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_BLACKLIST;
  public static ForgeConfigSpec.BooleanValue UNICORN;
  public static ForgeConfigSpec.IntValue UNICORN_CHANCE;

  public static ForgeConfigSpec.BooleanValue CHERRY_TREES;
  public static ForgeConfigSpec.IntValue CHERRY_TREE_COUNT;
  public static ForgeConfigSpec.DoubleValue CHERRY_TREE_EXTRA;
  public static ForgeConfigSpec.IntValue CHERRY_TREE_EXTRA_COUNT;

  public static ForgeConfigSpec.IntValue STABLE_SEPARATION;
  public static ForgeConfigSpec.IntValue STABLE_SPACING;
  public static ForgeConfigSpec.BooleanValue MODIFY_STABLES;

  public static ForgeConfigSpec.BooleanValue SPAWNER_BLOCK;

  static {
    COMMON_BUILDER.push("Item Blacklist");

    ITEM_BLACKLIST = COMMON_BUILDER.comment("List of items to blacklist for deletion in the form of modname:itemname").defineList("item_blacklist", new ArrayList<>(Arrays.asList("minecraft:egg", "minecraft:glowstone", "quark:root_item", "druidcraft:elderberries", "minecraft:bone", "minecraft:arrow", "minecraft:ink_sac", "minecraft:rotten_flesh", "swampexpansion:cattail_seeds", "minecraft:dirt", "mysticalworld:silk_cocoon", "minecraft:powered_rail", "minecraft:rail", "botania:white_mushroom", "botania:orange_mushroom", "botania:magenta_mushroom", "botania:light_blue_mushroom", "botania:yellow_mushroom", "botania:lime_mushroom", "botania:pink_mushroom", "botania:gray_mushroom", "botania:light_gray_mushroom", "botania:cyan_mushroom", "botania:purple_mushroom", "botania:blue_mushroom", "botania:brown_mushroom", "botania:green_mushroom", "botania:red_mushroom", "botania:black_mushroom", "minecraft:sugar_cane")), (s) -> ((String) s).contains(":"));
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("Unicorn");
    UNICORN = COMMON_BUILDER.comment("whether or not spawns for the ultimate unicorn mod should be adjusted").define("unicorn_spawn_adjustment", true);
    UNICORN_CHANCE = COMMON_BUILDER.comment("the chance (for all unicorns as a whole) for a unicorn to spawn, default 4").defineInRange("unicorn_chance", 4, 0, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("Cherry Trees");
    CHERRY_TREES = COMMON_BUILDER.comment("whether or not the spawn frequency of cherry trees should be adjusted").define("cherry_tree", true);
    CHERRY_TREE_COUNT = COMMON_BUILDER.comment("the count chance of a cherry tree per chunk").defineInRange("cherry_tree_count", 0, 0, Integer.MAX_VALUE);
    CHERRY_TREE_EXTRA = COMMON_BUILDER.comment("the chance of an extra tree per chunk").defineInRange("cherry_tree_extra", 0.05, 0, Integer.MAX_VALUE);
    CHERRY_TREE_EXTRA_COUNT = COMMON_BUILDER.comment("the number of extra trees that should be placed").defineInRange("cherry_tree_extra_count", 1, 0, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("Spawners");
    SPAWNER_BLOCK = COMMON_BUILDER.comment("whether or not spawners should be blocked from having their spawn entity replaced by right-clicking with a spawn egg").define("block_spawner_eggs", true);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("Stables");
    MODIFY_STABLES = COMMON_BUILDER.comment("whether or not the structure separation for stables should be modified").define("modify_stables", true);
    STABLE_SPACING = COMMON_BUILDER.comment("the spacing between stables structures").defineInRange("stable_spacing", 60, 1, Integer.MAX_VALUE);
    STABLE_SEPARATION = COMMON_BUILDER.comment("the separation between stable structures").defineInRange("stable_separation", 40, 1, Integer.MAX_VALUE);
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static boolean shouldUnicorn() {
    return UNICORN.get();
  }

  public static int unicornChance() {
    return UNICORN_CHANCE.get();
  }

  public static boolean shouldCherry() {
    return CHERRY_TREES.get();
  }

  public static int cherryCount() {
    return CHERRY_TREE_COUNT.get();
  }

  public static float cherryExtra() {
    return CHERRY_TREE_EXTRA.get().floatValue();
  }

  public static int cherryExtraCount() {
    return CHERRY_TREE_EXTRA_COUNT.get();
  }

  public static boolean blockSpawners() {
    return SPAWNER_BLOCK.get();
  }

  public static int getStableSpacing () {
    return STABLE_SPACING.get();
  }

  public static int getStableSeparation () {
    return STABLE_SPACING.get();
  }

  public static boolean shouldModifyStables () {
    return MODIFY_STABLES.get();
  }

  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }

  public static void onReload(ModConfig.Reloading event) {
    reset(event.getConfig());
  }

  public static void onLoading(ModConfig.Loading event) {
    reset(event.getConfig());
  }

  private static Set<Item> blacklist = null;

  public static Set<Item> getItemBlacklist() {
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
