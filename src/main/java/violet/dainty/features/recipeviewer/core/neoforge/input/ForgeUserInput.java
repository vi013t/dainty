package violet.dainty.features.recipeviewer.core.neoforge.input;

import com.mojang.blaze3d.platform.InputConstants;

import net.neoforged.neoforge.client.event.ScreenEvent;
import violet.dainty.features.recipeviewer.core.gui.input.InputType;
import violet.dainty.features.recipeviewer.core.gui.input.MouseUtil;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public final class ForgeUserInput {
	private ForgeUserInput() {}

	public static UserInput fromEvent(ScreenEvent.KeyPressed keyEvent) {
		InputConstants.Key input = InputConstants.getKey(keyEvent.getKeyCode(), keyEvent.getScanCode());
		double mouseX = MouseUtil.getX();
		double mouseY = MouseUtil.getY();
		int modifiers = keyEvent.getModifiers();
		// execute the input immediately, on key pressed. do not wait for key released
		return new UserInput(input, mouseX, mouseY, modifiers, InputType.IMMEDIATE);
	}

	public static Optional<UserInput> fromEvent(ScreenEvent.MouseButtonPressed event) {
		int button = event.getButton();
		if (button < 0) {
			return Optional.empty();
		}
		InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(button);
		UserInput userInput = new UserInput(input, event.getMouseX(), event.getMouseY(), 0, InputType.SIMULATE);
		return Optional.of(userInput);
	}

	public static Optional<UserInput> fromEvent(ScreenEvent.MouseButtonReleased event) {
		int button = event.getButton();
		if (button < 0) {
			return Optional.empty();
		}
		InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(button);
		UserInput userInput = new UserInput(input, event.getMouseX(), event.getMouseY(), 0, InputType.EXECUTE);
		return Optional.of(userInput);
	}
}
