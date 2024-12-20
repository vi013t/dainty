package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.gui.input.IDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;
import java.util.function.Supplier;

public class ProxyDragHandler implements IDragHandler {
	private final Supplier<IDragHandler> source;

	public ProxyDragHandler(Supplier<IDragHandler> source) {
		this.source = source;
	}

	@Override
	public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
		return this.source.get().handleDragStart(screen, input);
	}

	@Override
	public boolean handleDragComplete(Screen screen, UserInput input) {
		return this.source.get().handleDragComplete(screen, input);
	}

	@Override
	public void handleDragCanceled() {
		this.source.get().handleDragCanceled();
	}
}
