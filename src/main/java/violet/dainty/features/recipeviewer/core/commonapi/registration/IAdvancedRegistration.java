package violet.dainty.features.recipeviewer.core.commonapi.registration;

import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPluginHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.ISimpleRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.IRecipeCategoryDecorator;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiFeatures;

/**
 * The IAdvancedRegistration instance is passed to your mod plugin in {@link IModPlugin#registerAdvanced(IAdvancedRegistration)}.
 */
public interface IAdvancedRegistration {
	/**
	 * {@link IJeiHelpers} provides helpers and tools for addon mods.
	 */
	IJeiHelpers getJeiHelpers();

	/**
	 * Helpers for implementing {@link IRecipeManagerPlugin}s.
	 *
	 * @since 19.15.1
	 */
	IRecipeManagerPluginHelper getRecipeManagerPluginHelper();

	/**
	 * Register your own {@link IRecipeManagerPlugin} here.
	 */
	void addRecipeManagerPlugin(IRecipeManagerPlugin recipeManagerPlugin);

	/**
	 * Register your own {@link ISimpleRecipeManagerPlugin} here.
	 *
	 * @since 19.16.0
	 */
	<T> void addTypedRecipeManagerPlugin(RecipeType<T> recipeType, ISimpleRecipeManagerPlugin<T> recipeManagerPlugin);

	/**
	 * Register a {@link IRecipeCategoryDecorator} for a recipe type.
	 *
	 * @since 15.1.0
	 */
	<T> void addRecipeCategoryDecorator(RecipeType<T> recipeType, IRecipeCategoryDecorator<T> decorator);

	/**
	 * Get access to disable various JEI features.
	 * This may be needed by mods that substantially change hard-coded vanilla behaviors.
	 *
	 * @since 17.3.0
	 */
	IJeiFeatures getJeiFeatures();
}
