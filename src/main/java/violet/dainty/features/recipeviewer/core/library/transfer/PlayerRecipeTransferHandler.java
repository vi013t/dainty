package violet.dainty.features.recipeviewer.core.library.transfer;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandlerHelper;

public class PlayerRecipeTransferHandler implements IRecipeTransferHandler<InventoryMenu, RecipeHolder<CraftingRecipe>> {
	/**
	 * Indexes from the crafting recipe inputs that fit into the player crafting grid
	 * when we trim the right and bottom edges.
	 */
	private static final IntSet PLAYER_INV_INDEXES = IntArraySet.of(0, 1, 3, 4);

	private final IRecipeTransferHandlerHelper handlerHelper;
	private final IRecipeTransferHandler<InventoryMenu, RecipeHolder<CraftingRecipe>> handler;

	public PlayerRecipeTransferHandler(IRecipeTransferHandlerHelper handlerHelper) {
		this.handlerHelper = handlerHelper;
		var basicRecipeTransferInfo = handlerHelper.createBasicRecipeTransferInfo(InventoryMenu.class, null, RecipeTypes.CRAFTING, 1, 4, 9, 36);
		this.handler = handlerHelper.createUnregisteredRecipeTransferHandler(basicRecipeTransferInfo);
	}

	@Override
	public Class<? extends InventoryMenu> getContainerClass() {
		return handler.getContainerClass();
	}

	@Override
	public Optional<MenuType<InventoryMenu>> getMenuType() {
		return handler.getMenuType();
	}

	@Override
	public RecipeType<RecipeHolder<CraftingRecipe>> getRecipeType() {
		return RecipeTypes.CRAFTING;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(InventoryMenu container, RecipeHolder<CraftingRecipe> recipe, IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
		if (!handlerHelper.recipeTransferHasServerSupport()) {
			Component tooltipMessage = Component.translatable("dainty.tooltip.error.recipe.transfer.no.server");
			return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
		}

		List<IRecipeSlotView> slotViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
		if (!validateIngredientsOutsidePlayerGridAreEmpty(slotViews)) {
			Component tooltipMessage = Component.translatable(
				"dainty.tooltip.error.recipe.transfer.too.large.player.inventory"
			);
			return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
		}

		// filter the crafting table input slots to player inventory input slots
		List<IRecipeSlotView> filteredSlotViews = filterSlots(slotViews);
		IRecipeSlotsView filteredRecipeSlots = this.handlerHelper.createRecipeSlotsView(filteredSlotViews);
		return this.handler.transferRecipe(container, recipe, filteredRecipeSlots, player, maxTransfer, doTransfer);
	}

	private static boolean validateIngredientsOutsidePlayerGridAreEmpty(List<IRecipeSlotView> slotViews) {
		int bound = slotViews.size();
		for (int i = 0; i < bound; i++) {
			if (!PLAYER_INV_INDEXES.contains(i)) {
				IRecipeSlotView slotView = slotViews.get(i);
				if (!slotView.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	private static List<IRecipeSlotView> filterSlots(List<IRecipeSlotView> slotViews) {
		return PLAYER_INV_INDEXES.intStream()
			.mapToObj(slotViews::get)
			.toList();
	}
}
