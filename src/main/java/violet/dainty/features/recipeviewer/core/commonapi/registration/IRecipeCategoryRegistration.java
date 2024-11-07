package violet.dainty.features.recipeviewer.core.commonapi.registration;

import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

/**
 * This is given to your {@link IModPlugin#registerCategories(IRecipeCategoryRegistration)}.
 */
public interface IRecipeCategoryRegistration {
	/**
	 * {@link IJeiHelpers} provides helpers and tools for addon mods.
	 */
	IJeiHelpers getJeiHelpers();

	/**
	 * Add the recipe categories provided by this plugin.
	 */
	void addRecipeCategories(IRecipeCategory<?>... recipeCategories);
}
