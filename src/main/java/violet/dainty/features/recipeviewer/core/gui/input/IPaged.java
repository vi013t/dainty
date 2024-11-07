package violet.dainty.features.recipeviewer.core.gui.input;

public interface IPaged {
	boolean nextPage();

	boolean previousPage();

	boolean hasNext();

	boolean hasPrevious();

	int getPageCount();

	int getPageNumber();
}
