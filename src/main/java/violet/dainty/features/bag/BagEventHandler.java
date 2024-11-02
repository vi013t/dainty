package violet.dainty.features.bag;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyDataComponents;

@EventBusSubscriber(modid = Dainty.MODID)
public class BagEventHandler {
	
	@SuppressWarnings({ "null", "resource" })
	@SubscribeEvent
	public static void onItemPickup(final ItemEntityPickupEvent.Pre event) {
		if (event.getItemEntity().tickCount < 40) return;

		final List<ItemStack> bags = new ArrayList<>();
		for (ItemStack itemStack : event.getPlayer().getInventory().items) {
			if (itemStack.getItem() instanceof Bag) {
				bags.add(itemStack);
			}
		}

		ItemStack itemStack = event.getItemEntity().getItem();

		for (ItemStack bagItemStack : bags) {
			BagDataComponent bagData = bagItemStack.get(DaintyDataComponents.BAG_DATA_COMPONENT);
			ImmutablePair<BagDataComponent, Integer> result = bagData.addItemAlreadyInBag(itemStack);
			BagDataComponent newBagData = result.getLeft();
			int amountConsumed = result.getRight();
			if (amountConsumed > 0) {

				// Update item stack data
				bagItemStack.set(DaintyDataComponents.BAG_DATA_COMPONENT, newBagData);

				// Item stack not fully consumed
				if (amountConsumed < bagItemStack.getCount()) {
					itemStack.setCount(itemStack.getCount() - amountConsumed);
				} 

				// Item stack fully consumed
				else {
					event.setCanPickup(TriState.FALSE);
					event.getItemEntity().kill();
					if (Minecraft.getInstance().player != null) {
						Minecraft.getInstance().player.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
					}
					break;
				}
			}
			
		}
	}
}