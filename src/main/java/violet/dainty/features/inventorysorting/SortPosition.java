package violet.dainty.features.inventorysorting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public record SortPosition(BlockPos position) {

	public static Codec<SortPosition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockPos.CODEC.fieldOf("position").forGetter(SortPosition::position)
	).apply(instance, SortPosition::new));

	public static StreamCodec<ByteBuf, SortPosition> STREAM_CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, SortPosition::position,
		SortPosition::new
	);

	public static SortPosition error() {
		throw new UnsupportedOperationException("Cannot construct default instance of SortPosition. Check for hasData() before calling getData().");
	}
}
