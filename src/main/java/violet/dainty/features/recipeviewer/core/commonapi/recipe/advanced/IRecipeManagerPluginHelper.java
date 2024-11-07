package violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

/**
 * Helpers for implementing {@link IRecipeManagerPlugin}s.
 *
 * @since 19.15.1
 */
public interface IRecipeManagerPluginHelper {
	/**
	 * @return true if the given focus should be treated as a catalyst of this recipe type.
	 * @since 19.15.1
	 */
	boolean isRecipeCatalyst(RecipeType<?> recipeType, IFocus<?> focus);
}
