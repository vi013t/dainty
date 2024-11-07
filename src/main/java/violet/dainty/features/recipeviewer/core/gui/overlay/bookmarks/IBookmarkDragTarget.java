package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;

public interface IBookmarkDragTarget {
	ImmutableRect2i getArea();
	void accept(IBookmark bookmark);
}
