package violet.dainty.features.recipeviewer.core.commonapi.runtime;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

/**
 * The {@link IBookmarkOverlay} is JEI's gui that displays all the bookmarked ingredients next to an open container gui.
 * Use this interface to get information from it.
 * Get the instance from {@link IJeiRuntime#getBookmarkOverlay()}.
 */
public interface IBookmarkOverlay {
	/**
	 * @return the ingredient that's currently under the mouse.
	 * @since 9.3.0
	 */
	Optional<ITypedIngredient<?>> getIngredientUnderMouse();

	/**
	 * @return the ingredient that's currently under the mouse, or null if there is none.
	 */
	@Nullable
	<T> T getIngredientUnderMouse(IIngredientType<T> ingredientType);

	/**
	 * @return the ingredient that's currently under the mouse, or null if there is none.
	 */
	@Nullable
	default ItemStack getItemStackUnderMouse() {
		return getIngredientUnderMouse(VanillaTypes.ITEM_STACK);
	}
}
