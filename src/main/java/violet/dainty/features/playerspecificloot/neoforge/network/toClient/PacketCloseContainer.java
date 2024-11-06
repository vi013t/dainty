package violet.dainty.features.playerspecificloot.neoforge.network.toClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.features.playerspecificloot.neoforge.Lootr;
import violet.dainty.features.playerspecificloot.neoforge.network.ILootrNeoForgePacket;
import violet.dainty.features.playerspecificloot.neoforge.network.client.ClientHandlers;

public record PacketCloseContainer(BlockPos position) implements ILootrNeoForgePacket {
  public static final CustomPacketPayload.Type<PacketCloseContainer> TYPE = new CustomPacketPayload.Type<>(Lootr.rl("close_container"));
  public static final StreamCodec<FriendlyByteBuf, PacketCloseContainer> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, PacketCloseContainer::position, PacketCloseContainer::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientHandlers.handleCloseContainer(this.position);
  }


  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
