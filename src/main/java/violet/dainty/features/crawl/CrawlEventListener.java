package violet.dainty.features.crawl;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.Dainty;
import violet.dainty.config.DaintyConfig;
import violet.dainty.registries.DaintyKeyBindings;

@EventBusSubscriber(modid = Dainty.MODID)
public class CrawlEventListener {

	/**
	 * Handles player crawling. This is fired every tick on the logical client, and checks if the player is holding
	 * {@link violet.dainty.registries.DaintyKeyBindings#CRAWL the crawl keybinding}. If they are, then a
	 * {@link CrawlPacket} is sent to the server so that the player can enter crawl mode, which must happen on both
	 * the logical client <i>and</i> server. This handles the crawling for the logical client, and once the packet
	 * is received on the server, the logic is handled on the logical server in the 
	 * {@link violet.dainty.features.crawl.CrawlPacketHandler CrawlPacketHandler} (in
	 * {@link violet.dainty.features.crawl.CrawlPacketHandler#handle(CrawlPacket, net.neoforged.neoforge.network.handling.IPayloadContext)
	 * the handle method}).
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The client tick event fired by Neoforge.
	 */
	@SubscribeEvent
	@SuppressWarnings({ "resource", "null" })
	public static void crawl(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().player == null || !DaintyConfig.ENABLE_CRAWL_KEYBIND.get()) return;

		// Crawling - enter crawl mode
		if (DaintyKeyBindings.CRAWL.get().isDown()) {
			if (Minecraft.getInstance().player.getForcedPose() != Pose.SWIMMING) {
				PacketDistributor.sendToServer(new CrawlPacket(true));	
				Minecraft.getInstance().player.setForcedPose(Pose.SWIMMING);
			}
		} 
		
		// Not crawling - revert crawl mode
		else if (Minecraft.getInstance().player.getForcedPose() == Pose.SWIMMING) {
			PacketDistributor.sendToServer(new CrawlPacket(false));	
			Minecraft.getInstance().player.setForcedPose(null);
		}
	}	
}
