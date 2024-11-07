package violet.dainty.features.recipeviewer.core.library.gui.recipes;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;

import java.util.List;
import java.util.Optional;

public class RecipesGuiDummy implements IRecipesGui {
	public static final IRecipesGui INSTANCE = new RecipesGuiDummy();

	public RecipesGuiDummy() {

	}

	@Override
	public void show(List<IFocus<?>> focuses) {

	}

	@Override
	public void showTypes(List<RecipeType<?>> recipeTypes) {

	}

	@Override
	public <T> void showRecipes(IRecipeCategory<T> recipeCategory, List<T> recipes, List<IFocus<?>> focuses) {

	}

	@Override
	public <T> Optional<T> getIngredientUnderMouse(IIngredientType<T> ingredientType) {
		return Optional.empty();
	}

	@Override
	public Optional<Screen> getParentScreen() {
		return Optional.empty();
	}
}
