package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.impl.ui.ItemStackElement;
import violet.dainty.features.blocktooltips.overlay.RayTracing;
import violet.dainty.features.blocktooltips.util.ClientProxy;

public enum BlockDisplayProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public @Nullable IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
		BlockDisplay itemDisplay = (BlockDisplay) accessor.getEntity();
		Block block = itemDisplay.getBlockState().getBlock();
		if (block.asItem() == Items.AIR) {
			return null;
		}
		IElement icon = ItemStackElement.of(new ItemStack(block));
		if (RayTracing.isEmptyElement(icon) && block instanceof LiquidBlock) {
			icon = ClientProxy.elementFromLiquid(itemDisplay.getBlockState());
		}
		return icon;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_BLOCK_DISPLAY;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
