package violet.dainty.features.carryon;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import violet.dainty.Dainty;

public record ClientboundStartRidingPacket(int iden, boolean ride) implements PacketBase {

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundStartRidingPacket> CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, ClientboundStartRidingPacket::iden,
		ByteBufCodecs.BOOL, ClientboundStartRidingPacket::ride,
		ClientboundStartRidingPacket::new
	);

	public static final ResourceLocation PACKET_ID_START_RIDING = ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "start_riding");;
	public static final CustomPacketPayload.Type<ClientboundStartRidingPacket> TYPE = new Type<>(PACKET_ID_START_RIDING);

	@Override
	public void handle(Player player) {
		Entity otherPlayer = player.level().getEntity(this.iden);
		if (otherPlayer != null) {
			if (ride) otherPlayer.startRiding(player);
			else otherPlayer.stopRiding();
		}
	}

	@Override
	public Type<ClientboundStartRidingPacket> type() {
		return TYPE;
	}
}