package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum CropProgressProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (accessor.getBlock() == Blocks.WHEAT) {
			return IElementHelper.get().item(new ItemStack(Items.WHEAT));
		}

		if (accessor.getBlock() == Blocks.BEETROOTS) {
			return IElementHelper.get().item(new ItemStack(Items.BEETROOT));
		}

		return null;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		BlockState state = accessor.getBlockState();
		Block block = state.getBlock();

		if (block instanceof CropBlock crop) {
			addMaturityTooltip(tooltip, crop.getAge(state) / (float) crop.getMaxAge());
		} else if (block instanceof BushBlock || block instanceof BonemealableBlock) {
			if (state.hasProperty(BlockStateProperties.AGE_2)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_2) / 2F);
			} else if (state.hasProperty(BlockStateProperties.AGE_3)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_3) / 3F);
			} else if (state.hasProperty(BlockStateProperties.AGE_4)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_4) / 4F);
			} else if (state.hasProperty(BlockStateProperties.AGE_5)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_5) / 5F);
			} else if (state.hasProperty(BlockStateProperties.AGE_7)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_7) / 7F);
			} else if (state.hasProperty(BlockStateProperties.AGE_15)) {
				addMaturityTooltip(tooltip, state.getValue(BlockStateProperties.AGE_15) / 15F);
			} else if (state.is(BlockTags.MAINTAINS_FARMLAND) && accessor.getLevel()
					.getBlockState(accessor.getPosition().below())
					.getBlock() instanceof FarmBlock) {
				addMaturityTooltip(tooltip, 1);
			}
		}
	}

	private static void addMaturityTooltip(ITooltip tooltip, float growthValue) {
		MutableComponent component;
		if (growthValue < 1) {
			component = IThemeHelper.get().info(String.format("%.0f%%", growthValue * 100));
		} else {
			component = IThemeHelper.get().success(Component.translatable("tooltip.dainty.crop_mature"));
		}
		tooltip.add(Component.translatable("tooltip.dainty.crop_growth", component));
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_CROP_PROGRESS;
	}

}
