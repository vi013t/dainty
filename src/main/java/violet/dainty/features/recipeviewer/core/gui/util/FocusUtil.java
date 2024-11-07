package violet.dainty.features.recipeviewer.core.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class FocusUtil {
	private final IFocusFactory focusFactory;
	private final IClientConfig clientConfig;
	private final IIngredientManager ingredientManager;

	public FocusUtil(IFocusFactory focusFactory, IClientConfig clientConfig, IIngredientManager ingredientManager) {
		this.focusFactory = focusFactory;
		this.clientConfig = clientConfig;
		this.ingredientManager = ingredientManager;
	}

	public List<IFocus<?>> createFocuses(ITypedIngredient<?> ingredient, List<RecipeIngredientRole> roles) {
		List<ITypedIngredient<?>> ingredients = new ArrayList<>();
		ingredients.add(ingredient);

		if (clientConfig.isLookupFluidContentsEnabled()) {
			IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
			getContainedFluid(fluidHelper, ingredient)
				.ifPresent(ingredients::add);
		}

		return roles.stream()
			.<IFocus<?>>flatMap(role ->
				ingredients.stream()
					.map(i -> focusFactory.createFocus(role, i))
			)
			.toList();
	}

	private <T> Optional<ITypedIngredient<T>> getContainedFluid(IPlatformFluidHelperInternal<T> fluidHelper, ITypedIngredient<?> ingredient) {
		return fluidHelper.getContainedFluid(ingredient)
			.flatMap(fluid -> {
				IIngredientTypeWithSubtypes<Fluid, T> type = fluidHelper.getFluidIngredientType();
				return ingredientManager.createTypedIngredient(type, fluid);
			});
	}
}
