package violet.dainty.features.recipeviewer.core.commonapi.gui;

import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableAnimated;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;

/**
 * A timer to help render things that normally depend on ticks.
 * Get an instance from {@link IGuiHelper#createTickTimer(int, int, boolean)}.
 * These are used in the internal implementation of {@link IDrawableAnimated}.
 */
public interface ITickTimer {
	int getValue();

	int getMaxValue();
}
