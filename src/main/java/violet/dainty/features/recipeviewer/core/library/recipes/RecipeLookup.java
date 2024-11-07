package violet.dainty.features.recipeviewer.core.library.recipes;

import java.util.Collection;
import java.util.stream.Stream;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeLookup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.focus.FocusGroup;

public class RecipeLookup<R> implements IRecipeLookup<R> {
	private final RecipeType<R> recipeType;
	private final RecipeManagerInternal recipeManager;
	private final IIngredientManager ingredientManager;

	private boolean includeHidden = false;
	private IFocusGroup focusGroup = FocusGroup.EMPTY;

	public RecipeLookup(RecipeType<R> recipeType, RecipeManagerInternal recipeManager, IIngredientManager ingredientManager) {
		this.recipeType = recipeType;
		this.recipeManager = recipeManager;
		this.ingredientManager = ingredientManager;
	}

	@Override
	public IRecipeLookup<R> limitFocus(Collection<? extends IFocus<?>> focuses) {
		this.focusGroup = FocusGroup.create(focuses, ingredientManager);
		return this;
	}

	@Override
	public IRecipeLookup<R> includeHidden() {
		this.includeHidden = true;
		return this;
	}

	@Override
	public Stream<R> get() {
		return recipeManager.getRecipesStream(recipeType, focusGroup, includeHidden);
	}
}
