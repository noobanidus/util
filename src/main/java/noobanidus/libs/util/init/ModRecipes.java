package noobanidus.libs.util.init;

import com.tterrag.registrate.util.RegistryEntry;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import noobanidus.libs.util.recipes.EnchantmentRecipe;

import static noobanidus.libs.util.Util.REGISTRATE;


public class ModRecipes {

  public static RegistryEntry<SpecialRecipeSerializer<EnchantmentRecipe>> ENCHANTMENT_RECIPE = REGISTRATE.recipeSerializer("enchantment_recipe", () -> new SpecialRecipeSerializer<>(EnchantmentRecipe::new)).register();

  public static void load() {

  }
}
