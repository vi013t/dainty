package violet.dainty.features.blocktooltips.addon.access;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;

public class HeldItemProvider implements IEntityComponentProvider {
	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		LivingEntity entity = (LivingEntity) accessor.getEntity();
		if (!entity.getMainHandItem().isEmpty()) {
			tooltip.add(Component.translatable("dainty.access.held_item.main", entity.getMainHandItem().getHoverName()));
		}
		if (!entity.getOffhandItem().isEmpty()) {
			tooltip.add(Component.translatable("dainty.access.held_item.off", entity.getOffhandItem().getHoverName()));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.ACCESS_HELD_ITEM;
	}
}
