package violet.dainty.features.inventorysorting;

import java.util.Set;
import java.util.TreeSet;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import violet.dainty.Dainty;

public class SortPacketHandler implements IPayloadHandler<SortPacket> {

	@Override
	public void handle(SortPacket payload, IPayloadContext context) {

		// Get container
		Container container = null;
		int startSlotIndex = 0;
		if (payload.isPlayer()) {
			container = context.player().getInventory();
			startSlotIndex = 9;
		}
		else {
			BlockEntity blockEntity = context.player().level().getBlockEntity(payload.position());
			if (blockEntity instanceof Container blockContainer) container = blockContainer;
			if (context.player().containerMenu instanceof ChestMenu chestMenu) {
				if (chestMenu.getContainer() instanceof CompoundContainer compound) {
					container = compound;
				}
			}
		}
		if (container == null) return;

		// Get items
		Set<ItemStack> sortedItems = new TreeSet<>(payload.ordering().comparator());
		for (int slotIndex = startSlotIndex; slotIndex < container.getContainerSize(); slotIndex++) {
			try { 
				ItemStack stack = container.getItem(slotIndex); 
				if (!stack.isEmpty()) {
					boolean wasAdded = false;
					for (ItemStack alreadySortedStack : sortedItems) {
						if (ItemStack.isSameItemSameComponents(stack, alreadySortedStack)) {
							alreadySortedStack.setCount(alreadySortedStack.getCount() + stack.getCount());
							wasAdded = true;
							break;
						}
					}

					if (!wasAdded) {
						sortedItems.add(stack);
					}
				}
			}
			catch (Exception e) {
				Dainty.LOGGER.warn("Exception caught while sorting inventory:");
				e.printStackTrace();
			}
		}

		// Empty container
		for (int slotIndex = startSlotIndex; slotIndex < container.getContainerSize(); slotIndex++) {
			container.removeItemNoUpdate(slotIndex);
		}

		// Set items
		int slot = startSlotIndex;
		for (var itemStack : sortedItems) {
			int itemsLeft = itemStack.getCount();
			while (itemsLeft > 0) {
				int amountToAdd = Math.min(itemsLeft, itemStack.getMaxStackSize());
				ItemStack copy = itemStack.copy();
				copy.setCount(amountToAdd);
				container.setItem(slot, copy);
				itemsLeft -= amountToAdd;
				slot++;
			}
		}
	}
}
	