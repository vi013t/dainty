package violet.dainty.features.recipeviewer.core.gui.recipes;

import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;

public class RecipeSortUtil {
	private static final Comparator<RecipeLayoutWithButtons<?>> CRAFTABLE_COMPARATOR = createCraftableComparator();

	public static List<IRecipeCategory<?>> sortRecipeCategories(
		List<IRecipeCategory<?>> recipeCategories,
		IRecipeTransferManager recipeTransferManager
	) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) {
			return recipeCategories;
		}
		AbstractContainerMenu openContainer = player.containerMenu;
		//noinspection ConstantConditions
		if (openContainer == null) {
			return recipeCategories;
		}

		Comparator<IRecipeCategory<?>> comparator = Comparator.comparing((IRecipeCategory<?> r) -> {
			var recipeTransferHandler = recipeTransferManager.getRecipeTransferHandler(openContainer, r);
			return recipeTransferHandler.isPresent();
		})
			.reversed();

		return recipeCategories.stream()
			.sorted(comparator)
			.toList();
	}

	public static Comparator<RecipeLayoutWithButtons<?>> getCraftableComparator() {
		return CRAFTABLE_COMPARATOR;
	}

	private static Comparator<RecipeLayoutWithButtons<?>> createCraftableComparator() {
		return Comparator.comparingInt(r -> {
			IRecipeLayoutDrawable<?> recipeLayout = r.recipeLayout();

			RecipeTransferButton transferButton = r.transferButton();
			int missingCount = transferButton.getMissingCountHint();
			if (missingCount == -1) {
				return 0;
			}

			IRecipeSlotsView recipeSlotsView = recipeLayout.getRecipeSlotsView();
			int ingredientCount = ingredientCount(recipeSlotsView);
			if (ingredientCount == 0) {
				return 0;
			}

			int matchCount = ingredientCount - missingCount;
			int matchPercent = 100 * matchCount / ingredientCount;
			return -matchPercent;
		});
	}

	private static int ingredientCount(IRecipeSlotsView recipeSlotsView) {
		int count = 0;
		for (IRecipeSlotView i : recipeSlotsView.getSlotViews()) {
			if (i.getRole() == RecipeIngredientRole.INPUT && !i.isEmpty()) {
				count++;
			}
		}
		return count;
	}
}
