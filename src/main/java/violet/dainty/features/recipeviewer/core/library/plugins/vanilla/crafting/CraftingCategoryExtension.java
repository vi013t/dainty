package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.ICraftingGridHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import violet.dainty.features.recipeviewer.core.library.util.RecipeUtil;

import java.util.List;
import java.util.Optional;

public class CraftingCategoryExtension implements ICraftingCategoryExtension<CraftingRecipe> {
	@Override
	public void setRecipe(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		CraftingRecipe recipe = recipeHolder.value();
		ItemStack resultItem = RecipeUtil.getResultItem(recipe);

		int width = getWidth(recipeHolder);
		int height = getHeight(recipeHolder);
		craftingGridHelper.createAndSetOutputs(builder, List.of(resultItem));
		craftingGridHelper.createAndSetIngredients(builder, recipe.getIngredients(), width, height);
	}

	@SuppressWarnings("removal")
	@Override
	public Optional<ResourceLocation> getRegistryName(RecipeHolder<CraftingRecipe> recipeHolder) {
		return Optional.of(recipeHolder.id());
	}

	@Override
	public int getWidth(RecipeHolder<CraftingRecipe> recipeHolder) {
		CraftingRecipe recipe = recipeHolder.value();
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			return shapedRecipe.getWidth();
		}
		if (recipe instanceof JeiShapedRecipe shapedRecipe) {
			return shapedRecipe.getWidth();
		}
		return 0;
	}

	@Override
	public int getHeight(RecipeHolder<CraftingRecipe> recipeHolder) {
		CraftingRecipe recipe = recipeHolder.value();
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			return shapedRecipe.getHeight();
		}
		if (recipe instanceof JeiShapedRecipe shapedRecipe) {
			return shapedRecipe.getHeight();
		}
		return 0;
	}

	@Override
	public boolean isHandled(RecipeHolder<CraftingRecipe> recipeHolder) {
		CraftingRecipe recipe = recipeHolder.value();
		return !recipe.isSpecial();
	}
}
