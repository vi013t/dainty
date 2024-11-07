package violet.dainty.features.blockreverting;

import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import violet.dainty.config.DaintyConfig;

public class HoeConversion implements ConversionTool<HoeItem> {
	
	private static final Map<BlockState, ImmutablePair<BlockState, ModConfigSpec.ConfigValue<Boolean>>> CONVERSIONS = Map.ofEntries(
		Map.entry(Blocks.FARMLAND.defaultBlockState(), ImmutablePair.of(Blocks.DIRT.defaultBlockState(), DaintyConfig.ENABLE_UNTILLING_FARMLAND))
	);

	@Override
	public Map<BlockState, ImmutablePair<BlockState, ModConfigSpec.ConfigValue<Boolean>>> getReverseConversions() {
		return CONVERSIONS;
	}

	@Override
	public Class<HoeItem> getToolClass() {
		return HoeItem.class;
	}

	@Override
	public SoundEvent getConversionSound() {
		return SoundEvents.HOE_TILL;
	}

	@Override
	public void performExtraConversionSteps(Level level, Player player, BlockState toConvert, BlockPos position) {

		// Farmland - break crop above
		if (toConvert == Blocks.FARMLAND.defaultBlockState() && level.getBlockState(position.above()).getBlock() instanceof CropBlock) {

			// Server - drop items and replace block
			if (!level.isClientSide()) {
				BlockPos cropPosition = position.above();
				BlockState cropBlock = level.getBlockState(cropPosition);
				Block.dropResources(cropBlock, level, cropPosition);
				level.removeBlock(position.above(), false);
			}

			// Server and client - play sound
			player.playSound(SoundEvents.ITEM_PICKUP);
		}
	}
}
