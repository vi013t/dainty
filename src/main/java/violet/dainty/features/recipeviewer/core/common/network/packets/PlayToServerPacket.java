package violet.dainty.features.recipeviewer.core.common.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import violet.dainty.features.recipeviewer.core.common.network.ServerPacketContext;

public abstract class PlayToServerPacket<T extends PlayToServerPacket<T>> implements CustomPacketPayload {
	@Override
	public abstract Type<T> type();
	public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
	public abstract void process(ServerPacketContext context);
}
