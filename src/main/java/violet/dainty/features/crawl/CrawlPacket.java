package violet.dainty.features.crawl;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;

public record CrawlPacket(boolean crawl) implements CustomPacketPayload {

	public static final Type<CrawlPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "crawl"));

	public static final StreamCodec<ByteBuf, CrawlPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, CrawlPacket::crawl,
		CrawlPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
