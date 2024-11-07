package violet.dainty.features.recipeviewer.core.commonapi.ingredients;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;

import java.util.List;

/**
 * A supplier for ingredients.
 * Useful for getting ingredients out of a recipe.
 *
 * Get an instance from {@link IRecipeManager#getRecipeIngredients}
 *
 * @since 19.9.0
 */
@ApiStatus.NonExtendable
public interface IIngredientSupplier {
	/**
	 * Get all the ingredients for the given role.
	 * @since 19.9.0
	 */
	@Unmodifiable
	List<ITypedIngredient<?>> getIngredients(RecipeIngredientRole role);
}
