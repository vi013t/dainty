package violet.dainty.features.bag;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.registries.DaintyDataComponents;

/**
 * Class for creating {@link violet.dainty.registries.DaintyItems#BAG bag items}. Bags are basically inverse bundles;
 * They can only hold a very small amount of different types of items, but can hold mass quantities of those items.
 */
public class Bag extends Item {

	/** 
	 * The {@link Bag.Tier tier} of this bag item. 
	 */
	private final Tier tier;

	/**
	 * Creates a new bag of the specified tier. This is private because the item should only ever be instantiated through
	 * a supplier; Use {@link #create(Tier)} to access it publicly.
	 * 
	 * @param tier The tier of the bag to create
	 */
	private Bag(Bag.Tier tier) {
		super(new Item.Properties().component(DaintyDataComponents.BAG_DATA_COMPONENT, BagDataComponent.empty(tier)).stacksTo(1));
		this.tier = tier;
	}

	/**
	 * Creates a new bag supplier that can be registered in an item registry.
	 * 
	 * @param tier The tier of the bag to create.
	 * 
	 * @return The created bag as a supplier.
	 */
	public static Supplier<Bag> create(Tier tier) {
		return () -> new Bag(tier);
	}

	/**
	 * Returns a custom tooltip image for this item. For bags, this shows the items inside the bag
	 * using the vanilla bundle tooltip rendering; See {@link BagDataComponent#tooltip()} for more
	 * information. This code uses {@link net.minecraft.world.item.BundleItem#getTooltipImage(ItemStack)
	 * the equivalent method in the vanilla bundle class} as a reference.
	 * 
	 * @param stack The item stack containing the bag item.
	 * 
	 * @return The tooltip image of the bag's contents.
	 */
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (stack.has(DataComponents.HIDE_TOOLTIP) || stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) return Optional.empty();
		BagDataComponent bagData = stack.get(DaintyDataComponents.BAG_DATA_COMPONENT);
		if (bagData.isEmpty()) return Optional.empty();
        return Optional.of(bagData.tooltip());
    }

	/**
	 * Renders the tooltip when hovering over this item in an inventory. Overridden to add special bag information to the tooltip,
	 * such as the space remaining and the items currently stored.
	 * 
	 * @param stack The item stack being hovered on
	 * @param context The tooltip context for this tooltip
	 * @param tooltipComponents the components of the tooltip
	 * @param tooltipFlag the tooltip flag for this tooltip render
	 *
	 */
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

		// Description
		tooltipComponents.add(
			Component.literal(this.tier.toString()).withStyle(Style.EMPTY.withColor(this.tier.color()).withBold(true))
			.append(Component.literal(" tier: Holds ").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withBold(false)))
			.append(Component.literal(Integer.toString(tier.capacity())).withStyle(Style.EMPTY.withColor(this.tier.color()).withBold(true)))
			.append(Component.literal(" items of ").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withBold(false)))
			.append(Component.literal(Integer.toString(tier.types())).withStyle(Style.EMPTY.withColor(this.tier.color()).withBold(true)))
			.append(Component.literal(" types").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withBold(false)))
		);

		// Shift down - show full information
		if (Screen.hasShiftDown()) {
			BagDataComponent bagData = stack.get(DaintyDataComponents.BAG_DATA_COMPONENT);
			float stacks = (float) bagData.capacityRemaining() / 64f;
			tooltipComponents.add(
				Component.literal(Integer.toString(bagData.capacityRemaining())).withColor(this.tier.color())
				.append(Component.literal(" items remaining (").withStyle(ChatFormatting.GRAY))
				.append(Component.literal(stacks == Math.floor(stacks) ? Integer.toString((int) stacks) : new DecimalFormat("##.0").format(stacks)).withColor(tier.color()))
				.append(Component.literal(" stacks)").withStyle(ChatFormatting.GRAY))
			);
		} 
		
		// Shift not held - show "hold shift for more information"
		else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		}
	}

	/**
	 * Called when the player clicks on a bag item in their inventory, while (possibly) holding another item on their cursor.
	 * This is used to insert items into the bag, as well as take items out in the case that the cursor isn't holding
	 * any items.
	 * 
	 * <br/><br/>
	 * 
	 * For another example implementation of this method, see 
	 * {@link net.minecraft.world.item.BundleItem#overrideOtherStackedOnMe(ItemStack, ItemStack, Slot, ClickAction, Player, SlotAccess)
	 * the vanilla bundle implementation}.
	 * 
	 * @param bag The bag item stack being clicked on
	 * @param carried The item stack being carried by the cursor when clicking on the bag; This might be empty.
	 * @param slot The inventory slot the bag is in
	 * @param action The click action, which provides information about whether this was a left or right click
	 * @param player The player clicking on the bag
	 * @param access The slot access information.
	 * 
	 * @return Whether to cancel the item swap; i.e., if {@code false}, A standard item swap will occur where the item on
	 * the player's cursor is placed in the inventory slot the player is clicking on, and the item in the player's inventory
	 * will be picked up by their cursor.
	 */
	@Override
	@SuppressWarnings("resource")
    public boolean overrideOtherStackedOnMe(ItemStack bag, ItemStack carried, Slot slot, ClickAction action, Player player, SlotAccess access) {
		BagDataComponent bagData = bag.get(DaintyDataComponents.BAG_DATA_COMPONENT);	

		// Adding item to bag
		if (!carried.isEmpty() && carried.getItem() != Items.AIR) {
			var result = bagData.addItemMaybeInBag(carried);
			BagDataComponent newBagData = result.getLeft();
			int amountAdded = result.getRight();

			if (amountAdded > 0) {
				bag.set(DaintyDataComponents.BAG_DATA_COMPONENT, newBagData);
				carried.setCount(carried.getCount() - amountAdded);
				if (player.level().isClientSide) player.playSound(SoundEvents.BUNDLE_INSERT, 1, 1);
				return true;
			}
		}

		// Taking item from bag
		else if (action == ClickAction.SECONDARY) {
			var result = bagData.removeStack();
			BagDataComponent newBagData = result.getLeft();
			ItemStack stackTaken = result.getRight();
			if (!stackTaken.isEmpty()) {
				bag.set(DaintyDataComponents.BAG_DATA_COMPONENT, newBagData);
				access.set(stackTaken);
				if (player.level().isClientSide) player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 1, 1);
				return true;
			}
		}

		return false;
    }

	/**
	 * A bag tier. Bags can have upgraded tiers, like tools or armor. Higher tiers can hold more items and item types.
	 */
	public static enum Tier {

		/**
		 * The basic starting tier for bags. This tier can hold 8 stacks of 1 type of items.
		 */
		BASIC,

		/**
		 * The iron tier for bags. This tier can hold 16 stacks of 1 type of items.
		 */
		IRON,

		/**
		 * The gold tier for bags. This tier can hold 32 stacks of 2 type of items.
		 */
		GOLD,

		/**
		 * The diamond tier for bags. This tier can hold 64 stacks of 3 type of items.
		 */
		DIAMOND,

		/**
		 * The netherite tier for bags. This tier can hold 128 stacks of 4 type of items.
		 */
		NETHERITE;

		/**
		 * The total number of items that can be held by a bag of this tier.
		 * 
		 * @return The total number of items that can be held by a bag of this tier.
		 */
		public int capacity() {
			return 64 * (int) Math.pow(2, this.ordinal() + 3);
		}

		/**
		 * The number of different item types that can be held by a bag of this tier.
		 * 
		 * @return The number of different item types that can be held by a bag of this tier.
		 */
		public int types() {
			return switch(this) {
				case BASIC -> 1;
				case IRON -> 1;
				case GOLD -> 1;
				case DIAMOND -> 2;
				case NETHERITE -> 3;
			};
		}

		/**
		 * The "color" of a bag of this tier; Used when creating the tooltip ({@link Bag#appendHoverText(ItemStack, TooltipContext, List, TooltipFlag)}) to
		 * render certain words in color.
		 * 
		 * @return the color of a bag of this tier.
		 */
		public int color() {
			return switch(this) {
				case BASIC -> 0xBB8877;
				case IRON -> 0xDDDDDD;
				case GOLD -> 0xFFFF99;
				case DIAMOND -> 0x99FFFF;
				case NETHERITE -> 0x554444;
			};
		}

		@Override
		public String toString() {
			return this.name().charAt(0) + this.name().substring(1).toLowerCase();
		}
	}
}
