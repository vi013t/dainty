package violet.dainty.features.bag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;

public record BagDataComponent(int capacity, int types, ItemStack[] items) {

	/**
	 * Creates a new {@code BagDataComponent} with the given capacity and types, using a list of items instead
	 * of an array. Generally you shouldn't need to use this, and instead you should just use 
	 * {@link BagDataComponent#BagDataComponent(int, int, ItemStack[]) the regular constructor that takes an array}.
	 * This is used by {@link violet.dainty.registries.DaintyDataComponents#BAG_DATA_CODEC the bag data component's
	 * codec}, which can only operate on lists instead of arrays, and needs to be able to construct an instance
	 * of the data component from a list.
	 * 
	 * @param capacity The total item capacity of this bag
	 * @param types The number of different item types this bag can store
	 * 
	 * @returns The newly created data component.
	 */
	public static BagDataComponent fromItemList(int capacity, int types, List<ItemStack> items) {
		return new BagDataComponent(capacity, types, items.toArray(ItemStack[]::new));
	}

	/**
	 * Creates an empty bag data component that has no items.
	 * 
	 * @param tier the tier of the bag
	 */
	public static BagDataComponent empty(Bag.Tier tier) {
		return new BagDataComponent(tier.capacity(), tier.types(), new ItemStack[] {});
	}

	/**
	 * Returns the total number of items (not types) stored in this bag.
	 * 
	 * @return The total number of items stored in this bag.
	 */
	private int total() {
		return Arrays.stream(this.items).mapToInt(itemStack -> itemStack.getCount()).sum();
	}

	/**
	 * Returns the total item capacity remaining for this bag.
	 * 
	 * @return the total item capacity remaining for this bag.
	 */
	public int capacityRemaining() {
		return this.capacity - this.total();
	}

	/**
	 * Returns the number of type slots available for this bag.
	 * 
	 * @return the number of type slots available for this bag.
	 */
	public int typesRemaining() {
		return this.types - this.items.length;
	}

	/**
	 * Returns whether this bag is full on capacity, meaning no more items can be added.
	 * 
	 * @return Whether this bag is full on capacity.
	 */
	public boolean isCapacityFull() {
		return this.capacityRemaining() == 0;
	}

	/**
	 * Returns whether this bag is full on types, meaning no new types of items can be added.
	 * 
	 * @return Whether this bag is full for types.
	 */
	public boolean isTypesFull() {
		return this.typesRemaining() == 0;
	}

	/**
	 * Returns whether this bag is currently storing the given item.
	 * 
	 * @param item The item to check if this bag is storing
	 * 
	 * @return Whether this bag is storing the given item
	 */
	public boolean hasItem(Item item) {
		return Arrays.stream(this.items).map(itemStack -> itemStack.getItem()).toList().contains(item);
	}

	/**
	 * Returns whether the given item can be added to this bag. This is only true if the bag is under capacity, and either the
	 * item is already in the bag or the bag has a spare type slot. Also, the item stack cannot have any data components
	 * attached to it. This prevents things like other bags, items with NBT, etc. from being added.
	 * 
	 * @param helmet The item to check if it can be added to this bag
	 * 
	 * @return Whether the given item can be added to this bag
	 */
	public boolean canAdd(ItemStack itemStack) {
		if (this.isCapacityFull()) return false;
		if (this.isTypesFull() && !this.hasItem(itemStack.getItem())) return false;
		if (!itemStack.isComponentsPatchEmpty()) return false;
		return true;
	}

	/**
	 * Returns whether this bag is completely empty and has no items inside.
	 * 
	 * @return whether this bag is empty.
	 */
	public boolean isEmpty() {
		return this.items.length == 0;
	}

	/**
	 * Returns an unmodifiable list referencing the items in this bag. Generally you shouldn't need this and should simply reference
	 * {@link #items()} instead; This is only used for {@link violet.dainty.registries.DaintyDataComponents#BAG_DATA_CODEC the bag
	 * data component's codec}, which can't serialize arrays, but <i>can</i> serialize lists, so it needs a getter that returns a
	 * list of item stacks in this bag.
	 */
	public List<ItemStack> itemList() {
		return List.of(this.items);
	}

	/**
	 * Attempt to remove a full stack of items from this bag. As many items as possible will be removed from the first item in this
	 * bag, up to at most 64.
	 * 
	 * <br/><br/>
	 * 
	 * As with all methods on data components in Dainty, this does not modify this data component, and returns a new instance of the
	 * data component to be applied to the item stack that previously held this data component. This is because data components must
	 * be effectively immutable, as stated by <a href="https://docs.neoforged.net/docs/items/datacomponents/#creating-custom-data-components">
	 * the corresponding section of the Neoforge documentation</a>.
	 * 
	 * @return The new bag data to be applied to the bag item stack, and the stack of items removed.
	 */
	public ImmutablePair<BagDataComponent, ItemStack> removeStack() {
		if (this.isEmpty()) return ImmutablePair.of(this, ItemStack.EMPTY);
		List<ItemStack> items = new ArrayList<>(List.of(this.items.clone()));

		// Get the item and amount to take
		int amountToTake = Math.min(items.get(0).getCount(), 64);
		Item item = items.get(0).getItem();

		// Take the items out
		if (items.get(0).getCount() <= 64) items.remove(0);
		else items.get(0).setCount(items.get(0).getCount() - amountToTake);

		// Return the pair
		return ImmutablePair.of(new BagDataComponent(this.capacity, this.types, items.toArray(ItemStack[]::new)), new ItemStack(item, amountToTake));
	}

	/**
	 * Attempts to add the given item stack to this bag. If this bag is not holding the item provided in the given stack, no items
	 * will be added. If this bag is at max capacity, not items will be added. Otherwise, as many items possible will be added to
	 * this bag without going over capacity.
	 * 
	 * <br/><br/>
	 * 
	 * This is used when the player picks up items; Items picked up should automatically go into the player's bag if and only if
	 * the bag is already holding that type of item. This logic is handled in 
	 * {@link violet.dainty.features.bag.BagEventHandler#onItemPickup(net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent.Pre)
	 * the corresponding part of the bag event handler.}
	 * 
	 * <br/><br/>
	 * 
	 * If you'd like to try to add an item regardless of whether it's already stored in this bag, use {@link #addItemMaybeInBag(ItemStack)}
	 * 
	 * <br/><br/>
	 * 
	 * As with all methods on data components in Dainty, this does not modify this data component, and returns a new instance of the
	 * data component to be applied to the item stack that previously held this data component. This is because data components must
	 * be effectively immutable, as stated by <a href="https://docs.neoforged.net/docs/items/datacomponents/#creating-custom-data-components">
	 * the corresponding section of the Neoforge documentation</a>.
	 * 
	 * @param itemStack The item stack to attempt to add the entirety of to this bag.
	 * 
	 * @return The new bag data to apply to the item stack, and the number of items that were added to the bag.
	 */
	public ImmutablePair<BagDataComponent, Integer> addItemAlreadyInBag(ItemStack itemStack) {
		if (!this.canAdd(itemStack)) return ImmutablePair.of(this, 0);

		// Get index to add the item to
		List<ItemStack> items = new ArrayList<>(List.of(this.items.clone()));
		Optional<Integer> itemIndex = Optional.empty();
		Optional<ItemStack> itemEntry = Optional.empty();
		for (int index = 0; index < items.size(); index++) {
			if (items.get(index).getItem() == itemStack.getItem()) {
				itemIndex = Optional.of(index);
				itemEntry = Optional.of(items.get(index));
				break;
			}
		}

		// Item in bag - proceed
		if (itemIndex.isPresent()) {
			int spaceRemaining = this.capacityRemaining();
			int amountToAdd = Math.min(itemStack.getCount(), spaceRemaining);
			items.set(itemIndex.get(), new ItemStack(itemEntry.get().getItem(), itemEntry.get().getCount() + amountToAdd));
			return ImmutablePair.of(new BagDataComponent(this.capacity, this.types, items.toArray(ItemStack[]::new)), amountToAdd);
		}

		// Item not in bag - do nothing
		return ImmutablePair.of(this, 0);
	}

	/**
	 * Attempts to add the given item stack to this bag. Even if the item stored in the item stack is not stored in this bag, it
	 * will attempt to be stored.
	 * 
	 * <br/><br/>
	 * 
	 * This is used when the player clicks an item into the bag manually in their inventory. This logic is handled in
	 * {@link violet.dainty.features.bag.Bag#overrideOtherStackedOnMe(ItemStack, ItemStack, net.minecraft.world.inventory.Slot, net.minecraft.world.inventory.ClickAction, net.minecraft.world.entity.player.Player, net.minecraft.world.entity.SlotAccess)
	 * the corresponding part of the bag item class}.
	 * 
	 * <br/><br/>
	 * 
	 * If you'd like to try to add an item only if it's already stored in this bag, use {@link #addItemAlreadyInBag(ItemStack)}
	 * 
	 * <br/><br/>
	 * 
	 * As with all methods on data components in Dainty, this does not modify this data component, and returns a new instance of the
	 * data component to be applied to the item stack that previously held this data component. This is because data components must
	 * be effectively immutable, as stated by <a href="https://docs.neoforged.net/docs/items/datacomponents/#creating-custom-data-components">
	 * the corresponding section of the Neoforge documentation</a>.
	 * 
	 * @param itemStack The item stack to attempt to add to this bag.
	 * 
	 * @return The new bag data to apply to the item stack, and the number of items that were added to the bag.
	 */
	public ImmutablePair<BagDataComponent, Integer> addItemMaybeInBag(ItemStack itemStack) {
		if (!this.canAdd(itemStack)) return ImmutablePair.of(this, 0);

		List<ItemStack> items = new ArrayList<>(Arrays.asList(this.items.clone()));
		Optional<Integer> itemIndex = Optional.empty();
		Optional<ItemStack> itemEntry = Optional.empty();

		int amountToAdd = Math.min(itemStack.getCount(), this.capacityRemaining());

		for (int index = 0; index < items.size(); index++) {
			if (items.get(index).getItem() == itemStack.getItem()) {
				itemIndex = Optional.of(index);
				itemEntry = Optional.of(items.get(index));
				break;
			}
		}

		if (itemIndex.isPresent()) items.set(itemIndex.get(), new ItemStack(itemEntry.get().getItem(), itemEntry.get().getCount() + amountToAdd));
		else items.add(new ItemStack(itemStack.getItem(), amountToAdd));

		return ImmutablePair.of(new BagDataComponent(this.capacity, this.types, items.toArray(ItemStack[]::new)), amountToAdd);
	}

	/**
	 * Returns the tooltip image for this bag data. This is done by converting the data in this bag into
	 * vanilla {@link BundleContents bundle data}, and then generating a {@link BundleTooltip bundle tooltip}
	 * from that. The tooltip is then used and rendered on the item in {@link Bag#getTooltipImage(ItemStack)}.
	 * 
	 * @return A tooltip to show the contents of this bag.
	 */
	public TooltipComponent tooltip() {
		return new BundleTooltip(new BundleContents(this.itemList()));
	}

}