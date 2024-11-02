package violet.dainty.features.bag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record BagDataComponent(int capacity, int types, ItemStack[] items) {

	public static BagDataComponent fromItemList(int capacity, int types, List<ItemStack> items) {
		return new BagDataComponent(capacity, types, items.toArray(ItemStack[]::new));
	}

	/**
	 * Creates an empty bag data component that has no items.
	 * 
	 * @param tier the tier of the bag
	 */
	public static BagDataComponent empty(Bag.Tier tier) {
		return new BagDataComponent(tier.capacity(), tier.maxItemVariants(), new ItemStack[] {});
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
	 * item is already in the bag or the bag has a spare type slot.
	 * 
	 * @param item The item to check if it can be added to this bag
	 * 
	 * @return Whether the given item can be added to this bag
	 */
	public boolean canAdd(Item item) {
		if (this.isCapacityFull()) return false;
		if (this.isTypesFull() && !this.hasItem(item)) return false;
		return true;
	}

	public List<ItemStack> itemList() {
		return List.of(this.items);
	}

	/**
	 * Attempt to remove a full stack of items from this bag. As many items as possible will be removed from the first item in this
	 * bag, up to at most 64.
	 * 
	 * @return The new bag data to be applied to the bag item stack, and the stack of items removed.
	 */
	public ImmutablePair<BagDataComponent, ItemStack> removeStack() {
		if (this.items.length == 0) return ImmutablePair.of(this, ItemStack.EMPTY);
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
	 * @param itemStack The item stack to attempt to add the entirety of to this bag.
	 * 
	 * @return The new bag data to apply to the item stack, and the number of items that were added to the bag.
	 */
	public ImmutablePair<BagDataComponent, Integer> addItemAlreadyInBag(ItemStack itemStack) {
		if (this.canAdd(itemStack.getItem())) return ImmutablePair.of(this, 0);

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
	 * @param itemStack The item stack to attempt to add to this bag.
	 * 
	 * @return The new bag data to apply to the item stack, and the number of items that were added to the bag.
	 */
	public ImmutablePair<BagDataComponent, Integer> addItemMaybeInBag(ItemStack itemStack) {
		if (this.canAdd(itemStack.getItem())) return ImmutablePair.of(this, 0);

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

}