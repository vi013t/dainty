package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import com.google.common.base.MoreObjects;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;
import java.util.function.Supplier;

public class ProxyInputHandler implements IUserInputHandler {
	private final Supplier<IUserInputHandler> source;

	public ProxyInputHandler(Supplier<IUserInputHandler> source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("source", this.source.get())
			.toString();
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		return this.source.get().handleUserInput(screen, input, keyBindings);
	}

	@Override
	public void unfocus() {
		this.source.get().unfocus();
	}

	@Override
	public Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
		return this.source.get().handleMouseScrolled(mouseX, mouseY, scrollDeltaX, scrollDeltaY);
	}
}
