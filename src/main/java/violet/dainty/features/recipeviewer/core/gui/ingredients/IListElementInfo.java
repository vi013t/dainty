package violet.dainty.features.recipeviewer.core.gui.ingredients;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.jetbrains.annotations.Unmodifiable;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public interface IListElementInfo<V> {

	List<String> getNames();

	String getModNameForSorting();

	List<String> getModNames();

	List<String> getModIds();

	@Unmodifiable
	Set<String> getTooltipStrings(IIngredientFilterConfig config, IIngredientManager ingredientManager);

	Collection<String> getTagStrings(IIngredientManager ingredientManager);

	Stream<ResourceLocation> getTagIds(IIngredientManager ingredientManager);

	Iterable<Integer> getColors(IIngredientManager ingredientManager);

	@Unmodifiable
	Collection<String> getCreativeTabsStrings(IIngredientManager ingredientManager);

	ResourceLocation getResourceLocation();

	IListElement<V> getElement();

	ITypedIngredient<V> getTypedIngredient();

	int getCreatedIndex();
}
