package violet.dainty.features.blocktooltips.api.callback;

import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.ui.IBoxElement;

@FunctionalInterface
public interface JadeTooltipCollectedCallback {

	void onTooltipCollected(IBoxElement rootElement, Accessor<?> accessor);

}
