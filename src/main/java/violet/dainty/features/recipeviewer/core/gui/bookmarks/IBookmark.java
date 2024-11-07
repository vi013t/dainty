package violet.dainty.features.recipeviewer.core.gui.bookmarks;

import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;

public interface IBookmark {
	BookmarkType getType();
	IElement<?> getElement();
	boolean isVisible();
	void setVisible(boolean visible);
}
