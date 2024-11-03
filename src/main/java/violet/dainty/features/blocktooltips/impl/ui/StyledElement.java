package violet.dainty.features.blocktooltips.impl.ui;

import violet.dainty.features.blocktooltips.api.ui.BoxStyle;
import violet.dainty.features.blocktooltips.api.ui.IElement;

public interface StyledElement extends IElement {
	IElement getIcon();

	BoxStyle getStyle();
}
