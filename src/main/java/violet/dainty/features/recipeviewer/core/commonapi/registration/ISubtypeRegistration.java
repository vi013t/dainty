package violet.dainty.features.recipeviewer.core.commonapi.registration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import net.minecraft.world.item.Item;

/**
 * Tell JEI how to interpret Components and capabilities when comparing and looking up ingredients.
 *
 * If your ingredient has subtypes that depend on Components or capabilities,
 * use this so JEI can tell those subtypes apart.
 */
public interface ISubtypeRegistration {

	/**
	 * Add an interpreter to allow JEI to understand the differences between ingredient subtypes.
	 * This interpreter should account for Components and anything else
	 * that's relevant to differentiating the ingredient's subtypes.
	 *
	 * @param type        the ingredient type (for example {@link VanillaTypes#ITEM_STACK}
	 * @param base        the base of the ingredient that has subtypes (for example, {@link Items#ENCHANTED_BOOK}).
	 *                       All ingredients with this base will use the given interpreter.
	 * @param interpreter the interpreter for the ingredient's subtypes
	 *
	 * @since 19.9.0
	 */
	<B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, ISubtypeInterpreter<I> interpreter);

	/**
	 * Add an interpreter to allow JEI to understand the differences between ingredient subtypes.
	 * This interpreter should account for Components and anything else
	 * that's relevant to differentiating the ingredient's subtypes.
	 *
	 * @param item        the item base of the ItemStack that has subtypes (for example, {@link Items#ENCHANTED_BOOK}).
	 *                       All ItemStacks with this base will use the given interpreter.
	 * @param interpreter the interpreter for the ItemStack's subtypes
	 *
	 * @since 19.9.0
	 */
	default void registerSubtypeInterpreter(Item item, ISubtypeInterpreter<ItemStack> interpreter) {
		registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, interpreter);
	}

	/**
	 * Add an interpreter to allow JEI to understand the differences between ingredient subtypes.
	 * This interpreter should account for Components and anything else
	 * that's relevant to differentiating the ingredient's subtypes.
	 *
	 * @param type        the ingredient type (for example {@link VanillaTypes#ITEM_STACK}
	 * @param base        the base of the ingredient that has subtypes (for example, {@link Items#ENCHANTED_BOOK}).
	 *                       All ingredients with this base will use the given interpreter.
	 * @param interpreter the interpreter for the ingredient's subtypes
	 *
	 * @since 9.7.0
	 *
	 * @deprecated use {@link #registerSubtypeInterpreter(IIngredientTypeWithSubtypes, Object, ISubtypeInterpreter)}
	 */
	@SuppressWarnings("removal")
	@Deprecated(since = "19.9.0", forRemoval = true)
	<B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter<I> interpreter);

	/**
	 * Add an interpreter to allow JEI to understand the differences between ingredient subtypes.
	 * This interpreter should account for Components and anything else
	 * that's relevant to differentiating the ingredient's subtypes.
	 *
	 * @param item        the item base of the ItemStack that has subtypes (for example, {@link Items#ENCHANTED_BOOK}).
	 *                       All ItemStacks with this base will use the given interpreter.
	 * @param interpreter the interpreter for the ItemStack's subtypes
	 *
	 * @since 11.1.1
	 *
	 * @deprecated use {@link #registerSubtypeInterpreter(Item, ISubtypeInterpreter)}
	 */
	@SuppressWarnings("removal")
	@Deprecated(since = "19.9.0", forRemoval = true)
	default void registerSubtypeInterpreter(Item item, violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter<ItemStack> interpreter) {
		registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, interpreter);
	}

}
