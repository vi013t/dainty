package violet.dainty.features.recipeviewer.core.common.platform;


public interface IPlatformHelper {
	IPlatformItemStackHelper getItemStackHelper();

	IPlatformFluidHelperInternal<?> getFluidHelper();

	IPlatformRenderHelper getRenderHelper();

	IPlatformRecipeHelper getRecipeHelper();

	IPlatformConfigHelper getConfigHelper();

	IPlatformInputHelper getInputHelper();

	IPlatformScreenHelper getScreenHelper();

	IPlatformIngredientHelper getIngredientHelper();

	IPlatformModHelper getModHelper();
}