package violet.dainty.features.blockreverting;

import java.util.Map;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ShovelConversion implements ConversionTool<ShovelItem> {
	
	private static final Map<BlockState, BlockState> CONVERSIONS = Map.ofEntries(
		Map.entry(Blocks.DIRT_PATH.defaultBlockState(), Blocks.DIRT.defaultBlockState())
	);

	@Override
	public Map<BlockState, BlockState> getReverseConversions() {
		return CONVERSIONS;
	}

	@Override
	public Class<ShovelItem> getToolClass() {
		return ShovelItem.class;
	}

	@Override
	public SoundEvent getConversionSound() {
		return SoundEvents.HOE_TILL;
	}
}
