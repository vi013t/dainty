package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import violet.dainty.features.blocktooltips.addon.core.ObjectNameProvider;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.impl.ui.CompoundElement;

public enum WaxedProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (accessor.getPickedResult().isEmpty()) {
			return currentIcon;
		}
		IElementHelper helper = IElementHelper.get();
		IElement largeIcon = helper.item(accessor.getPickedResult());
		if (accessor.getBlockEntity() instanceof SignBlockEntity sign) {
			if (sign.isWaxed()) {
				return new CompoundElement(largeIcon, helper.item(Items.HONEYCOMB.getDefaultInstance(), 0.5f));
			} else {
				return largeIcon;
			}
		}
		return currentIcon;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (IWailaConfig.get().getGeneral().getEnableAccessibilityPlugin() && accessor.getBlockEntity() instanceof SignBlockEntity sign &&
				sign.isWaxed()) {
			String message = tooltip.getMessage(JadeIds.CORE_OBJECT_NAME);
			if (!message.isBlank()) {
				tooltip.replace(JadeIds.CORE_OBJECT_NAME, IThemeHelper.get().title(Component.translatable("dainty.waxed", message)));
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_WAXED;
	}

	@Override
	public int getDefaultPriority() {
		return ObjectNameProvider.getBlock().getDefaultPriority() + 10;
	}
}
