package violet.dainty.features.blocktooltips.addon.core;

import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum BlockFaceProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		tooltip.replace(JadeIds.CORE_OBJECT_NAME, lists -> {
			List<IElement> lastList = lists.getLast();
			lastList.add(IElementHelper.get().text(Component.translatable("dainty.blockFace", directionName(accessor.getSide()))));
			return lists;
		});
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.CORE_BLOCK_FACE;
	}

	@Override
	public int getDefaultPriority() {
		return ObjectNameProvider.getBlock().getDefaultPriority() + 30;
	}

	@Override
	public boolean enabledByDefault() {
		return false;
	}

	public static MutableComponent directionName(Direction direction) {
		return Component.translatable("dainty." + direction.getSerializedName());
	}

}
