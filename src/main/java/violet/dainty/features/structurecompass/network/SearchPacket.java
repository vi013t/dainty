package violet.dainty.features.structurecompass.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.Dainty;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.features.structurecompass.items.ExplorersCompassItem;
import violet.dainty.features.structurecompass.util.ItemUtils;

public record SearchPacket(ResourceLocation groupKey, List<ResourceLocation> structureKeys, BlockPos pos) implements CustomPacketPayload {
	
	public static final Type<SearchPacket> TYPE = new Type<SearchPacket>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "structuresearch"));
	
	public static final StreamCodec<FriendlyByteBuf, SearchPacket> CODEC = StreamCodec.ofMember(SearchPacket::write, SearchPacket::read);

	public static SearchPacket read(FriendlyByteBuf buf) {
		final ResourceLocation groupKey = buf.readResourceLocation();
		final List<ResourceLocation> structureKeys = new ArrayList<ResourceLocation>();
		int numStructures = buf.readInt();
		for (int i = 0; i < numStructures; i++) {
			structureKeys.add(buf.readResourceLocation());
		}
		final BlockPos pos = buf.readBlockPos();
		return new SearchPacket(groupKey, structureKeys, pos);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeResourceLocation(groupKey);
		buf.writeInt(structureKeys.size());
		for (ResourceLocation key : structureKeys) {
			buf.writeResourceLocation(key);
		}
		buf.writeBlockPos(pos);
	}

	public static void handle(SearchPacket packet, IPayloadContext context) {
		if (context.flow().isServerbound()) {
			context.enqueueWork(() -> {
				final ItemStack stack = ItemUtils.getHeldItem(context.player(), StructureCompass.explorersCompass);
				if (!stack.isEmpty()) {
					final ExplorersCompassItem explorersCompass = (ExplorersCompassItem) stack.getItem();
					explorersCompass.searchForStructure((ServerLevel) context.player().level(), context.player(), packet.groupKey, packet.structureKeys, packet.pos, stack);
				}
			});
		}
	}
	
	@Override
	public Type<SearchPacket> type() {
		return TYPE;
	}
}
