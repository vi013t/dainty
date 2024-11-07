package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.gui.input.IDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class NullDragHandler implements IDragHandler {
	public static final NullDragHandler INSTANCE = new NullDragHandler();

	private NullDragHandler() {

	}

	@Override
	public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
		return Optional.empty();
	}

	@Override
	public boolean handleDragComplete(Screen screen, UserInput input) {
		return false;
	}
}
