package violet.dainty.features.blocktooltips.addon.debug;

import java.util.Collection;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.BoxStyle;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum BlockStatesProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		BlockState state = accessor.getBlockState();
		Collection<Property<?>> properties = state.getProperties();
		if (properties.isEmpty()) {
			return;
		}
		IElementHelper helper = IElementHelper.get();
		IThemeHelper t = IThemeHelper.get();
		ITooltip box = helper.tooltip();
		properties.forEach(p -> {
			Comparable<?> value = state.getValue(p);
			MutableComponent valueText = Component.literal(" " + value).withStyle();
			if (p instanceof BooleanProperty) {
				valueText = value == Boolean.TRUE ? t.success(valueText) : t.danger(valueText);
			}
			box.add(Component.literal(p.getName() + ":").append(valueText));
		});
		tooltip.add(helper.box(box, BoxStyle.getNestedBox()));
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.DEBUG_BLOCK_STATES;
	}

	@Override
	public int getDefaultPriority() {
		return -4500;
	}

	@Override
	public boolean enabledByDefault() {
		return false;
	}
}
