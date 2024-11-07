package violet.dainty.features.recipeviewer.core.commonapi.recipe;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

import java.util.stream.Stream;

/**
 * This is a helper class for looking up recipe catalysts.
 * Create one with {@link IRecipeManager#createRecipeCatalystLookup(RecipeType)},
 * then set its properties and call {@link #get()} to get the results.
 *
 * @since 9.5.0
 */
public interface IRecipeCatalystLookup {
	/**
	 * By default, hidden results are not returned.
	 * Calling this will make this lookup include hidden recipe catalysts.
	 *
	 * @since 9.5.0
	 */
	IRecipeCatalystLookup includeHidden();

	/**
	 * Get the recipe catalyst results for this lookup.
	 *
	 * @since 9.5.0
	 */
	Stream<ITypedIngredient<?>> get();

	/**
	 * Get the recipe catalyst results of the given type for this lookup.
	 *
	 * @since 9.5.0
	 */
	<S> Stream<S> get(IIngredientType<S> ingredientType);

	/**
	 * Get the ItemStack recipe catalyst results for this lookup.
	 *
	 * @since 11.1.1
	 */
	default Stream<ItemStack> getItemStack() {
		return get(VanillaTypes.ITEM_STACK);
	}
}
