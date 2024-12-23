package violet.dainty.features.playerspecificloot.neoforge.block.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.playerspecificloot.api.data.blockentity.ILootrBlockEntity;
import violet.dainty.features.playerspecificloot.neoforge.network.toClient.PacketCloseContainer;
import violet.dainty.features.playerspecificloot.neoforge.network.toClient.PacketOpenContainer;

public interface ILootrNeoForgeBlockEntity extends ILootrBlockEntity {
  @Override
  default void performOpen(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new PacketOpenContainer(asBlockEntity().getBlockPos()));
  }

  @Override
  default void performOpen() {
    PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) getInfoLevel(), new ChunkPos(asBlockEntity().getBlockPos()), new PacketOpenContainer(asBlockEntity().getBlockPos()));
  }

  @Override
  default void performClose(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new PacketCloseContainer(asBlockEntity().getBlockPos()));
  }

  @Override
  default void performClose() {
    PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) getInfoLevel(), new ChunkPos(asBlockEntity().getBlockPos()), new PacketCloseContainer(asBlockEntity().getBlockPos()));
  }

  @Override
  default void performUpdate(ServerPlayer player) {
    performUpdate();
  }

  @Override
  default void performUpdate() {
    markChanged();
    updatePacketViaForce();
  }

  @Override
  default void performDecay() {
    Level level = getInfoLevel();
    if (level == null || level.isClientSide()) {
      return;
    }
    level.destroyBlock(getInfoPos(), true);
  }
}
