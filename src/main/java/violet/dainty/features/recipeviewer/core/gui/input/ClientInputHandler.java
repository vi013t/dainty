package violet.dainty.features.recipeviewer.core.gui.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.core.util.ReflectionUtil;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.DragRouter;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.UserInputRouter;

import java.util.List;

public class ClientInputHandler {
	private final List<ICharTypedHandler> charTypedHandlers;
	private final UserInputRouter inputRouter;
	private final DragRouter dragRouter;
	private final IInternalKeyMappings keybindings;
	private final ReflectionUtil reflectionUtil = new ReflectionUtil();

	public ClientInputHandler(
		List<ICharTypedHandler> charTypedHandlers,
		UserInputRouter inputRouter,
		DragRouter dragRouter,
		IInternalKeyMappings keybindings
	) {
		this.charTypedHandlers = charTypedHandlers;
		this.inputRouter = inputRouter;
		this.dragRouter = dragRouter;
		this.keybindings = keybindings;
	}

	public void onInitGui() {
		this.inputRouter.handleGuiChange();
		this.dragRouter.handleGuiChange();
	}

	/**
	 * When we have keyboard focus, use Pre
	 */
	public boolean onKeyboardKeyPressedPre(Screen screen, UserInput input) {
		if (!isContainerTextFieldFocused(screen)) {
			return this.inputRouter.handleUserInput(screen, input, keybindings);
		}
		return false;
	}

	/**
	 * Without keyboard focus, use Post
	 */
	public boolean onKeyboardKeyPressedPost(Screen screen, UserInput input) {
		if (isContainerTextFieldFocused(screen)) {
			return this.inputRouter.handleUserInput(screen, input, keybindings);
		}
		return false;
	}

	/**
	 * When we have keyboard focus, use Pre
	 */
	public boolean onKeyboardCharTypedPre(Screen screen, char codePoint, int modifiers) {
		if (!isContainerTextFieldFocused(screen)) {
			return handleCharTyped(codePoint, modifiers);
		}
		return false;
	}

	/**
	 * Without keyboard focus, use Post
	 */
	public void onKeyboardCharTypedPost(Screen screen, char codePoint, int modifiers) {
		if (isContainerTextFieldFocused(screen)) {
			handleCharTyped(codePoint, modifiers);
		}
	}

	public boolean onGuiMouseClicked(Screen screen, UserInput input) {
		boolean handled = this.inputRouter.handleUserInput(screen, input, keybindings);

		if (Minecraft.getInstance().screen == screen && input.is(keybindings.getLeftClick())) {
			handled |= this.dragRouter.startDrag(screen, input);
		}
		return handled;
	}

	public boolean onGuiMouseReleased(Screen screen, UserInput input) {
		boolean handled = this.inputRouter.handleUserInput(screen, input, keybindings);

		if (input.is(keybindings.getLeftClick())) {
			handled |= this.dragRouter.completeDrag(screen, input);
		}
		return handled;
	}

	public boolean onGuiMouseScroll(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
		return this.inputRouter.handleMouseScrolled(mouseX, mouseY, scrollDeltaX, scrollDeltaY);
	}

	private boolean handleCharTyped(char codePoint, int modifiers) {
		return this.charTypedHandlers.stream()
			.filter(ICharTypedHandler::hasKeyboardFocus)
			.anyMatch(handler -> handler.onCharTyped(codePoint, modifiers));
	}

	private boolean isContainerTextFieldFocused(Screen screen) {
		return reflectionUtil.getFieldWithClass(screen, EditBox.class)
			.anyMatch(textField -> textField.isActive() && textField.isFocused());
	}
}
