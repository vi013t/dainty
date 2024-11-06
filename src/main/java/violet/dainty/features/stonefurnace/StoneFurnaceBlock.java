package violet.dainty.features.stonefurnace;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import violet.dainty.registries.DaintyBlockEntities;

public class StoneFurnaceBlock extends AbstractFurnaceBlock {

    public static final MapCodec<StoneFurnaceBlock> CODEC = simpleCodec(StoneFurnaceBlock::new);

    public StoneFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

	public StoneFurnaceBlock() {
		super(BlockBehaviour.Properties
			.of()
			.mapColor(MapColor.STONE)
			.requiresCorrectToolForDrops()
			.strength(3.5F)
			.lightLevel(block -> block.getValue(BlockStateProperties.LIT) ? 13 : 0)
		);
	}

	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StoneFurnaceBlockEntity(pos, state);
	}
	
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createFurnaceTicker(level, blockEntityType, DaintyBlockEntities.STONE_FURNACE.get());
    }

	@Override
	protected MapCodec<? extends AbstractFurnaceBlock> codec() {
		return CODEC;
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
		player.openMenu((MenuProvider) blockEntity);
	}
	
}
