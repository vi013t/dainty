package violet.dainty.features.inventorysorting;

import java.util.Comparator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyDataAttachments;

public enum SortBy {

	ID(Comparator.comparingInt(itemStack -> Item.getId(itemStack.getItem()))),
	QUANTITY(Comparator.comparingInt(itemStack -> itemStack.getCount()));

	public static StreamCodec<ByteBuf, SortBy> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, SortBy::ordinal,
		SortBy::fromOrdinal
	);

	public static Codec<SortBy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("ordinal").forGetter(SortBy::ordinal)
	).apply(instance, SortBy::fromOrdinal));

	private final Comparator<ItemStack> comparator;
	
	private SortBy(Comparator<ItemStack> comparator) {
		this.comparator = comparator;
	}

	public Comparator<ItemStack> comparator() {
		return this.comparator;
	}

	public ResourceLocation buttonTexture() {
		return ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "textures/gui/" + "sort_by_" + this.name().toLowerCase() + ".png");
	}

	public SortBy next() {
		return SortBy.values()[(this.ordinal() + 1) % SortBy.values().length];
	}

	private static SortBy fromOrdinal(int ordinal) {
		return SortBy.values()[ordinal];
	}

	public static SortBy defaultSorting() {
		return SortBy.ID;
	}

	@SuppressWarnings({ "null", "resource" })
	public static SortBy preferenceOf(Player player) {
		return Minecraft.getInstance().level.getBlockEntity(player.getData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE).position()).getData(DaintyDataAttachments.SORT_ORDER);
	}
}
