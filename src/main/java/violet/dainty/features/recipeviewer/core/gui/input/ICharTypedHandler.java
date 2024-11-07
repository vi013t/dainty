package violet.dainty.features.recipeviewer.core.gui.input;

public interface ICharTypedHandler {
	boolean hasKeyboardFocus();

	boolean onCharTyped(char codePoint, int modifiers);
}
