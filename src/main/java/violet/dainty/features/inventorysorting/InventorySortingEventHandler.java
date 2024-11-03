package violet.dainty.features.inventorysorting;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyDataAttachments;

@EventBusSubscriber(modid = Dainty.MODID)
public class InventorySortingEventHandler {
	
	@SubscribeEvent
	@SuppressWarnings({ "null", "resource" })
	public static void openContainer(PlayerInteractEvent.RightClickBlock event) {
		// This data is only used on the GUI screen which is client-side, so we only need to set this on the client player
		Minecraft.getInstance().player.setData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE, new SortPosition(event.getPos()));
	}
}
