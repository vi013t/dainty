package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum PaintingProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		Painting painting = (Painting) accessor.getEntity();
		ResourceLocation id = painting.getVariant().unwrapKey().orElseThrow().location();
		tooltip.add(IThemeHelper.get().warning(Component.translatable(id.toLanguageKey("painting", "title"))));
		tooltip.add(Component.translatable(id.toLanguageKey("painting", "author")));
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_PAINTING;
	}
}
