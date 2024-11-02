package violet.dainty.features.bag;

import java.util.List;

import com.google.common.base.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.registries.DaintyDataComponents;

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

		// Shift down - show full information
		if (Screen.hasShiftDown()) {

			// Item description
			tooltipComponents.add(
				Component.literal("Holds up to ")
				.append(Component.literal(Integer.toString(this.tier.capacity() / 64)).withColor(this.tier.color()))
				.append(Component.literal(" stacks of "))
				.append(Component.literal(Integer.toString(this.tier.maxItemVariants())).withColor(this.tier.color()))
				.append(Component.literal(" types of items."))
			);

			// Spacing + "empty" if no items inside
			BagDataComponent bagData = stack.get(DaintyDataComponents.BAG_DATA_COMPONENT);
			tooltipComponents.add(Component.literal(""));
			if (bagData.items().length == 0) {
				tooltipComponents.add(Component.literal("Empty").withStyle(ChatFormatting.GRAY));
			}

			// Show items stored
			for (ItemStack storedItem : bagData.items()) {
				tooltipComponents.add(Component.literal(storedItem.getCount() + "x ").append(((MutableComponent) storedItem.getDisplayName()).withStyle(ChatFormatting.AQUA)));
			}

			// Show items remaining
			tooltipComponents.add(Component.literal(""));
			tooltipComponents.add(
				Component.literal(Integer.toString(bagData.capacityRemaining())).withColor(this.tier.color())
				.append(Component.literal(" items remaining"))
			);

		} 
		
		// Shift not held - show "hold shift for more information"
		else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		}
	}

	@Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        return false;
    }

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
		public int maxItemVariants() {
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
				case NETHERITE -> 0x333344;
			};
		}
	}
}
