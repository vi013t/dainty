package violet.dainty.features.blockreverting;

import java.util.Map;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AxeConversion implements ConversionTool<AxeItem> {

	private static final Map<BlockState, BlockState> CONVERSIONS = Map.ofEntries(
		Map.entry(Blocks.STRIPPED_ACACIA_LOG.defaultBlockState(), Blocks.ACACIA_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_ACACIA_WOOD.defaultBlockState(), Blocks.STRIPPED_ACACIA_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_BAMBOO_BLOCK.defaultBlockState(), Blocks.BAMBOO_BLOCK.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_BIRCH_LOG.defaultBlockState(), Blocks.BIRCH_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_BIRCH_WOOD.defaultBlockState(), Blocks.BIRCH_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_CHERRY_LOG.defaultBlockState(), Blocks.CHERRY_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_CHERRY_WOOD.defaultBlockState(), Blocks.CHERRY_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_CRIMSON_HYPHAE.defaultBlockState(), Blocks.CRIMSON_HYPHAE.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_CRIMSON_STEM.defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState(), Blocks.DARK_OAK_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_DARK_OAK_WOOD.defaultBlockState(), Blocks.DARK_OAK_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState(), Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState(), Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState(), Blocks.MANGROVE_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_MANGROVE_WOOD.defaultBlockState(), Blocks.MANGROVE_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_OAK_WOOD.defaultBlockState(), Blocks.OAK_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState(), Blocks.SPRUCE_LOG.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_SPRUCE_WOOD.defaultBlockState(), Blocks.SPRUCE_WOOD.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_WARPED_HYPHAE.defaultBlockState(), Blocks.WARPED_HYPHAE.defaultBlockState()),
		Map.entry(Blocks.STRIPPED_WARPED_STEM.defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState())
	);

	@Override
	public Map<BlockState, BlockState> getReverseConversions() {
		return CONVERSIONS;
	}

	@Override
	public Class<AxeItem> getToolClass() {
		return AxeItem.class;
	}

	@Override
	public SoundEvent getConversionSound() {
		return SoundEvents.AXE_STRIP;
	}
	
}
