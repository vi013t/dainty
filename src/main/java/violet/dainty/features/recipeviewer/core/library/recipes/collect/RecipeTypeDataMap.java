package violet.dainty.features.recipeviewer.core.library.recipes.collect;

import com.google.common.collect.ImmutableListMultimap;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeTypeDataMap {
	@Unmodifiable
	private final Map<RecipeType<?>, RecipeTypeData<?>> uidMap;

	public RecipeTypeDataMap(
		List<IRecipeCategory<?>> recipeCategories,
		ImmutableListMultimap<IRecipeCategory<?>, ITypedIngredient<?>> recipeCategoryCatalystsMap
	) {
		this.uidMap = recipeCategories.stream()
			.collect(
				Collectors.toUnmodifiableMap(
					IRecipeCategory::getRecipeType,
					recipeCategory -> {
						List<ITypedIngredient<?>> catalysts = recipeCategoryCatalystsMap.get(recipeCategory);
						return new RecipeTypeData<>(recipeCategory, catalysts);
					}
				)
			);
	}

	public <T> RecipeTypeData<T> get(RecipeType<T> recipeType) {
		RecipeTypeData<?> data = this.uidMap.get(recipeType);
		if (data == null) {
			throw new IllegalStateException(
				"There is no recipe category registered for: " + recipeType +
				"\nA recipe category must be registered in order to use this recipe type."
			);
		}
		@SuppressWarnings("unchecked")
		RecipeTypeData<T> recipeTypeData = (RecipeTypeData<T>) data;
		return recipeTypeData;
	}

	public void validate(RecipeType<?> recipeType) {
		if (!uidMap.containsKey(recipeType)) {
			throw new IllegalStateException("There is no recipe type registered for: " + recipeType);
		}
	}

	public Optional<RecipeType<?>> getType(ResourceLocation recipeTypeUid) {
		return uidMap.keySet()
			.stream()
			.filter(recipeType -> recipeType.getUid().equals(recipeTypeUid))
			.findFirst();
	}

	public <T> Optional<RecipeType<T>> getType(ResourceLocation recipeTypeUid, Class<? extends T> recipeClass) {
		return uidMap.keySet()
			.stream()
			.filter(recipeType -> recipeType.getUid().equals(recipeTypeUid) && recipeType.getRecipeClass().equals(recipeClass))
			.map(recipeType -> {
				@SuppressWarnings("unchecked")
				RecipeType<T> castRecipeType = (RecipeType<T>) recipeType;
				return castRecipeType;
			})
			.findFirst();
	}
}
