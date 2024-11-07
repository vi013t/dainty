package violet.dainty.features.recipeviewer.core.gui.recipes.layouts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.RecipeSorterStage;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.RecipeBookmark;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeBookmarkButton;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeLayoutWithButtons;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeSortUtil;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeTransferButton;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipesGui;
import violet.dainty.features.recipeviewer.core.gui.recipes.lookups.IFocusedRecipes;

public class LazyRecipeLayoutList<T> implements IRecipeLayoutList {
	private final IRecipeManager recipeManager;
	private final IRecipeCategory<T> recipeCategory;
	private final RecipesGui recipesGui;
	private final IFocusGroup focusGroup;
	private final List<RecipeLayoutWithButtons<?>> results;
	private final List<RecipeLayoutWithButtons<?>> craftMissing;
	private final Iterator<T> unsortedIterator;
	private final int size;

	private final boolean matchingCraftable;
	private final BookmarkList bookmarkList;

	public LazyRecipeLayoutList(
		Set<RecipeSorterStage> recipeSorterStages,
		@Nullable AbstractContainerMenu container,
		IFocusedRecipes<T> selectedRecipes,
		BookmarkList bookmarkList,
		IRecipeManager recipeManager,
		RecipesGui recipesGui,
		IFocusGroup focusGroup
	) {
		this.bookmarkList = bookmarkList;
		boolean matchingBookmarks = recipeSorterStages.contains(RecipeSorterStage.BOOKMARKED);
		boolean matchingCraftable = recipeSorterStages.contains(RecipeSorterStage.CRAFTABLE);
		this.recipeManager = recipeManager;
		this.recipesGui = recipesGui;
		this.focusGroup = focusGroup;
		this.results = new ArrayList<>();
		this.craftMissing = new ArrayList<>();
		this.recipeCategory = selectedRecipes.getRecipeCategory();

		List<T> recipes = selectedRecipes.getRecipes();
		this.size = recipes.size();

		if (matchingCraftable && container != null) {
			IRecipeTransferManager recipeTransferManager = Internal.getJeiRuntime().getRecipeTransferManager();
			this.matchingCraftable = recipeTransferManager.getRecipeTransferHandler(container, recipeCategory).isPresent();
		} else {
			this.matchingCraftable = false;
		}

		if (matchingBookmarks) {
			// if bookmarks go first, start by grabbing all the bookmarked elements, it's relatively cheap
			RecipeType<T> recipeType = recipeCategory.getRecipeType();

			recipes = new ArrayList<>(recipes);
			Iterator<T> iterator = recipes.iterator();
			while (iterator.hasNext()) {
				T recipe = iterator.next();
				RecipeBookmark<T, ?> recipeBookmark = bookmarkList.getMatchingBookmark(recipeType, recipe);
				if (recipeBookmark != null) {
					IRecipeLayoutDrawable<T> recipeLayout = recipeManager.createRecipeLayoutDrawableOrShowError(recipeCategory, recipe, focusGroup);
					RecipeLayoutWithButtons<T> recipeLayoutWithButtons = createRecipeLayoutWithButtons(recipeLayout, recipeBookmark, bookmarkList, recipesGui, container);
					results.add(recipeLayoutWithButtons);
					iterator.remove();
				}
			}
		}

		this.unsortedIterator = recipes.iterator();
	}

	private static <T> RecipeLayoutWithButtons<T> createRecipeLayoutWithButtons(
		IRecipeLayoutDrawable<T> recipeLayoutDrawable,
		@Nullable RecipeBookmark<?, ?> recipeBookmark,
		BookmarkList bookmarks,
		RecipesGui recipesGui,
		@Nullable AbstractContainerMenu container
	) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		RecipeTransferButton transferButton = RecipeTransferButton.create(recipeLayoutDrawable, recipesGui::onClose, container, player);
		RecipeBookmarkButton bookmarkButton = RecipeBookmarkButton.create(recipeLayoutDrawable, bookmarks, recipeBookmark);
		return new RecipeLayoutWithButtons<>(recipeLayoutDrawable, transferButton, bookmarkButton);
	}

	private IRecipeLayoutDrawable<T> createRecipeLayout(T recipe) {
		return recipeManager.createRecipeLayoutDrawableOrShowError(recipeCategory, recipe, focusGroup);
	}

	private RecipeLayoutWithButtons<T> createRecipeLayoutWithButtons(
		IRecipeLayoutDrawable<T> recipeLayoutDrawable,
		IIngredientManager ingredientManager,
		@Nullable AbstractContainerMenu container
	) {
		RecipeBookmark<T, ?> recipeBookmark = RecipeBookmark.create(recipeLayoutDrawable, ingredientManager);
		return createRecipeLayoutWithButtons(recipeLayoutDrawable, recipeBookmark, bookmarkList, recipesGui, container);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<RecipeLayoutWithButtons<?>> subList(int from, int to) {
		ensureResults(to - 1);
		return results.subList(from, to);
	}

	private void ensureResults(int index) {
		AbstractContainerMenu container = recipesGui.getParentContainerMenu();
		while (index >= results.size()) {
			if (!calculateNextResult(container)) {
				return;
			}
		}
	}

	@Override
	public Optional<RecipeLayoutWithButtons<?>> findFirst() {
		ensureResults(0);
		if (results.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(results.getFirst());
	}

	@Override
	public void tick(@Nullable AbstractContainerMenu container) {
		calculateNextResult(container);
	}

	private boolean calculateNextResult(@Nullable AbstractContainerMenu container) {
		IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
		IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();

		while (unsortedIterator.hasNext()) {
			T recipe = unsortedIterator.next();
			IRecipeLayoutDrawable<T> recipeLayout = createRecipeLayout(recipe);
			RecipeLayoutWithButtons<T> recipeLayoutWithButtons = createRecipeLayoutWithButtons(recipeLayout, ingredientManager, container);
			RecipeTransferButton transferButton = recipeLayoutWithButtons.transferButton();

			if (matchingCraftable) {
				// if craftables go first, look for a 100% craftable element
				int missingCountHint = transferButton.getMissingCountHint();
				if (missingCountHint == 0) {
					results.add(recipeLayoutWithButtons);
					return true;
				} else {
					craftMissing.add(recipeLayoutWithButtons);
				}
			} else {
				results.add(recipeLayoutWithButtons);
				return true;
			}
		}

		// from here we're finished with calculating all the transfer handlers,
		// just sort and add everything left to the results
		if (!craftMissing.isEmpty()) {
			craftMissing.sort(RecipeSortUtil.getCraftableComparator());
			results.addAll(craftMissing);
			craftMissing.clear();
			return true;
		}

		return false;
	}
}
