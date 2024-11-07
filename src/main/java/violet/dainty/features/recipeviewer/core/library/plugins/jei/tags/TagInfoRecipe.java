package violet.dainty.features.recipeviewer.core.library.plugins.jei.tags;

import java.util.Collections;
import java.util.List;

import net.minecraft.tags.TagKey;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public class TagInfoRecipe<B, I> implements ITagInfoRecipe {
	private final TagKey<B> tag;
	private final List<ITypedIngredient<I>> ingredients;

	public TagInfoRecipe(TagKey<B> tag, List<ITypedIngredient<I>> ingredients) {
		this.tag = tag;
		this.ingredients = ingredients;
	}

	@Override
	public TagKey<B> getTag() {
		return tag;
	}

	@Override
	public List<ITypedIngredient<?>> getTypedIngredients() {
		return Collections.unmodifiableList(ingredients);
	}
}
