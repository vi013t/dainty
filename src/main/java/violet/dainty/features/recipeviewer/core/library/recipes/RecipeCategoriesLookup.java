package violet.dainty.features.recipeviewer.core.library.recipes;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeCategoriesLookup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.focus.FocusGroup;

public class RecipeCategoriesLookup implements IRecipeCategoriesLookup {
	private final RecipeManagerInternal recipeManager;
	private final IIngredientManager ingredientManager;

	private boolean includeHidden = false;
	private Collection<RecipeType<?>> recipeTypes = List.of();
	private IFocusGroup focusGroup = FocusGroup.EMPTY;

	public RecipeCategoriesLookup(RecipeManagerInternal recipeManager, IIngredientManager ingredientManager) {
		this.recipeManager = recipeManager;
		this.ingredientManager = ingredientManager;
	}

	@Override
	public IRecipeCategoriesLookup limitTypes(Collection<RecipeType<?>> recipeTypes) {
		ErrorUtil.checkNotNull(recipeTypes, "recipeTypes");
		this.recipeTypes = recipeTypes;
		return this;
	}

	@Override
	public IRecipeCategoriesLookup limitFocus(Collection<? extends IFocus<?>> focuses) {
		ErrorUtil.checkNotNull(focuses, "focuses");
		this.focusGroup = FocusGroup.create(focuses, ingredientManager);
		return this;
	}

	@Override
	public IRecipeCategoriesLookup includeHidden() {
		this.includeHidden = true;
		return this;
	}

	@Override
	public Stream<IRecipeCategory<?>> get() {
		return recipeManager.getRecipeCategoriesForTypes(recipeTypes, focusGroup, includeHidden);
	}
}
