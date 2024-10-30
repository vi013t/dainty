package violet.dainty.features.carryon;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import violet.dainty.Dainty;

public record ClientboundSyncScriptsPacket(Tag serialized) implements PacketBase {
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncScriptsPacket> CODEC = StreamCodec.composite(
		ByteBufCodecs.TAG, ClientboundSyncScriptsPacket::serialized,
		ClientboundSyncScriptsPacket::new
	);

	public static final ResourceLocation PACKET_ID_SYNC_SCRIPTS =  ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "sync_scripts");
	public static final CustomPacketPayload.Type<ClientboundSyncScriptsPacket> TYPE = new Type<>(PACKET_ID_SYNC_SCRIPTS);

	@Override
	public void handle(Player player) {
		DataResult<List<CarryOnScript>> res = Codec.list(CarryOnScript.CODEC).parse(NbtOps.INSTANCE, serialized);
		List<CarryOnScript> scripts = res.getOrThrow((s) -> {throw new RuntimeException("Failed deserializing carry on scripts on the client: " + s);});
		ScriptManager.SCRIPTS.clear();
		ScriptManager.SCRIPTS.addAll(scripts);
	}

	@Override
	public Type<ClientboundSyncScriptsPacket> type() {
		return TYPE;
	}
}