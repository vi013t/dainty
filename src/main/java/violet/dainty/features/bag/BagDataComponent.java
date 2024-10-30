package violet.dainty.features.bag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record BagDataComponent(int capacity, ItemStack[] items) {

	public static BagDataComponent fromItemList(int capacity, List<ItemStack> items) {
		return new BagDataComponent(capacity, items.toArray(ItemStack[]::new));
	}

	public static BagDataComponent empty(int capacity) {
		return new BagDataComponent(capacity, new ItemStack[] {});
	}

	public BagDataComponent remove(int index, int amount) {
		ItemStack[] items = this.items.clone();
		items[index] = new ItemStack(items[index].getItem(), items[index].getCount() - amount);
		ItemStack[] itemsLeft = Arrays.stream(items).filter(entry -> entry.getCount() > 0).toArray(ItemStack[]::new);
		return new BagDataComponent(this.capacity, itemsLeft);
	}

	public ImmutablePair<BagDataComponent, Integer> addAsManyAsPossible(ItemStack itemStack) {
		Optional<ItemStack> storedStack = Arrays.stream(this.items).filter(bagStack -> bagStack.getItem() == itemStack.getItem()).findFirst();

		// Only catch items we are already storing
		if (!storedStack.isPresent()) return ImmutablePair.of(this, 0);

		int spaceRemaining = this.capacity - this.total();
		int amountToAdd = Math.min(itemStack.getCount(), spaceRemaining);
		return ImmutablePair.of(this.add(itemStack.getItem(), amountToAdd), amountToAdd);
	}

	public int total() {
		return Arrays.stream(this.items).mapToInt(itemStack -> itemStack.getCount()).sum();
	}

	public int capacityRemaining() {
		return this.capacity - this.total();
	}

	private BagDataComponent add(Item item, int amount) {
		List<ItemStack> items = new ArrayList<>(Arrays.asList(this.items.clone()));
		Optional<Integer> itemIndex = Optional.empty();
		Optional<ItemStack> itemEntry = Optional.empty();

		for (int index = 0; index < items.size(); index++) {
			if (items.get(index).getItem() == item) {
				itemIndex = Optional.of(index);
				itemEntry = Optional.of(items.get(index));
				break;
			}
		}

		if (itemIndex.isPresent()) items.set(itemIndex.get(), new ItemStack(itemEntry.get().getItem(), itemEntry.get().getCount() + amount));
		else items.add(new ItemStack(item, amount));

		return new BagDataComponent(this.capacity, items.toArray(ItemStack[]::new));
	}

	public BagDataComponent addStack(Item item) {
		return this.add(item, 64);
	}

	public BagDataComponent addOne(Item item) {
		return this.add(item, 1);
	}

	public List<ItemStack> itemList() {
		return List.of(this.items);
	}
}