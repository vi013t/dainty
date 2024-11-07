package violet.dainty.features.recipeviewer.core.commonapi.runtime;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

/**
 * JEI's gui for displaying recipes. Use this interface to open recipes.
 * Get the instance from {@link IJeiRuntime#getRecipesGui()}.
 */
public interface IRecipesGui {
	/**
	 * Show recipes for an {@link IFocus}.
	 * Opens the {@link IRecipesGui} if recipes are found and the gui is closed.
	 *
	 * @see IFocusFactory#createFocus(RecipeIngredientRole, IIngredientType, Object)
	 */
	default <V> void show(IFocus<V> focus) {
		show(List.of(focus));
	}

	/**
	 * Show recipes for multiple {@link IFocus}.
	 * Opens the {@link IRecipesGui} if recipes are found and the gui is closed.
	 *
	 * @see IFocusFactory#createFocus(RecipeIngredientRole, IIngredientType, Object)
	 *
	 * @since 9.3.0
	 */
	void show(List<IFocus<?>> focuses);

	/**
	 * Show entire categories of recipes.
	 *
	 * @param recipeTypes a list of recipe types to display, in order. Must not be empty.
	 */
	void showTypes(List<RecipeType<?>> recipeTypes);

	/**
	 * Show specific recipes for one recipe category, with multiple {@link IFocus}.
	 * Opens the {@link IRecipesGui} if recipes are valid and the gui is closed.
	 *
	 * @see IFocusFactory#createFocus(RecipeIngredientRole, IIngredientType, Object)
	 *
	 * @since 19.1.0
	 */
	<T> void showRecipes(IRecipeCategory<T> recipeCategory, List<T> recipes, List<IFocus<?>> focuses);

	/**
	 * @return the ingredient that's currently under the mouse in this gui
	 */
	<T> Optional<T> getIngredientUnderMouse(IIngredientType<T> ingredientType);

	/**
	 * Get the screen that the {@link IRecipesGui} was opened from.
	 * When the {@link IRecipesGui} is closed, it will re-open the parent screen.
	 *
	 * If the {@link IRecipesGui} is not open, this will return {@link Optional#empty()}.
	 *
	 * @since 19.20.0
	 */
	Optional<Screen> getParentScreen();
}