package violet.dainty.features.recipeviewer.core.gui.overlay;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;

import java.util.List;

public interface IIngredientGridSource {
	@Unmodifiable
	List<IElement<?>> getElements();

	void addSourceListChangedListener(SourceListChangedListener listener);

	interface SourceListChangedListener {
		void onSourceListChanged();
	}
}
