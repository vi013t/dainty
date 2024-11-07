package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCategoryRegistration;
import violet.dainty.features.recipeviewer.core.library.runtime.JeiHelpers;

import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeCategoryRegistration implements IRecipeCategoryRegistration {
	private final List<IRecipeCategory<?>> recipeCategories = new ArrayList<>();
	private final Map<ResourceLocation, RecipeType<?>> recipeTypes = new HashMap<>();
	private final JeiHelpers jeiHelpers;

	public RecipeCategoryRegistration(JeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public void addRecipeCategories(IRecipeCategory<?>... recipeCategories) {
		ErrorUtil.checkNotEmpty(recipeCategories, "recipeCategories");

		for (IRecipeCategory<?> recipeCategory : recipeCategories) {
			RecipeType<?> recipeType = recipeCategory.getRecipeType();
			Preconditions.checkNotNull(recipeType, "Recipe type cannot be null %s", recipeCategory);
			ResourceLocation recipeTypeUid = recipeType.getUid();
			if (recipeTypes.containsKey(recipeTypeUid)) {
				RecipeType<?> existing = recipeTypes.get(recipeTypeUid);
				throw new IllegalArgumentException("Tried to register a recipe type \"" + recipeType + "\" but there is already one registered with the same UID: " + existing);
			} else {
				recipeTypes.put(recipeTypeUid, recipeType);
			}
			Preconditions.checkArgument(recipeCategory.getWidth() > 0, "Width must be greater than 0");
			Preconditions.checkArgument(recipeCategory.getHeight() > 0, "Height must be greater than 0");
		}

		Collections.addAll(this.recipeCategories, recipeCategories);
		this.jeiHelpers.setRecipeCategories(Collections.unmodifiableCollection(this.recipeCategories));
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Unmodifiable
	public List<IRecipeCategory<?>> getRecipeCategories() {
		return List.copyOf(recipeCategories);
	}
}
