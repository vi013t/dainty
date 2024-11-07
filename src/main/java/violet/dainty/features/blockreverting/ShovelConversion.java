package violet.dainty.features.blockreverting;

import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import violet.dainty.config.DaintyConfig;

public class ShovelConversion implements ConversionTool<ShovelItem> {
	
	private static final Map<BlockState, ImmutablePair<BlockState, ModConfigSpec.ConfigValue<Boolean>>> CONVERSIONS = Map.ofEntries(
		Map.entry(Blocks.DIRT_PATH.defaultBlockState(), ImmutablePair.of(Blocks.DIRT.defaultBlockState(), DaintyConfig.ENABLE_UNPATHING_DIRT))
	);

	@Override
	public Map<BlockState, ImmutablePair<BlockState, ModConfigSpec.ConfigValue<Boolean>>> getReverseConversions() {
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
