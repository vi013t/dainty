package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class NullInputHandler implements IUserInputHandler {
	public static final NullInputHandler INSTANCE = new NullInputHandler();

	private NullInputHandler() {

	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		return Optional.empty();
	}
}
