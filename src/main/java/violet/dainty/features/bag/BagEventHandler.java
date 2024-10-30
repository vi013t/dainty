package violet.dainty.features.bag;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import violet.dainty.Dainty;
import violet.dainty.data.DaintyDataComponents;

@EventBusSubscriber(modid = Dainty.MODID)
public class BagEventHandler {
	
	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void onItemPickup(final ItemEntityPickupEvent.Pre event) {
		if (event.getPlayer().level().isClientSide) return;
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
			ImmutablePair<BagDataComponent, Integer> result = bagData.addAsManyAsPossible(itemStack);
			BagDataComponent newBagData = result.getLeft();
			int amountConsumed = result.getRight();
			if (amountConsumed > 0) {
				bagItemStack.set(DaintyDataComponents.BAG_DATA_COMPONENT, newBagData);
				if (amountConsumed < bagItemStack.getCount()) {
					itemStack.setCount(itemStack.getCount() - amountConsumed);
				} else {
					event.setCanPickup(TriState.FALSE);
					event.getItemEntity().kill();
					event.getPlayer().level().playSound(event.getPlayer(), event.getPlayer().blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
					break;
				}
			}
		}
	}
}