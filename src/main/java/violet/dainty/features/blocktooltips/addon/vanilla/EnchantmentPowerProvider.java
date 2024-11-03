package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;
import violet.dainty.features.blocktooltips.util.CommonProxy;

public enum EnchantmentPowerProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		float power = CommonProxy.getEnchantPowerBonus(accessor.getBlockState(), accessor.getLevel(), accessor.getPosition());
		if (power > 0) {
			tooltip.add(Component.translatable("dainty.ench_power", IThemeHelper.get().info(DisplayHelper.dfCommas.format(power))));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ENCHANTMENT_POWER;
	}

	@Override
	public int getDefaultPriority() {
		return -400;
	}
}
