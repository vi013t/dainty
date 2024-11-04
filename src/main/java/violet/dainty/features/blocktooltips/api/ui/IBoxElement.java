package violet.dainty.features.blocktooltips.api.ui;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.impl.ui.StyledElement;

@SuppressWarnings("unused")
public interface IBoxElement extends IElement, StyledElement {

	ITooltip getTooltip();

	void setBoxProgress(MessageType type, float progress);

	float getBoxProgress();

	void clearBoxProgress();

	void setIcon(@Nullable IElement icon);

	int padding(ScreenDirection direction);

	void setPadding(ScreenDirection direction, int value);

	@Override
	BoxStyle getStyle();
}
