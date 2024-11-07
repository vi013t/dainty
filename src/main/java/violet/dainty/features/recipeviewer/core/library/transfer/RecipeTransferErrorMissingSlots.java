package violet.dainty.features.recipeviewer.core.library.transfer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;

import java.util.Collection;

public class RecipeTransferErrorMissingSlots extends RecipeTransferErrorTooltip {
	private static final int HIGHLIGHT_COLOR = 0x66FF0000;

	private final Collection<IRecipeSlotView> slots;

	public RecipeTransferErrorMissingSlots(Component message, Collection<IRecipeSlotView> slots) {
		super(message);
		this.slots = slots;
	}

	@Override
	public void showError(GuiGraphics guiGraphics, int mouseX, int mouseY, IRecipeSlotsView recipeSlotsView, int recipeX, int recipeY) {
		var poseStack = guiGraphics.pose();
		poseStack.pushPose();
		{
			poseStack.translate(recipeX, recipeY, 0);

			for (IRecipeSlotView slot : slots) {
				slot.drawHighlight(guiGraphics, HIGHLIGHT_COLOR);
			}
		}
		poseStack.popPose();
	}

	@Override
	public int getMissingCountHint() {
		return this.slots.size();
	}
}
