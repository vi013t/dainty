package violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientAcceptor;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class IngredientAcceptorVoid implements IIngredientAcceptor<IngredientAcceptorVoid> {
	public static final IngredientAcceptorVoid INSTANCE = new IngredientAcceptorVoid();

	private IngredientAcceptorVoid() {}

	@Override
	public IngredientAcceptorVoid addIngredientsUnsafe(List<?> ingredients) {
		return this;
	}

	@Override
	public <I> IngredientAcceptorVoid addIngredients(IIngredientType<I> ingredientType, List<@Nullable I> ingredients) {
		return this;
	}

	@Override
	public <I> IngredientAcceptorVoid addIngredient(IIngredientType<I> ingredientType, I ingredient) {
		return this;
	}

	@Override
	public IngredientAcceptorVoid addTypedIngredients(List<ITypedIngredient<?>> ingredients) {
		return this;
	}

	@Override
	public IngredientAcceptorVoid addOptionalTypedIngredients(List<Optional<ITypedIngredient<?>>> ingredients) {
		return this;
	}

	@Override
	public IngredientAcceptorVoid addFluidStack(Fluid fluid) {
		return this;
	}

	@Override
	public IngredientAcceptorVoid addFluidStack(Fluid fluid, long amount) {
		return this;
	}

	@Override
	public IngredientAcceptorVoid addFluidStack(Fluid fluid, long amount, DataComponentPatch componentPatch) {
		return this;
	}
}
