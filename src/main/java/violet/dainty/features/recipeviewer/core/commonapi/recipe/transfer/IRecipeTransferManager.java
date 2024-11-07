package violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer;

import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.Optional;

/**
 * Holds all the registered recipe transfer handlers.
 *
 * @since 11.5.0
 */
public interface IRecipeTransferManager {
	/**
	 * Get a recipe transfer handler for the given container and recipe category, if one is registered for it.
	 *
	 * @since 11.5.0
	 */
	<C extends AbstractContainerMenu, R> Optional<IRecipeTransferHandler<C, R>> getRecipeTransferHandler(C container, IRecipeCategory<R> recipeCategory);
}
