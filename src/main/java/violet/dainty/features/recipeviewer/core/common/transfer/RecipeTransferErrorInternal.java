package violet.dainty.features.recipeviewer.core.common.transfer;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;

public class RecipeTransferErrorInternal implements IRecipeTransferError {
	public static final RecipeTransferErrorInternal INSTANCE = new RecipeTransferErrorInternal();

	private RecipeTransferErrorInternal() {

	}

	@Override
	public Type getType() {
		return Type.INTERNAL;
	}
}
