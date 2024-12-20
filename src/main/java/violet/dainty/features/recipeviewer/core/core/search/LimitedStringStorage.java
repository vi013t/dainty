package violet.dainty.features.recipeviewer.core.core.search;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Consumer;

import violet.dainty.features.recipeviewer.core.core.collect.SetMultiMap;
import violet.dainty.features.recipeviewer.core.core.search.suffixtree.GeneralizedSuffixTree;

/**
 * This is more memory-efficient than {@link GeneralizedSuffixTree}
 * when there are many values for each key.
 *
 * It stores a map of keys to a set of values.
 * The set values are shared with the internal {@link GeneralizedSuffixTree} to index and find them.
 * The sets values are modified directly when values with the same key are added.
 */
public class LimitedStringStorage<T> implements ISearchStorage<T> {
	private final SetMultiMap<String, T> multiMap = new SetMultiMap<>(() -> Collections.newSetFromMap(new IdentityHashMap<>()));
	private final GeneralizedSuffixTree<Set<T>> generalizedSuffixTree = new GeneralizedSuffixTree<>();

	@Override
	public void getSearchResults(String token, Consumer<Collection<T>> resultsConsumer) {
		generalizedSuffixTree.getSearchResults(token, resultSet -> {
			for (Collection<T> result : resultSet) {
				resultsConsumer.accept(result);
			}
		});
	}

	@Override
	public void getAllElements(Consumer<Collection<T>> resultsConsumer) {
		Collection<T> values = multiMap.allValues();
		resultsConsumer.accept(values);
	}

	@Override
	public void put(String key, T value) {
		boolean isNewKey = !multiMap.containsKey(key);
		multiMap.put(key, value);
		if (isNewKey) {
			Set<T> set = multiMap.get(key);
			generalizedSuffixTree.put(key, set);
		}
	}

	@Override
	public String statistics() {
		return "LimitedStringStorage: " + generalizedSuffixTree.statistics();
	}
}
