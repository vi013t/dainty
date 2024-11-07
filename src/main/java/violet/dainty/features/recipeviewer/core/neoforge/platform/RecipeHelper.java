package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.util.List;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRecipeHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiBrewingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class RecipeHelper implements IPlatformRecipeHelper {
	@Override
	public Ingredient getBase(SmithingRecipe recipe) {
		if (recipe instanceof SmithingTransformRecipe transformRecipe) {
			return transformRecipe.base;
		}
		if (recipe instanceof SmithingTrimRecipe trimRecipe) {
			return trimRecipe.base;
		}
		return Ingredient.EMPTY;
	}

	@Override
	public Ingredient getAddition(SmithingRecipe recipe) {
		if (recipe instanceof SmithingTransformRecipe transformRecipe) {
			return transformRecipe.addition;
		}
		if (recipe instanceof SmithingTrimRecipe trimRecipe) {
			return trimRecipe.addition;
		}
		return Ingredient.EMPTY;
	}

	@Override
	public Ingredient getTemplate(SmithingRecipe recipe) {
		if (recipe instanceof SmithingTransformRecipe transformRecipe) {
			return transformRecipe.template;
		}
		if (recipe instanceof SmithingTrimRecipe trimRecipe) {
			return trimRecipe.template;
		}
		return Ingredient.EMPTY;
	}

	@Override
	public List<IJeiBrewingRecipe> getBrewingRecipes(IIngredientManager ingredientManager, IVanillaRecipeFactory vanillaRecipeFactory, PotionBrewing potionBrewing) {
		return BrewingRecipeMaker.getBrewingRecipes(ingredientManager, vanillaRecipeFactory, potionBrewing);
	}
}
