package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.gui.input.IMouseOverable;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class SameElementInputHandler implements IUserInputHandler {
	private final IUserInputHandler handler;
	private final IMouseOverable mouseOverable;

	public SameElementInputHandler(IUserInputHandler handler, IMouseOverable mouseOverable) {
		this.handler = handler;
		this.mouseOverable = mouseOverable;
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		double mouseX = input.getMouseX();
		double mouseY = input.getMouseY();
		if (mouseOverable.isMouseOver(mouseX, mouseY)) {
			return this.handler.handleUserInput(screen, input, keyBindings)
				.map(handled -> this);
		}
		return Optional.empty();
	}

	@Override
	public void unfocus() {
		this.handler.unfocus();
	}

	@Override
	public Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
		if (mouseOverable.isMouseOver(mouseX, mouseY)) {
			return this.handler.handleMouseScrolled(mouseX, mouseY, scrollDeltaX, scrollDeltaY);
		}
		return Optional.empty();
	}
}
