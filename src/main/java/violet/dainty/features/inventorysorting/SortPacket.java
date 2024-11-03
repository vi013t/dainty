package violet.dainty.features.inventorysorting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;

public record SortPacket(BlockPos position, SortBy ordering, boolean isPlayer) implements CustomPacketPayload {
	
	public static Codec<SortPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockPos.CODEC.fieldOf("position").forGetter(SortPacket::position),
		SortBy.CODEC.fieldOf("ordering").forGetter(SortPacket::ordering),
		Codec.BOOL.fieldOf("isPlayer").forGetter(SortPacket::isPlayer)
	).apply(instance, SortPacket::new));

	public static StreamCodec<ByteBuf, SortPacket> STREAM_CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, SortPacket::position,
		SortBy.STREAM_CODEC, SortPacket::ordering,
		ByteBufCodecs.BOOL, SortPacket::isPlayer,
		SortPacket::new
	);

	public static final CustomPacketPayload.Type<SortPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "inventorysort"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
