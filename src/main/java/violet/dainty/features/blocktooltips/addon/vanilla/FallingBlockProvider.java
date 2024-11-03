package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum FallingBlockProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {

	}

	@Override
	public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		FallingBlockEntity entity = (FallingBlockEntity) accessor.getEntity();
		ItemStack stack = new ItemStack(entity.getBlockState().getBlock());
		if (stack.isEmpty()) {
			return currentIcon;
		}
		return IElementHelper.get().item(stack);
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_FALLING_BLOCK;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
