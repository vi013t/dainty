package violet.dainty.features.recipeviewer.core.library.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientSupplier;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.focus.FocusGroup;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.supplier.builder.IngredientSupplierBuilder;

public final class IngredientSupplierHelper {
	private static final Logger LOGGER = LogManager.getLogger();

	private IngredientSupplierHelper() {
	}

	public static <T> IIngredientSupplier getIngredientSupplier(T recipe, IRecipeCategory<T> recipeCategory, IIngredientManager ingredientManager) {
		IngredientSupplierBuilder builder = new IngredientSupplierBuilder(ingredientManager);
		if (!recipeCategory.isHandled(recipe)) {
			return builder.buildIngredientSupplier();
		}
		try {
			recipeCategory.setRecipe(builder, recipe, FocusGroup.EMPTY);
		} catch (RuntimeException | LinkageError e) {
			String recipeName = RecipeErrorUtil.getNameForRecipe(recipe);
			LOGGER.error("Found a broken recipe, failed to setRecipe with RecipeLayoutBuilder: {}\n", recipeName, e);
		}

		return builder.buildIngredientSupplier();
	}
}
