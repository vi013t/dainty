package violet.dainty.features.recipeviewer.core.gui.config;

import net.minecraft.core.RegistryAccess;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;

import java.util.List;

public interface IBookmarkConfig {
	boolean saveBookmarks(IRecipeManager recipeManager, IFocusFactory focusFactory, IGuiHelper guiHelper, IIngredientManager ingredientManager, RegistryAccess registryAccess, ICodecHelper codecHelper, List<IBookmark> bookmarks);

	void loadBookmarks(IRecipeManager recipeManager, IFocusFactory focusFactory, IGuiHelper guiHelper, IIngredientManager ingredientManager, RegistryAccess registryAccess, BookmarkList bookmarkList, ICodecHelper codecHelper);
}
