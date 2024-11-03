package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.ScreenDirection;
import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum ArmorStandProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		ArmorStand entity = (ArmorStand) accessor.getEntity();
		boolean empty = true;
		for (ItemStack stack : entity.getArmorSlots()) {
			if (stack.isEmpty()) {
				continue;
			}
			tooltip.add(IElementHelper.get().smallItem(stack));
			tooltip.append(IDisplayHelper.get().stripColor(stack.getHoverName()));
			tooltip.setLineMargin(-1, ScreenDirection.DOWN, -1);
			empty = false;
		}
		if (!empty) {
			tooltip.setLineMargin(-1, ScreenDirection.DOWN, 1);
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ARMOR_STAND;
	}

}
