package violet.dainty.features.blocktooltips.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.Jade;
import violet.dainty.features.blocktooltips.api.JadeIds;

public record ShowOverlayPacket(boolean show) implements CustomPacketPayload {
	public static final Type<ShowOverlayPacket> TYPE = new Type<>(JadeIds.PACKET_SHOW_OVERLAY);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShowOverlayPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			ShowOverlayPacket::show,
			ShowOverlayPacket::new
	);

	public static void handle(ShowOverlayPacket message, ClientPayloadContext context) {
		Dainty.LOGGER.info("Received request from the server to {} overlay", message.show ? "show" : "hide");
		context.execute(() -> {
			Jade.CONFIG.get().getGeneral().setDisplayTooltip(message.show);
			Jade.CONFIG.save();
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
