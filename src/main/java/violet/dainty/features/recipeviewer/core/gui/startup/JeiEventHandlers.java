package violet.dainty.features.recipeviewer.core.gui.startup;

import violet.dainty.features.recipeviewer.core.gui.events.GuiEventHandler;
import violet.dainty.features.recipeviewer.core.gui.input.ClientInputHandler;

public record JeiEventHandlers(
	GuiEventHandler guiEventHandler,
	ClientInputHandler clientInputHandler,
	ResourceReloadHandler resourceReloadHandler
) {
}
