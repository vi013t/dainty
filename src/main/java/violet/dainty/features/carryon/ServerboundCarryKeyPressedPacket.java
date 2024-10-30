package violet.dainty.features.carryon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import violet.dainty.Dainty;

public record ServerboundCarryKeyPressedPacket(boolean pressed) implements PacketBase {
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCarryKeyPressedPacket> CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, ServerboundCarryKeyPressedPacket::pressed,
		ServerboundCarryKeyPressedPacket::new
	);

	public static final ResourceLocation PACKET_ID_KEY_PRESSED =  ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "key_pressed");
	public static final CustomPacketPayload.Type<ServerboundCarryKeyPressedPacket> TYPE = new Type<>(PACKET_ID_KEY_PRESSED);

	@Override
	public void handle(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		carry.setKeyPressed(this.pressed);
		CarryOnDataManager.setCarryData(player, carry);
	}

	@Override
	public Type<ServerboundCarryKeyPressedPacket> type() {
		return TYPE;
	}
}