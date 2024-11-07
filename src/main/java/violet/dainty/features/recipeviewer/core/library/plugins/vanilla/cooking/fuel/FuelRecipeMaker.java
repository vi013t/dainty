package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.fuel;

import java.util.Comparator;
import java.util.List;

import violet.dainty.features.recipeviewer.core.common.platform.IPlatformItemStackHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiFuelingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public final class FuelRecipeMaker {

	private FuelRecipeMaker() {
	}

	public static List<IJeiFuelingRecipe> getFuelRecipes(IIngredientManager ingredientManager) {
		IPlatformItemStackHelper itemStackHelper = Services.PLATFORM.getItemStackHelper();
		return ingredientManager.getAllItemStacks().stream()
			.<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
				int burnTime = itemStackHelper.getBurnTime(stack);
				if (burnTime > 0) {
					consumer.accept(new FuelingRecipe(List.of(stack), burnTime));
				}
			})
			.sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
			.toList();
	}
}
