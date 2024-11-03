package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;
import violet.dainty.features.blocktooltips.util.CommonProxy;

public enum TotalEnchantmentPowerProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		Level world = accessor.getLevel();
		BlockPos pos = accessor.getPosition();
		float power = 0;
		// EnchantmentMenu.java
		for (BlockPos blockpos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
			if (EnchantingTableBlock.isValidBookShelf(world, pos, blockpos)) {
				power += getPower(world, pos.offset(blockpos));
			}
		}

		if (power > 0) {
			tooltip.add(Component.translatable("dainty.ench_power", IThemeHelper.get().info(DisplayHelper.dfCommas.format(power))));
		}
	}

	public static float getPower(Level world, BlockPos pos) {
		return CommonProxy.getEnchantPowerBonus(world.getBlockState(pos), world, pos);
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_TOTAL_ENCHANTMENT_POWER;
	}

	@Override
	public int getDefaultPriority() {
		return -400;
	}
}
