package violet.dainty.features.recipeviewer.core.gui.recipes.layouts;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.common.config.RecipeSorterStage;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeLayoutWithButtons;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipesGui;
import violet.dainty.features.recipeviewer.core.gui.recipes.lookups.IFocusedRecipes;

public interface IRecipeLayoutList {
	static IRecipeLayoutList create(
		Set<RecipeSorterStage> recipeSorterStages,
		@Nullable AbstractContainerMenu container,
		IFocusedRecipes<?> selectedRecipes,
		IFocusGroup focusGroup,
		BookmarkList bookmarkList,
		IRecipeManager recipeManager,
		RecipesGui recipesGui
	) {
		return new LazyRecipeLayoutList<>(
			recipeSorterStages,
			container,
			selectedRecipes,
			bookmarkList,
			recipeManager,
			recipesGui,
			focusGroup
		);
	}

	int size();

	List<RecipeLayoutWithButtons<?>> subList(int from, int to);

	Optional<RecipeLayoutWithButtons<?>> findFirst();

	void tick(@Nullable AbstractContainerMenu container);
}
