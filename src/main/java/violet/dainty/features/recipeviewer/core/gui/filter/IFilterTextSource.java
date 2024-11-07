package violet.dainty.features.recipeviewer.core.gui.filter;

public interface IFilterTextSource {
	String getFilterText();

	boolean setFilterText(String filterText);

	void addListener(Listener listener);

	@FunctionalInterface
	interface Listener {
		void onChange(String filterText);
	}
}
