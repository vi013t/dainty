package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.input.CombinedRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class BookmarkInputHandler implements IUserInputHandler {
	private final CombinedRecipeFocusSource focusSource;
	private final BookmarkList bookmarkList;

	public BookmarkInputHandler(CombinedRecipeFocusSource focusSource, BookmarkList bookmarkList) {
		this.focusSource = focusSource;
		this.bookmarkList = bookmarkList;
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		if (input.is(keyBindings.getBookmark())) {
			return handleBookmark(input, keyBindings);
		}
		return Optional.empty();
	}

	private Optional<IUserInputHandler> handleBookmark(UserInput input, IInternalKeyMappings keyBindings) {
		return focusSource.getIngredientUnderMouse(input, keyBindings)
			.findFirst()
			.flatMap(clicked -> {
				if (input.isSimulate() ||
					bookmarkList.onElementBookmarked(clicked.getElement())
				) {
					IUserInputHandler handler = new SameElementInputHandler(this, clicked::isMouseOver);
					return Optional.of(handler);
				}
				return Optional.empty();
			});
	}
}
