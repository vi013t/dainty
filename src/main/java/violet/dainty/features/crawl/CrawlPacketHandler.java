package violet.dainty.features.crawl;

import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class CrawlPacketHandler implements IPayloadHandler<CrawlPacket> {

	@Override
	public void handle(CrawlPacket payload, IPayloadContext context) {
		context.player().setForcedPose(payload.crawl() ?  Pose.SWIMMING : null);
	}
	
}
