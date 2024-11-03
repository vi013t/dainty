package violet.dainty.features.blocktooltips;

import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.impl.theme.ThemeHelper;
import violet.dainty.features.blocktooltips.impl.ui.ElementHelper;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;

public final class JadeInternals {

	public static IWailaConfig getWailaConfig() {
		return Jade.CONFIG.get();
	}

	public static IElementHelper getElementHelper() {
		return ElementHelper.INSTANCE;
	}

	public static IDisplayHelper getDisplayHelper() {
		return DisplayHelper.INSTANCE;
	}

	public static IThemeHelper getThemeHelper() {
		return ThemeHelper.INSTANCE;
	}
}
