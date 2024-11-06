package violet.dainty.features.playerspecificloot.common.block.entity;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.playerspecificloot.api.data.LootrBlockType;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;

public class LootrTrappedChestBlockEntity extends LootrChestBlockEntity {
  public LootrTrappedChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(LootrRegistry.getTrappedChestBlockEntity(), pWorldPosition, pBlockState);
  }

  @Override
  protected void signalOpenCount(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, int p_155868_, int p_155869_) {
    super.signalOpenCount(level, pos, state, p_155868_, p_155869_);
    if (p_155868_ != p_155869_) {
      Block block = state.getBlock();
      level.updateNeighborsAt(pos, block);
      level.updateNeighborsAt(pos.below(), block);
    }
  }

  @Override
  public LootrBlockType getInfoBlockType() {
    return LootrBlockType.TRAPPED_CHEST;
  }
}
