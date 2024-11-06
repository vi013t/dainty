package violet.dainty.features.crawl;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;

/**
 * A "crawl packet". This is a custom packet type that is sent to the logical server to indicate that the player should
 * start or stop crawling. The logic for that is handled in 
 * {@link violet.dainty.features.crawl.CrawlEventListener#crawl(net.neoforged.neoforge.client.event.ClientTickEvent)
 * the corresponding part of the crawl event handler}, and the logical server receives the packet and handles it
 * in {@link violet.dainty.features.crawl.CrawlPacketHandler the crawl packet handler} in
 * {@link violet.dainty.features.crawl.CrawlPacketHandler#handle(CrawlPacket, IPayloadContext) its handle method}.
 * 
 * @param crawl Whether the player should be crawling or not.
 */
public record CrawlPacket(boolean crawl) implements CustomPacketPayload {

	/**
	 * The {@link Type} of this crawl packet. This is required by {@link CustomPacketPayload the base packet class}
	 * because of {@link #type()}.
	 */
	public static final Type<CrawlPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "crawl"));

	/**
	 * The {@link StreamCodec} for this {@link CrawlPacket}. This is the codec that serializes and deserializes crawl packets
	 * in order to transfer them between the logical client and server. In 
	 * {@link violet.dainty.features.crawl.CrawlEventListener#crawl(net.neoforged.neoforge.client.event.ClientTickEvent)
	 * the appropriate part of the crawl event handler}, crawl packets are sent from the logical server to the logical client,
	 * (that is the point of packets...), so naturally this needs a way to be written to and read from streams, hence this
	 * stream codec.
	 */
	public static final StreamCodec<ByteBuf, CrawlPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, CrawlPacket::crawl,
		CrawlPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
