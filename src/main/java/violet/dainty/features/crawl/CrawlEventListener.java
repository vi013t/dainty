package violet.dainty.features.crawl;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyKeyBindings;

@EventBusSubscriber(modid = Dainty.MODID)
public class CrawlEventListener {

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().player == null) return;

		if (DaintyKeyBindings.CRAWL.get().isDown()) {
			PacketDistributor.sendToServer(new CrawlPacket(true));	
			Minecraft.getInstance().player.setForcedPose(Pose.SWIMMING);
		} else {
			PacketDistributor.sendToServer(new CrawlPacket(false));	
			Minecraft.getInstance().player.setForcedPose(null);
		}
	}	
}
