package violet.dainty.features.blocktooltips.addon.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum BlockPropertiesProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		BlockBehaviour.Properties properties = accessor.getBlock().properties();
		IThemeHelper themes = IThemeHelper.get();
		tooltip.add(Component.translatable("dainty.block_destroy_time", themes.info(properties.destroyTime)));
		tooltip.add(Component.translatable("dainty.block_explosion_resistance", themes.info(properties.explosionResistance)));
		if (properties.jumpFactor != 1) {
			tooltip.add(Component.translatable("dainty.block_jump_factor", themes.info(properties.jumpFactor)));
		}
		if (properties.speedFactor != 1) {
			tooltip.add(Component.translatable("dainty.block_speed_factor", themes.info(properties.speedFactor)));
		}
		int igniteOdds = ((FireBlock) Blocks.FIRE).getIgniteOdds(accessor.getBlockState());
		if (igniteOdds != 0) {
			tooltip.add(Component.translatable("dainty.block_ignite_odds", themes.info(igniteOdds)));
		}
		int burnOdds = ((FireBlock) Blocks.FIRE).getBurnOdds(accessor.getBlockState());
		if (burnOdds != 0) {
			tooltip.add(Component.translatable("dainty.block_burn_odds", themes.info(burnOdds)));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.DEBUG_BLOCK_PROPERTIES;
	}

	@Override
	public boolean enabledByDefault() {
		return false;
	}
}
