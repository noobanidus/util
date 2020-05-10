package noobanidus.libs.util.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import noobanidus.libs.util.Util;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandConflicts {
  private static Path output = Paths.get("recipe.log");

  private final CommandDispatcher<CommandSource> dispatcher;

  public CommandConflicts(CommandDispatcher<CommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public CommandConflicts register() {
    this.dispatcher.register(builder(Commands.literal("conflicts").requires((o) -> o.hasPermissionLevel(2))));
    return this;
  }

  public LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      MinecraftServer server = c.getSource().getServer();
      RecipeManager manager = server.getRecipeManager();

      final Collection<RecipeWrapper> recipes = manager.recipes.getOrDefault(IRecipeType.CRAFTING, Collections.emptyMap()).values().stream().map(r -> new RecipeWrapper((ICraftingRecipe) r)).collect(Collectors.toList());
      new Thread("ConflictScan_mc_thread") {
        @Override
        public void run() {
          try {
            runConflictScan(recipes);
            c.getSource().sendFeedback(new StringTextComponent("Recipe conflicts generated!"), true);
          } catch (Exception e) {
            e.printStackTrace();
            c.getSource().sendFeedback(new StringTextComponent("Unable to complete recipe conflicts generation. See server log for traceback."), true);
          }
        }
      }.start();

      return 1;
    });
    return builder;
  }

  private void runConflictScan(final Collection<RecipeWrapper> recipes) {
    final List<Set<RecipeWrapper>> conflicts = new ArrayList<>();
    final Set<RecipeWrapper> skipped = new HashSet<>();
    final Set<RecipeWrapper> doneRecipes = new HashSet<>();

    for (RecipeWrapper recipe : recipes) {
      if (recipe.getRecipe() instanceof SpecialRecipe) {
        skipped.add(recipe);
        continue;
      }
      Set<RecipeWrapper> thisConflict = new HashSet<>();

      for (RecipeWrapper otherRecipe : recipes) {
        if (doneRecipes.contains(otherRecipe)) {
          continue;
        }

        if (recipe.test(otherRecipe)) {
          thisConflict.add(recipe);
          thisConflict.add(otherRecipe);
        }
      }

      doneRecipes.add(recipe);
      if (!thisConflict.isEmpty()) {
        conflicts.add(thisConflict);
      }
    }

    List<String> out = new ArrayList<>();
    out.add("Conflicting recipes, grouped by ResourceLocation:");

    conflicts.forEach(o -> {
      StringJoiner joiner = new StringJoiner(",");
      o.stream().map(RecipeWrapper::getRecipeId).map(ResourceLocation::toString).forEach(joiner::add);
      out.add(joiner.toString());
    });
    out.add("Skipped the following special recipes: ");
    skipped.forEach(o -> out.add(o.getRecipeId().toString()));

    try {
      Files.write(output, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException ignored) {
    }
  }

  @SuppressWarnings("Duplicates")
  public static class RecipeWrapper implements Predicate<RecipeWrapper> {
    private final ICraftingRecipe recipe;
    private final boolean shapeless;
    private final List<IngredientWrapper> ingredients;
    private final List<IngredientWrapper> allIngredients;

    public RecipeWrapper(ICraftingRecipe recipe) {
      this.recipe = recipe;
      shapeless = !(recipe instanceof IShapedRecipe);

      ingredients = new ArrayList<>();
      allIngredients = new ArrayList<>();
      int i = -1;
      for (Ingredient ing : recipe.getIngredients()) {
        i++;
        allIngredients.add(new IngredientWrapper(ing, i));
        if (ing == Ingredient.EMPTY) {
          continue;
        }
        ingredients.add(new IngredientWrapper(ing, i));
      }
    }

    private int width = -1;
    private int height = -1;

    @SuppressWarnings("unchecked")
    public int width() {
      if (width == -1) {
        if (shapeless) {
          width = Math.max(3, size());
        } else {
          final IShapedRecipe<CraftingInventory> shaped = (IShapedRecipe<CraftingInventory>) recipe;
          width = shaped.getRecipeWidth();
          height = shaped.getRecipeHeight();
        }
      }
      return width;
    }

    @SuppressWarnings("unchecked")
    public int height() {
      if (height == -1) {
        if (shapeless) {
          height = size() / 3 + size() % 3;
        } else {
          final IShapedRecipe<CraftingInventory> shaped = (IShapedRecipe<CraftingInventory>) recipe;
          width = shaped.getRecipeWidth();
          height = shaped.getRecipeHeight();
        }
      }
      return height;
    }

    public int size() {
      return ingredients.size();
    }

    public int actualSize() {
      return allIngredients.size();
    }

    public List<IngredientWrapper> getIngredients() {
      return ingredients;
    }

    public List<IngredientWrapper> getAllIngredients() {
      return allIngredients;
    }

    @Nullable
    public IngredientWrapper getIngredient (int slot) {
      if (slot >= actualSize()) {
        return null;
      }

      return allIngredients.get(slot);
    }

    @Nullable
    public IngredientWrapper getSlot(int x, int y) {
      if (x < width && y < height) {
        int index = width() * y + x;

        if (index < actualSize()) {
          return allIngredients.get(index);
        }
      }

      return null;
    }

    public boolean isShapeless() {
      return shapeless;
    }

    public ICraftingRecipe getRecipe() {
      return recipe;
    }

    public ResourceLocation getRecipeId() {
      return recipe.getId();
    }

    @Override
    public boolean test(RecipeWrapper recipeWrapper) {
      if (recipeWrapper.getRecipeId().equals(getRecipeId())) {
        return false;
      }

      if (recipeWrapper.size() != size()) {
        return false;
      }

      if (!shapeless && recipeWrapper.isShapeless()) {
        return recipeWrapper.test(this);
      } else if (shapeless && recipeWrapper.isShapeless()) {
        List<IngredientWrapper> mine = Lists.newArrayList(getIngredients());
        List<IngredientWrapper> theirs = Lists.newArrayList(recipeWrapper.getIngredients());
        outer: for (Iterator<IngredientWrapper> iter1 = mine.iterator(); iter1.hasNext(); ) {
          IngredientWrapper a = iter1.next();
          for (Iterator<IngredientWrapper> iter2 = theirs.iterator(); iter2.hasNext(); ) {
            IngredientWrapper b = iter2.next();
            if (a.test(b)) {
              iter2.remove();
              iter1.remove();
              continue outer;
            }
          }
        }
        return mine.isEmpty() && theirs.isEmpty();
      } else {
        if (width() != recipeWrapper.width() || height() != recipeWrapper.height()) {
          return false;
        }

        for (int i = 0; i < actualSize(); i++) {
          IngredientWrapper a = getIngredient(i);
          IngredientWrapper b = recipeWrapper.getIngredient(i);
          if (a != null && b != null && !a.test(b)) {
            return false;
          }
        }

        return true;
      }
    }
  }

  public static class IngredientWrapper implements Predicate<IngredientWrapper> {
    private final Ingredient ingredient;
    private final int index;

    public IngredientWrapper(Ingredient ingredient, int index) {
      this.ingredient = ingredient;
      this.index = index;
    }

    public Ingredient getIngredient() {
      return ingredient;
    }

    public int getIndex() {
      return index;
    }

    @Override
    public boolean test(IngredientWrapper incoming) {
      if (incoming == null) {
        return false;
      }

      final Ingredient ingredient = incoming.getIngredient();
      if (ingredient == Ingredient.EMPTY) {
        return this.ingredient == Ingredient.EMPTY;
      }

      for (ItemStack stack : this.ingredient.getMatchingStacks()) {
        if (ingredient.test(stack)) {
          return true;
        }
      }
      for (ItemStack stack : ingredient.getMatchingStacks()) {
        if (this.ingredient.test(stack)) {
          return true;
        }
      }
      return false;
    }
  }
}
