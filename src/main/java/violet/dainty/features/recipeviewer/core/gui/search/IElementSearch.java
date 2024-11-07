package violet.dainty.features.recipeviewer.core.gui.search;

import java.util.Collection;
import java.util.Set;

import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElement;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElementInfo;

public interface IElementSearch {
	void add(IListElementInfo<?> info);

	void addAll(Collection<IListElementInfo<?>> infos);

	Collection<IListElement<?>> getAllIngredients();

	Set<IListElement<?>> getSearchResults(ElementPrefixParser.TokenInfo tokenInfo);

	void logStatistics();
}
