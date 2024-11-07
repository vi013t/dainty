package violet.dainty.features.recipeviewer.core.library.load.registration;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPluginHelper;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeManagerInternal;

public class RecipeManagerPluginHelper implements IRecipeManagerPluginHelper {
	private final RecipeManagerInternal recipeManager;

	public RecipeManagerPluginHelper(RecipeManagerInternal recipeManager) {
		this.recipeManager = recipeManager;
	}

	@Override
	public boolean isRecipeCatalyst(RecipeType<?> recipeType, IFocus<?> focus) {
		return recipeManager.isRecipeCatalyst(recipeType, focus);
	}
}
