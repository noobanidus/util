package noobanidus.libs.util.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.util.Util;
import noobanidus.libs.util.recipes.EnchantmentRecipe;

public class ModRecipes {
  public static DeferredRegister<IRecipeSerializer<?>> recipeRegistry = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Util.MODID);

  public static RegistryObject<SpecialRecipeSerializer<EnchantmentRecipe>> ENCHANTMENT_RECIPE = recipeRegistry.register("enchantment_recipe", () -> new SpecialRecipeSerializer<>(EnchantmentRecipe::new));
}
