package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum ItemBERProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
		if (accessor.getBlockEntity() != null) {
			ItemStack stack = accessor.getPickedResult();
			Minecraft.getInstance().addCustomNbtData(stack, accessor.getBlockEntity(), accessor.getLevel().registryAccess());
			return IElementHelper.get().item(stack);
		}
		return null;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ITEM_BER;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
