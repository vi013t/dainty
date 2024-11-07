package violet.dainty.features.recipeviewer.core.common.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.config.GiveMode;
import violet.dainty.features.recipeviewer.core.common.network.ServerPacketContext;
import violet.dainty.features.recipeviewer.core.common.network.codecs.EnumStreamCodec;
import violet.dainty.features.recipeviewer.core.common.util.ServerCommandUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public class PacketGiveItemStack extends PlayToServerPacket<PacketGiveItemStack> {
	public static final CustomPacketPayload.Type<PacketGiveItemStack> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "give_item_stack"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketGiveItemStack> STREAM_CODEC = StreamCodec.composite(
		ItemStack.STREAM_CODEC,
		p -> p.itemStack,
		new EnumStreamCodec<>(GiveMode.class),
		p -> p.giveMode,
		PacketGiveItemStack::new
	);

	private final ItemStack itemStack;
	private final GiveMode giveMode;

	public PacketGiveItemStack(ItemStack itemStack, GiveMode giveMode) {
		this.itemStack = itemStack;
		this.giveMode = giveMode;
	}

	@Override
	public Type<PacketGiveItemStack> type() {
		return TYPE;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, PacketGiveItemStack> streamCodec() {
		return STREAM_CODEC;
	}

	@Override
	public void process(ServerPacketContext context) {
		ServerCommandUtil.executeGive(context, itemStack, giveMode);
	}
}
