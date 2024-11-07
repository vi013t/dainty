package violet.dainty.features.recipeviewer.core.gui.recipes;

import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.recipes.lookups.IFocusedRecipes;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Stream;

public interface IRecipeGuiLogic {

	String getPageString();

	boolean hasMultipleCategories();

	boolean hasAllCategories();

	void previousRecipeCategory();

	int getRecipesPerPage();

	void nextRecipeCategory();

	void setRecipeCategory(IRecipeCategory<?> category);

	boolean hasMultiplePages();

	void goToFirstPage();

	void previousPage();

	void nextPage();

	void tick(@Nullable AbstractContainerMenu container);

	boolean showFocus(IFocusGroup focuses);

	boolean showRecipes(IFocusedRecipes<?> recipes, IFocusGroup focuses);

	boolean back();

	void clearHistory();

	boolean showAllRecipes();

	boolean showCategories(List<RecipeType<?>> recipeTypes);

	IRecipeCategory<?> getSelectedRecipeCategory();

	@Unmodifiable
	List<IRecipeCategory<?>> getRecipeCategories();

	Stream<ITypedIngredient<?>> getRecipeCatalysts();
	Stream<ITypedIngredient<?>> getRecipeCatalysts(IRecipeCategory<?> recipeCategory);

	List<RecipeLayoutWithButtons<?>> getVisibleRecipeLayoutsWithButtons(
		int availableHeight,
		int minRecipePadding,
		@Nullable AbstractContainerMenu container,
		BookmarkList bookmarkList,
		RecipesGui recipesGui
	);
}
