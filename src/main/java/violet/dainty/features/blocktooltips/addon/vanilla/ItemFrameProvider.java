package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;

public enum ItemFrameProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		ItemFrame itemFrame = (ItemFrame) accessor.getEntity();
		ItemStack stack = itemFrame.getItem();
		if (!stack.isEmpty()) {
			tooltip.add(IDisplayHelper.get().stripColor(stack.getHoverName()));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ITEM_FRAME;
	}
}
