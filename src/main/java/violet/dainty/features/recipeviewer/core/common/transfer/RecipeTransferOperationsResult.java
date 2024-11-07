package violet.dainty.features.recipeviewer.core.common.transfer;

import java.util.ArrayList;
import java.util.List;

import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;

public class RecipeTransferOperationsResult {
	/**
	 * map of "recipe target slot" to "source inventory slot"
	 */
	public final List<TransferOperation> results = new ArrayList<>();
	/**
	 * array of missing "required item stacks"
	 */
	public final List<IRecipeSlotView> missingItems = new ArrayList<>();
}
