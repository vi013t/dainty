package violet.dainty.features.recipeviewer.core.commonapi.registration;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IBookmarkOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientListOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;

/**
 * Allows mods to override the runtime classes for JEI with their own implementation.
 *
 * @since 12.0.2
 */
public interface IRuntimeRegistration {
	/**
	 * Set the ingredient list overlay.
	 *
	 * This is used by JEI's GUI and can be used by other mods
	 * that want to override JEI's GUI and have it still work with mods that use JEI's API.
	 */
	void setIngredientListOverlay(IIngredientListOverlay ingredientListOverlay);

	/**
	 * Set the bookmark list overlay.
	 *
	 * This is used by JEI's GUI and can be used by other mods
	 * that want to override JEI's GUI and have it still work with mods that use JEI's API.
	 */
	void setBookmarkOverlay(IBookmarkOverlay bookmarkOverlay);

	/**
	 * Set the Recipe GUI.
	 *
	 * This is used by JEI's GUI and can be used by other mods
	 * that want to override JEI's GUI and have it still work with mods that use JEI's API.
	 */
	void setRecipesGui(IRecipesGui recipesGui);

	/**
	 * Set the Ingredient Filter.
	 *
	 * This is used by JEI's GUI and can be used by other mods
	 * that want to override JEI's GUI and have it still work with mods that use JEI's API.
	 */
	void setIngredientFilter(IIngredientFilter ingredientFilter);

	/**
	 * The {@link IRecipeManager} offers several functions for retrieving and handling recipes.
	 */
	IRecipeManager getRecipeManager();

	/**
	 * {@link IJeiHelpers} provides helpers and tools for addon mods.
	 */
	IJeiHelpers getJeiHelpers();

	/**
	 * The {@link IIngredientManager} has some useful functions related to recipe ingredients.
	 */
	IIngredientManager getIngredientManager();

	/**
	 * The {@link IIngredientVisibility} allows mod plugins to do advanced filtering of
	 * ingredients based on what is visible in JEI.
	 * @deprecated use {@link IJeiHelpers#getIngredientVisibility()}
	 */
	@Deprecated(since = "19.18.4", forRemoval = true)
	default IIngredientVisibility getIngredientVisibility() {
		return getJeiHelpers().getIngredientVisibility();
	}

	/**
	 * Get a helper for all runtime Screen functions.
	 * This is used by JEI's GUI and can be used by other mods that want to use the same information from JEI.
	 */
	IScreenHelper getScreenHelper();

	/**
	 * Get a manager that holds all the registered recipe transfer handlers.
	 * This is used by JEI's GUI and can be used by other mods that want to use the same information from JEI.
	 */
	IRecipeTransferManager getRecipeTransferManager();

	/**
	 * Get access to the edit-mode config, which lets users hide ingredients from JEI.
	 * This is used by JEI's GUI and can be used by other mods that want to use the same information from JEI.
	 */
	IEditModeConfig getEditModeConfig();
}
