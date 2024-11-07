package violet.dainty.features.recipeviewer.core.commonapi.recipe;

import java.util.Collection;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

/**
 * {@link IFocusFactory} helps with creating {@link IFocus} for JEI.
 * Get an instance from {@link IJeiHelpers#getFocusFactory()}.
 */
public interface IFocusFactory {
	/**
	 * Returns a new focus.
	 *
	 * @since 9.3.0
	 */
	<V> IFocus<V> createFocus(RecipeIngredientRole role, IIngredientType<V> ingredientType, V ingredient);

	/**
	 * Returns a new focus.
	 *
	 * @since 11.5.0
	 */
	<V> IFocus<V> createFocus(RecipeIngredientRole role, ITypedIngredient<V> typedIngredient);

	/**
	 * Returns a new focus group.
	 *
	 * @since 11.5.0
	 */
	IFocusGroup createFocusGroup(Collection<? extends IFocus<?>> focuses);

	/**
	 * Returns an empty focus group.
	 *
	 * @since 11.5.0
	 */
	IFocusGroup getEmptyFocusGroup();
}
