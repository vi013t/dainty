package violet.dainty.features.blocktooltips.network;

import java.util.List;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.addon.harvest.HarvestToolProvider;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.impl.ObjectDataCenter;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.impl.config.PluginConfig;
import violet.dainty.features.blocktooltips.util.JsonConfig;

public record ServerPingPacket(
		String serverConfig,
		List<Block> shearableBlocks,
		List<ResourceLocation> blockProviderIds,
		List<ResourceLocation> entityProviderIds) implements CustomPacketPayload {
	public static final Type<ServerPingPacket> TYPE = new Type<>(JadeIds.PACKET_SERVER_PING);
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerPingPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			ServerPingPacket::serverConfig,
			ByteBufCodecs.registry(Registries.BLOCK).apply(ByteBufCodecs.list()),
			ServerPingPacket::shearableBlocks,
			ByteBufCodecs.<ByteBuf, ResourceLocation>list().apply(ResourceLocation.STREAM_CODEC),
			ServerPingPacket::blockProviderIds,
			ByteBufCodecs.<ByteBuf, ResourceLocation>list().apply(ResourceLocation.STREAM_CODEC),
			ServerPingPacket::entityProviderIds,
			ServerPingPacket::new);

	public static void handle(ServerPingPacket message, ClientPayloadContext context) {
		String s = message.serverConfig;
		JsonObject json;
		try {
			json = s.isEmpty() ? null : JsonConfig.GSON.fromJson(s, JsonObject.class);
		} catch (Throwable e) {
			Dainty.LOGGER.error("Received malformed config from the server: {}", s);
			return;
		}
		context.execute(() -> {
			ObjectDataCenter.serverConnected = true;
			PluginConfig.INSTANCE.reload(); // clear the server config last time we applied
			if (json != null && !json.keySet().isEmpty()) {
				PluginConfig.INSTANCE.applyServerConfigs(json);
			}
			HarvestToolProvider.INSTANCE.setShearableBlocks(message.shearableBlocks);
			WailaCommonRegistration.instance().blockDataProviders.remapIds(message.blockProviderIds);
			WailaCommonRegistration.instance().entityDataProviders.remapIds(message.entityProviderIds);
			Dainty.LOGGER.info("Received config from the server: {}", message.serverConfig);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
