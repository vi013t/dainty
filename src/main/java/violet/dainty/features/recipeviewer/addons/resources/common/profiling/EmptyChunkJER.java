package violet.dainty.features.recipeviewer.addons.resources.common.profiling;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;


public class EmptyChunkJER extends EmptyLevelChunk {
    public EmptyChunkJER(ServerLevel level, int x, int z) {
        super(level, new ChunkPos(x, z), null);
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        return null;
    }
}
