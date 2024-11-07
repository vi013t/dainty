package violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.smithing;

import net.minecraft.world.item.crafting.SmithingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IVanillaCategoryExtensionRegistration;

/**
 * Allows extending the vanilla smithing recipe category,
 * to support custom recipes classes that cannot be handled by default.
 *
 * Get the instance from {@link IVanillaCategoryExtensionRegistration#getSmithingCategory()}
 *
 * @since 19.5.0
 */
public interface IExtendableSmithingRecipeCategory {
	/**
	 * Add an extension that handles a subset of the recipes in the recipe category.
	 *
	 * @param recipeClass  the subset class of crafting recipes to handle
	 * @param extension    an extension for handling these recipes
	 * @since 19.5.0
	 */
	<R extends SmithingRecipe> void addExtension(
		Class<? extends R> recipeClass,
		ISmithingCategoryExtension<R> extension
	);
}
