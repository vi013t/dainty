package violet.dainty.features.recipeviewer.core.library.recipes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import violet.dainty.features.recipeviewer.core.common.Constants;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IUniversalRecipeTransferHandler;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UniversalRecipeTransferHandlerAdapter<C extends AbstractContainerMenu, R> implements IRecipeTransferHandler<C, R> {
	private final IUniversalRecipeTransferHandler<C> universalRecipeTransferHandler;

	public UniversalRecipeTransferHandlerAdapter(IUniversalRecipeTransferHandler<C> universalRecipeTransferHandler) {
		this.universalRecipeTransferHandler = universalRecipeTransferHandler;
	}

	@Override
	public Class<? extends C> getContainerClass() {
		return universalRecipeTransferHandler.getContainerClass();
	}

	@Override
	public Optional<MenuType<C>> getMenuType() {
		return universalRecipeTransferHandler.getMenuType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType<R> getRecipeType() {
		return (RecipeType<R>) Constants.UNIVERSAL_RECIPE_TRANSFER_TYPE;
	}

	@Override
	public @Nullable IRecipeTransferError transferRecipe(C container, R recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		return universalRecipeTransferHandler.transferRecipe(container, recipe, recipeSlots, player, maxTransfer, doTransfer);
	}
}
