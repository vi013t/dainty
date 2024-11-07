package violet.dainty.features.recipeviewer.core.library.transfer;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketRecipeTransfer;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferOperationsResult;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferUtil;
import violet.dainty.features.recipeviewer.core.common.util.StringUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IStackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandler;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandlerHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BasicRecipeTransferHandler<C extends AbstractContainerMenu, R> implements IRecipeTransferHandler<C, R> {
	private static final Logger LOGGER = LogManager.getLogger();

	private final IConnectionToServer serverConnection;
	private final IStackHelper stackHelper;
	private final IRecipeTransferHandlerHelper handlerHelper;
	private final IRecipeTransferInfo<C, R> transferInfo;

	public BasicRecipeTransferHandler(
		IConnectionToServer serverConnection,
		IStackHelper stackHelper,
		IRecipeTransferHandlerHelper handlerHelper,
		IRecipeTransferInfo<C, R> transferInfo
	) {
		this.serverConnection = serverConnection;
		this.stackHelper = stackHelper;
		this.handlerHelper = handlerHelper;
		this.transferInfo = transferInfo;
	}

	@Override
	public Class<? extends C> getContainerClass() {
		return transferInfo.getContainerClass();
	}

	@Override
	public Optional<MenuType<C>> getMenuType() {
		return transferInfo.getMenuType();
	}

	@Override
	public RecipeType<R> getRecipeType() {
		return transferInfo.getRecipeType();
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(C container, R recipe, IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
		if (!serverConnection.isJeiOnServer()) {
			Component tooltipMessage = Component.translatable("dainty.tooltip.error.recipe.transfer.no.server");
			return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
		}

		if (!transferInfo.canHandle(container, recipe)) {
			IRecipeTransferError handlingError = transferInfo.getHandlingError(container, recipe);
			if (handlingError != null) {
				return handlingError;
			}
			return handlerHelper.createInternalError();
		}

		List<Slot> craftingSlots = Collections.unmodifiableList(transferInfo.getRecipeSlots(container, recipe));
		List<Slot> inventorySlots = Collections.unmodifiableList(transferInfo.getInventorySlots(container, recipe));
		if (!validateTransferInfo(transferInfo, container, craftingSlots, inventorySlots)) {
			return handlerHelper.createInternalError();
		}

		List<IRecipeSlotView> inputItemSlotViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
		if (!validateRecipeView(transferInfo, container, craftingSlots, inputItemSlotViews)) {
			return handlerHelper.createInternalError();
		}

		InventoryState inventoryState = getInventoryState(craftingSlots, inventorySlots, player, container, transferInfo);
		if (inventoryState == null) {
			return handlerHelper.createInternalError();
		}

		// check if we have enough inventory space to shuffle items around to their final locations
		int inputCount = inputItemSlotViews.size();
		if (!inventoryState.hasRoom(inputCount)) {
			Component message = Component.translatable("dainty.tooltip.error.recipe.transfer.inventory.full");
			return handlerHelper.createUserErrorWithTooltip(message);
		}

		RecipeTransferOperationsResult transferOperations = RecipeTransferUtil.getRecipeTransferOperations(
			stackHelper,
			inventoryState.availableItemStacks,
			inputItemSlotViews,
			craftingSlots
		);

		if (!transferOperations.missingItems.isEmpty()) {
			Component message = Component.translatable("dainty.tooltip.error.recipe.transfer.missing");
			return handlerHelper.createUserErrorForMissingSlots(message, transferOperations.missingItems);
		}

		if (!RecipeTransferUtil.validateSlots(player, transferOperations.results, craftingSlots, inventorySlots)) {
			return handlerHelper.createInternalError();
		}

		if (doTransfer) {
			boolean requireCompleteSets = transferInfo.requireCompleteSets(container, recipe);
			PacketRecipeTransfer packet = PacketRecipeTransfer.fromSlots(
				transferOperations.results,
				craftingSlots,
				inventorySlots,
				maxTransfer,
				requireCompleteSets
			);
			serverConnection.sendPacketToServer(packet);
		}

		return null;
	}

	public static <C extends AbstractContainerMenu, R> boolean validateTransferInfo(
		IRecipeTransferInfo<C, R> transferInfo,
		C container,
		List<Slot> craftingSlots,
		List<Slot> inventorySlots
	) {
		for (Slot slot : craftingSlots) {
			if (slot.isFake()) {
				LOGGER.error("Recipe Transfer helper {} does not work for container {}. " +
						"The Recipe Transfer Helper references crafting slot index [{}] but it is a fake (output) slot, which is not allowed.",
					transferInfo.getClass(), container.getClass(), slot.index
				);
				return false;
			}
		}
		for (Slot slot : inventorySlots) {
			if (slot.isFake()) {
				LOGGER.error("Recipe Transfer helper {} does not work for container {}. " +
						"The Recipe Transfer Helper references inventory slot index [{}] but it is a fake (output) slot, which is not allowed.",
					transferInfo.getClass(), container.getClass(), slot.index
				);
				return false;
			}
		}
		Collection<Integer> craftingSlotIndexes = slotIndexes(craftingSlots);
		Collection<Integer> inventorySlotIndexes = slotIndexes(inventorySlots);
		Collection<Integer> containerSlotIndexes = slotIndexes(container.slots);

		if (!containerSlotIndexes.containsAll(craftingSlotIndexes)) {
			LOGGER.error("Recipe Transfer helper {} does not work for container {}. " +
					"The Recipes Transfer Helper references crafting slot indexes [{}] that are not found in the inventory container slots [{}]",
				transferInfo.getClass(), container.getClass(), StringUtil.intsToString(craftingSlotIndexes), StringUtil.intsToString(containerSlotIndexes)
			);
			return false;
		}

		if (!containerSlotIndexes.containsAll(inventorySlotIndexes)) {
			LOGGER.error("Recipe Transfer helper {} does not work for container {}. " +
					"The Recipes Transfer Helper references inventory slot indexes [{}] that are not found in the inventory container slots [{}]",
				transferInfo.getClass(), container.getClass(), StringUtil.intsToString(inventorySlotIndexes), StringUtil.intsToString(containerSlotIndexes)
			);
			return false;
		}

		return true;
	}

	public static <C extends AbstractContainerMenu, R> boolean validateRecipeView(
		IRecipeTransferInfo<C, R> transferInfo,
		C container,
		List<Slot> craftingSlots,
		List<IRecipeSlotView> inputSlots
	) {
		if (inputSlots.size() > craftingSlots.size()) {
			LOGGER.error("Recipe View {} does not work for container {}. " +
					"The Recipe View has more input slots ({}) than the number of inventory crafting slots ({})",
				transferInfo.getClass(), container.getClass(), inputSlots.size(), craftingSlots.size()
			);
			return false;
		}

		return true;
	}

	public static Set<Integer> slotIndexes(Collection<Slot> slots) {
		Set<Integer> set = new IntOpenHashSet(slots.size());
		for (Slot s : slots) {
			set.add(s.index);
		}
		return set;
	}

	@Nullable
	public static <C extends AbstractContainerMenu, R> InventoryState getInventoryState(
		Collection<Slot> craftingSlots,
		Collection<Slot> inventorySlots,
		Player player,
		C container,
		IRecipeTransferInfo<C, R> transferInfo
	) {
		Map<Slot, ItemStack> availableItemStacks = new HashMap<>();
		int filledCraftSlotCount = 0;
		int emptySlotCount = 0;

		for (Slot slot : craftingSlots) {
			final ItemStack stack = slot.getItem();
			if (!stack.isEmpty()) {
				if (!slot.allowModification(player)) {
					LOGGER.error(
						"Recipe Transfer helper {} does not work for container {}. " +
							"The Player is not able to move items out of Crafting Slot number {}",
						transferInfo.getClass(), container.getClass(), slot.index
					);
					return null;
				}
				filledCraftSlotCount++;
				availableItemStacks.put(slot, stack.copy());
			}
		}

		for (Slot slot : inventorySlots) {
			final ItemStack stack = slot.getItem();
			if (!stack.isEmpty()) {
				if (!slot.allowModification(player)) {
					LOGGER.error(
						"Recipe Transfer helper {} does not work for container {}. " +
							"The Player is not able to move items out of Inventory Slot number {}",
						transferInfo.getClass(), container.getClass(), slot.index
					);
					return null;
				}
				availableItemStacks.put(slot, stack.copy());
			} else {
				emptySlotCount++;
			}
		}

		return new InventoryState(availableItemStacks, filledCraftSlotCount, emptySlotCount);
	}

	public record InventoryState(
		Map<Slot, ItemStack> availableItemStacks,
		int filledCraftSlotCount,
		int emptySlotCount
	) {
		/**
		 * check if we have enough inventory space to shuffle items around to their final locations
		 */
		public boolean hasRoom(int inputCount) {
			return filledCraftSlotCount - inputCount <= emptySlotCount;
		}
	}
}
