package violet.dainty.features.blocktooltips.api.callback;

import org.apache.commons.lang3.mutable.MutableObject;

import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.theme.Theme;

@FunctionalInterface
public interface JadeBeforeTooltipCollectCallback {

	boolean beforeCollecting(MutableObject<Theme> theme, Accessor<?> accessor);

}
