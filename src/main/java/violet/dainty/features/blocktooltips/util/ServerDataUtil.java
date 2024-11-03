package violet.dainty.features.blocktooltips.util;

import java.util.Optional;

import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public class ServerDataUtil {
	public static <T> Optional<T> read(CompoundTag data, MapDecoder<T> codec) {
		MapLike<Tag> mapLike = NbtOps.INSTANCE.getMap(data).getOrThrow();
		return codec.decode(NbtOps.INSTANCE, mapLike).result();
	}

	public static <T> void write(CompoundTag data, MapEncoder<T> codec, T value) {
		Tag tag = codec.encode(value, NbtOps.INSTANCE, NbtOps.INSTANCE.mapBuilder()).build(new CompoundTag()).result().orElseThrow();
		data.merge((CompoundTag) tag);
	}
}
