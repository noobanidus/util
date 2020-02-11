package noobanidus.libs.util.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import noobanidus.libs.util.init.ModRecipes;

public class EnchantmentRecipe extends SpecialRecipe {
  public EnchantmentRecipe(ResourceLocation idIn) {
    super(idIn);
  }

  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack stack = inv.getStackInSlot(i);
      if (stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("Enchantments", Constants.NBT.TAG_LIST);
      }
    }

    return false;
  }

  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack stack = inv.getStackInSlot(i).copy();
      stack.setCount(1);
      if (stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("Enchantments", Constants.NBT.TAG_LIST)) {
          ListNBT enchants = tag.getList("Enchantments", Constants.NBT.TAG_COMPOUND);
          tag.remove("Enchantments");
          tag.put("StoredEnchantments", enchants);
        }
        stack.setTag(tag);
        return stack;
      }
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(int width, int height) {
    return true;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.ENCHANTMENT_RECIPE.get();
  }
}
