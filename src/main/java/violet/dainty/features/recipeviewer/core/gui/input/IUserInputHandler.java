package violet.dainty.features.recipeviewer.core.gui.input;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;

import java.util.Optional;

public interface IUserInputHandler {
	Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings);

	/**
	 * Called when a mouse is clicked but was handled and canceled by some other mouse handler.
	 */
	default void unfocus() {

	}

	default Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
		return Optional.empty();
	}
}
