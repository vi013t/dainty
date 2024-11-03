package violet.dainty.features.blocktooltips.network;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.impl.EntityAccessorImpl;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;

public record RequestEntityPacket(
		EntityAccessorImpl.SyncData data,
		List<@Nullable IServerDataProvider<EntityAccessor>> dataProviders) implements CustomPacketPayload {
	public static final Type<RequestEntityPacket> TYPE = new Type<>(JadeIds.PACKET_REQUEST_ENTITY);
	public static final StreamCodec<RegistryFriendlyByteBuf, RequestEntityPacket> CODEC = StreamCodec.composite(
			EntityAccessorImpl.SyncData.STREAM_CODEC,
			RequestEntityPacket::data,
			ByteBufCodecs.<ByteBuf, IServerDataProvider<EntityAccessor>>list()
					.apply(ByteBufCodecs.idMapper(
							$ -> Objects.requireNonNull(WailaCommonRegistration.instance().entityDataProviders.idMapper()).byId($),
							$ -> Objects.requireNonNull(WailaCommonRegistration.instance().entityDataProviders.idMapper())
									.getIdOrThrow($))),
			RequestEntityPacket::dataProviders,
			RequestEntityPacket::new);

	public static void handle(RequestEntityPacket message, ServerPayloadContext context) {
		EntityAccessorImpl.handleRequest(message, context, tag -> ReceiveDataPacket.send(tag, context));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
