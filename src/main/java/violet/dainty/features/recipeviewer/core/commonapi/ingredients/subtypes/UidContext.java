package violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCatalystRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeTransferRegistration;

/**
 * Optional context used when getting Unique IDs for ingredients and subtypes.
 * Subtype interpreters can use this context to return different subtypes for recipes and ingredients.
 * When implementing this, {@link #Ingredient} subtypes should be more specific than {@link #Recipe} subtypes.
 *
 * @since 7.3.0
 */
public enum UidContext {
	/**
	 * Context used for comparing ingredients in the ingredient list.
	 * This is the main context and should be more specific than {@link #Recipe}.
	 *
	 * Used for:
	 * ingredients (see {@link IModIngredientRegistration}
	 * blacklists from the config
	 * debug info
	 * bookmarks
	 */
	Ingredient,

	/**
	 * Context used for comparing ingredients in recipes.
	 * This is a secondary context and should be less specific than {@link #Ingredient}, to allow for broader matches in recipes.
	 *
	 * Used for:
	 * recipe lookups (see {@link IRecipeCategory#setRecipe(IRecipeLayoutBuilder, Object, IFocusGroup)})
	 * recipe catalysts (see {@link IRecipeCatalystRegistration})
	 * recipe transfer (since JEI 7.4.0) (see {@link IRecipeTransferRegistration}
	 */
	Recipe
}
