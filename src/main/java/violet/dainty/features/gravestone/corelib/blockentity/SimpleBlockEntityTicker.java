package violet.dainty.features.gravestone.corelib.blockentity;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleBlockEntityTicker<T extends BlockEntity> implements BlockEntityTicker<T> {

    @Override
    public void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull T entity) {
        if (entity instanceof ITickableBlockEntity tickable) {
            tickable.tick();
        }
        if (level.isClientSide) {
            if (entity instanceof IClientTickableBlockEntity tickable) {
                tickable.tickClient();
            }
        } else {
            if (entity instanceof IServerTickableBlockEntity tickable) {
                tickable.tickServer();
            }
        }
    }

}
