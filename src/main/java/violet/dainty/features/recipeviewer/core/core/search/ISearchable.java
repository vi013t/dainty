package violet.dainty.features.recipeviewer.core.core.search;

import java.util.Collection;
import java.util.function.Consumer;

public interface ISearchable<T> {
	void getSearchResults(String token, Consumer<Collection<T>> resultsConsumer);

	void getAllElements(Consumer<Collection<T>> resultsConsumer);

	default SearchMode getMode() {
		return SearchMode.ENABLED;
	}
}
