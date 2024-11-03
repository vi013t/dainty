package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display.ItemDisplay;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum ItemDisplayProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public @Nullable IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		ItemDisplay itemDisplay = (ItemDisplay) accessor.getEntity();
		if (itemDisplay.getSlot(0).get().isEmpty()) {
			return null;
		}
		return IElementHelper.get().item(itemDisplay.getSlot(0).get());
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ITEM_DISPLAY;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
