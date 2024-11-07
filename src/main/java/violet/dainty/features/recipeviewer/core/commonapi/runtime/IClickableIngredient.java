package violet.dainty.features.recipeviewer.core.commonapi.runtime;

import net.minecraft.client.renderer.Rect2i;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

/**
 * A clickable ingredient drawn on the screen, that JEI can use for recipe lookups.
 *
 * This can be an ingredient drawn in a GUI container slot, a fluid tank,
 * or anything else that holds ingredients.
 *
 * Create one with {@link IIngredientManager#createClickableIngredient}.
 *
 * @since 11.5.0
 */
public interface IClickableIngredient<T> {
	/**
	 * Get the typed ingredient that can be looked up by JEI for recipes.
	 *
	 * @since 11.5.0
	 * @deprecated use {@link #getIngredient()} and {@link #getIngredientType()} instead.
	 */
	@Deprecated(since = "19.12.0", forRemoval = true)
	ITypedIngredient<T> getTypedIngredient();

	/**
	 * @since 19.12.0
	 */
	default IIngredientType<T> getIngredientType() {
		return getTypedIngredient().getType();
	}

	/**
	 * @since 19.12.0
	 */
	default T getIngredient() {
		ITypedIngredient<T> typedIngredient = getTypedIngredient();
		return typedIngredient.getIngredient();
	}

	/**
	 * Get the area that this clickable ingredient is drawn in, in absolute screen coordinates.
	 * This is used for click handling, to ensure the mouse-down and mouse-up are on the same ingredient.
	 *
	 * @since 11.5.0
	 */
	Rect2i getArea();
}