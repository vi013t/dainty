package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketRequestCheatPermission;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class GlobalInputHandler implements IUserInputHandler {
	private final IClientToggleState toggleState;

	public GlobalInputHandler(IClientToggleState toggleState) {
		this.toggleState = toggleState;
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		if (input.is(keyBindings.getToggleOverlay())) {
			if (!input.isSimulate()) {
				toggleState.toggleOverlayEnabled();
			}
			return Optional.of(this);
		}

		if (input.is(keyBindings.getToggleBookmarkOverlay())) {
			if (!input.isSimulate()) {
				toggleState.toggleBookmarkEnabled();
			}
			return Optional.of(this);
		}

		if (input.is(keyBindings.getToggleCheatMode())) {
			if (!input.isSimulate()) {
				toggleState.toggleCheatItemsEnabled();
				if (toggleState.isCheatItemsEnabled()) {
					IConnectionToServer serverConnection = Internal.getServerConnection();
					serverConnection.sendPacketToServer(PacketRequestCheatPermission.INSTANCE);
				}
			}
			return Optional.of(this);
		}

		if (input.is(keyBindings.getToggleEditMode())) {
			if (!input.isSimulate()) {
				toggleState.toggleEditModeEnabled();
			}
			return Optional.of(this);
		}

		return Optional.empty();
	}
}
