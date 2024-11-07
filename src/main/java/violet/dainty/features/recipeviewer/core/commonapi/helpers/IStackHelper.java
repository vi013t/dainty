package violet.dainty.features.recipeviewer.core.commonapi.helpers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeManager;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

/**
 * Helps get ItemStacks from common formats used in recipes.
 * Get the instance from {@link IJeiHelpers#getStackHelper()}.
 */
public interface IStackHelper {
	/**
	 * Gets the unique identifier for a stack, ignoring NBT on items without subtypes, and uses the {@link ISubtypeManager}.
	 * If two unique identifiers are equal, then the items can be considered equivalent.
	 *
	 * @since 19.9.0
	 */
	Object getUidForStack(ItemStack stack, UidContext context);

	/**
	 * Gets the unique identifier for a stack, ignoring NBT on items without subtypes, and uses the {@link ISubtypeManager}.
	 * If two unique identifiers are equal, then the items can be considered equivalent.
	 *
	 * @since 19.19.4
	 */
	Object getUidForStack(ITypedIngredient<ItemStack> stack, UidContext context);

	/**
	 * Similar to ItemStack.areItemStacksEqual but ignores NBT on items without subtypes, and uses the {@link ISubtypeManager}
	 * @since 7.3.0
	 */
	boolean isEquivalent(@Nullable ItemStack lhs, @Nullable ItemStack rhs, UidContext context);

	/**
	 * Gets the unique identifier for a stack, ignoring NBT on items without subtypes, and uses the {@link ISubtypeManager}.
	 * If two unique identifiers are equal, then the items can be considered equivalent.
	 * @since 7.6.1
	 *
	 * @deprecated use {@link #getUidForStack(ItemStack, UidContext)}
	 */
	@Deprecated(since = "19.9.0", forRemoval = true)
	String getUniqueIdentifierForStack(ItemStack stack, UidContext context);
}
