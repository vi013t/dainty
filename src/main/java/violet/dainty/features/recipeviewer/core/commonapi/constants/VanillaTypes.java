package violet.dainty.features.recipeviewer.core.commonapi.constants;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;

/**
 * Built-in {@link IIngredientType} for vanilla Minecraft.
 */
public final class VanillaTypes {
	/**
	 * @since 9.7.0
	 */
	public static final IIngredientTypeWithSubtypes<Item, ItemStack> ITEM_STACK = new IIngredientTypeWithSubtypes<>() {
		@Override
		public String getUid() {
			return "item_stack";
		}

		@Override
		public Class<? extends ItemStack> getIngredientClass() {
			return ItemStack.class;
		}

		@Override
		public Class<? extends Item> getIngredientBaseClass() {
			return Item.class;
		}

		@Override
		public Item getBase(ItemStack ingredient) {
			return ingredient.getItem();
		}

		@Override
		public ItemStack getDefaultIngredient(Item base) {
			return base.getDefaultInstance();
		}
	};

	private VanillaTypes() {

	}
}
