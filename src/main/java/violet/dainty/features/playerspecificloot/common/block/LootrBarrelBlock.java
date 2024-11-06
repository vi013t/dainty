package violet.dainty.features.playerspecificloot.common.block;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.data.ILootrInfoProvider;
import violet.dainty.features.playerspecificloot.api.data.blockentity.ILootrBlockEntity;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrBarrelBlockEntity;

public class LootrBarrelBlock extends BarrelBlock {
  public LootrBarrelBlock(Properties p_49046_) {
    super(p_49046_);
  }

  @SuppressWarnings("deprecation")
@Override
  public float getExplosionResistance() {
    return LootrAPI.getExplosionResistance(this, super.getExplosionResistance());
  }

  @Override
  public void onRemove(@Nonnull BlockState pState, @Nonnull Level pLevel, @Nonnull BlockPos pPos, @Nonnull BlockState pNewState, boolean pIsMoving) {
    if (!pState.is(pNewState.getBlock())) {
      BlockEntity blockentity = pLevel.getBlockEntity(pPos);
      if (blockentity instanceof Container) {
        pLevel.updateNeighbourForOutputSignal(pPos, this);
      }

      if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
        pLevel.removeBlockEntity(pPos);
      }
    }
  }

  @Override
  public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult trace) {
    if (level.isClientSide() || player.isSpectator() || !(player instanceof ServerPlayer serverPlayer)) {
      return InteractionResult.CONSUME;
    }
    if (serverPlayer.isShiftKeyDown()) {
      LootrAPI.handleProviderSneak(ILootrInfoProvider.of(pos, level), serverPlayer);
    } else {
      LootrAPI.handleProviderOpen(ILootrInfoProvider.of(pos, level), serverPlayer);
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new LootrBarrelBlockEntity(pos, state);
  }

  @Override
  public boolean triggerEvent(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, int id, int param) {
    super.triggerEvent(state, world, pos, id, param);
    BlockEntity blockEntity = world.getBlockEntity(pos);
    return blockEntity != null && blockEntity.triggerEvent(id, param);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
    return ILootrBlockEntity::ticker;
  }

  @Override
  public void tick(@Nonnull BlockState pState, @Nonnull ServerLevel pLevel, @Nonnull BlockPos pPos, @Nonnull RandomSource pRandom) {
    BlockEntity blockentity = pLevel.getBlockEntity(pPos);
    if (blockentity instanceof LootrBarrelBlockEntity barrel) {
      barrel.recheckOpen();
    }
  }

  @Override
  public boolean hasAnalogOutputSignal(@Nonnull BlockState pState) {
    return true;
  }

  @Override
  public float getDestroyProgress(@Nonnull BlockState p_60466_, @Nonnull Player p_60467_, @Nonnull BlockGetter p_60468_, @Nonnull BlockPos p_60469_) {
    return LootrAPI.getDestroyProgress(p_60466_, p_60467_, p_60468_, p_60469_, super.getDestroyProgress(p_60466_, p_60467_, p_60468_, p_60469_));
  }

  @Override
  public int getAnalogOutputSignal(@Nonnull BlockState pBlockState, @Nonnull Level pLevel, @Nonnull BlockPos pPos) {
    return LootrAPI.getAnalogOutputSignal(pBlockState, pLevel, pPos, 0);
  }
}
