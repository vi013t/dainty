package violet.dainty.features.blocktooltips.addon.core;

import com.google.common.base.Strings;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.IToggleableProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.TooltipPosition;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.util.ModIdentification;

public abstract class ModNameProvider implements IToggleableProvider {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends ModNameProvider implements IBlockComponentProvider {
		private static final ForBlock INSTANCE = new ForBlock();

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			String modName = null;
			if (accessor.isFakeBlock()) {
				modName = ModIdentification.getModName(accessor.getFakeBlock());
			}
			if (modName == null && WailaClientRegistration.instance().shouldPick(accessor.getBlockState())) {
				ItemStack pick = accessor.getPickedResult();
				if (!pick.isEmpty()) {
					modName = ModIdentification.getModName(pick);
				}
			}
			if (modName == null) {
				modName = ModIdentification.getModName(accessor.getBlock());
			}

			if (!Strings.isNullOrEmpty(modName)) {
				tooltip.add(IThemeHelper.get().modName(modName));
			}
		}
	}

	public static class ForEntity extends ModNameProvider implements IEntityComponentProvider {
		private static final ForEntity INSTANCE = new ForEntity();

		@Override
		public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
			tooltip.add(IThemeHelper.get().modName(ModIdentification.getModName(accessor.getEntity())));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.CORE_MOD_NAME;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.TAIL - 1;
	}

}
