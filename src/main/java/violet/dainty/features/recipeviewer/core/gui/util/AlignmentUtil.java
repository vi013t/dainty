package violet.dainty.features.recipeviewer.core.gui.util;

import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableSize2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;

public class AlignmentUtil {
	public static ImmutableRect2i align(ImmutableSize2i size, ImmutableRect2i availableArea, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		final int width = size.width();
		final int height = size.height();
		final int x = availableArea.getX() + horizontalAlignment.getXPos(availableArea.width(), width);
		final int y = availableArea.getY() + verticalAlignment.getYPos(availableArea.height(), height);
		return new ImmutableRect2i(x, y, width, height);
	}
}
