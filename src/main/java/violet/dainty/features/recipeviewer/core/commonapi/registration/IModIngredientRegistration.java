package violet.dainty.features.recipeviewer.core.commonapi.registration;

import com.mojang.serialization.Codec;

import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

import java.util.Collection;

/**
 * Allows registration of new types of ingredients, beyond the basic ItemStack and FluidStack.
 * After every mod has registered its ingredients, the {@link IIngredientManager} is created from this information.
 *
 * This is given to your {@link IModPlugin#registerIngredients(IModIngredientRegistration)}.
 */
public interface IModIngredientRegistration {
	ISubtypeManager getSubtypeManager();

	/**
	 * Gets an {@link IColorHelper} to help in implementing {@link IIngredientHelper#getColors(Object)} for {@link IIngredientHelper}s that are being registered.
	 *
	 * @since 7.6.3
	 */
	IColorHelper getColorHelper();

	/**
	 * Register a new type of ingredient.
	 *
	 * @param ingredientType       The type of the ingredient.
	 * @param allIngredients       A collection of every to be displayed in the ingredient list.
	 * @param ingredientHelper     The ingredient helper to allows JEI to get information about ingredients for searching and other purposes.
	 * @param ingredientRenderer   The ingredient render to allow JEI to render these ingredients in the ingredient list.
	 *                             This ingredient renderer must be configured to draw in a 16 by 16 pixel space.
	 * @param ingredientCodec      A serializer codec for this type of ingredient, used for saving ingredients to file.
	 *                             The codec should support "normalized" ingredients, so as an optimization the count does not need to be encoded.
	 *
	 * @since 19.9.0
	 */
	<V> void register(
		IIngredientType<V> ingredientType,
		Collection<V> allIngredients,
		IIngredientHelper<V> ingredientHelper,
		IIngredientRenderer<V> ingredientRenderer,
		Codec<V> ingredientCodec
	);

	/**
	 * Register a new type of ingredient.
	 *
	 * @param ingredientType     The type of the ingredient.
	 * @param allIngredients     A collection of every to be displayed in the ingredient list.
	 * @param ingredientHelper   The ingredient helper to allows JEI to get information about ingredients for searching and other purposes.
	 * @param ingredientRenderer The ingredient render to allow JEI to render these ingredients in the ingredient list.
	 *                           This ingredient renderer must be configured to draw in a 16 by 16 pixel space.
	 *
	 * @deprecated use {@link #register(IIngredientType, Collection, IIngredientHelper, IIngredientRenderer, Codec)}
	 */
	@Deprecated(since = "19.9.0", forRemoval = true)
	<V> void register(
		IIngredientType<V> ingredientType,
		Collection<V> allIngredients,
		IIngredientHelper<V> ingredientHelper,
		IIngredientRenderer<V> ingredientRenderer
	);
}
