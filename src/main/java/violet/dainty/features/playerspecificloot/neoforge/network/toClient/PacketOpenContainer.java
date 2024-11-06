package violet.dainty.features.playerspecificloot.neoforge.network.toClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.features.playerspecificloot.neoforge.Lootr;
import violet.dainty.features.playerspecificloot.neoforge.network.ILootrNeoForgePacket;
import violet.dainty.features.playerspecificloot.neoforge.network.client.ClientHandlers;

public record PacketOpenContainer(BlockPos position) implements ILootrNeoForgePacket {
  public static final CustomPacketPayload.Type<PacketOpenContainer> TYPE = new CustomPacketPayload.Type<>(Lootr.rl("open_container"));
  public static final StreamCodec<FriendlyByteBuf, PacketOpenContainer> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, PacketOpenContainer::position, PacketOpenContainer::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientHandlers.handleOpenContainer(this.position);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
