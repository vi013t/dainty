package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum RedstoneProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		BlockState state = accessor.getBlockState();
		Block block = state.getBlock();
		IThemeHelper t = IThemeHelper.get();
		if (block instanceof LeverBlock) {
			Component info;
			if (state.getValue(BlockStateProperties.POWERED)) {
				info = t.success(Component.translatable("tooltip.dainty.state_on"));
			} else {
				info = t.danger(Component.translatable("tooltip.dainty.state_off"));
			}
			tooltip.add(Component.translatable("tooltip.dainty.state", info));
			return;
		}

		if (block == Blocks.REPEATER) {
			int delay = state.getValue(BlockStateProperties.DELAY);
			tooltip.add(Component.translatable("tooltip.dainty.delay", t.info(delay)));
			return;
		}

		if (block == Blocks.COMPARATOR) {
			ComparatorMode mode = state.getValue(BlockStateProperties.MODE_COMPARATOR);
			Component modeInfo = t.info(Component.translatable(
					"tooltip.dainty.mode_" + (mode == ComparatorMode.COMPARE ? "comparator" : "subtractor")));
			tooltip.add(Component.translatable("tooltip.dainty.mode", modeInfo));
			if (accessor.getServerData().contains("Signal")) {
				tooltip.add(Component.translatable("tooltip.dainty.power", t.info(accessor.getServerData().getInt("Signal"))));
			}
			return;
		}

		if (block instanceof CalibratedSculkSensorBlock && accessor.getServerData().contains("Signal")) {
			tooltip.add(Component.translatable("dainty.input_signal", t.info(accessor.getServerData().getInt("Signal"))));
		}

		if (state.hasProperty(BlockStateProperties.POWER)) {
			tooltip.add(Component.translatable("tooltip.dainty.power", t.info(state.getValue(BlockStateProperties.POWER))));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlockEntity blockEntity = accessor.getBlockEntity();
		if (blockEntity instanceof ComparatorBlockEntity comparator) {
			data.putInt("Signal", comparator.getOutputSignal());
		} else if (blockEntity instanceof CalibratedSculkSensorBlockEntity) {
			Direction direction = accessor.getBlockState().getValue(CalibratedSculkSensorBlock.FACING).getOpposite();
			int signal = accessor.getLevel().getSignal(accessor.getPosition().relative(direction), direction);
			data.putInt("Signal", signal);
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_REDSTONE;
	}

}