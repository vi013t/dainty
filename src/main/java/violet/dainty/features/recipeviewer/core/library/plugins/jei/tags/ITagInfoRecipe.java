package violet.dainty.features.recipeviewer.core.library.plugins.jei.tags;

import java.util.List;

import net.minecraft.tags.TagKey;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public interface ITagInfoRecipe {
	TagKey<?> getTag();

	List<ITypedIngredient<?>> getTypedIngredients();
}
