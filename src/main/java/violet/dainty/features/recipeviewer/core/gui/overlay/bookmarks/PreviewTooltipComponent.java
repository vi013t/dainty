package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;

import org.jetbrains.annotations.Nullable;

public class PreviewTooltipComponent<R> implements ClientTooltipComponent, TooltipComponent {
	private static final int UPDATE_INTERVAL_MS = 2000;

	private final IRecipeLayoutDrawable<R> drawable;
	private @Nullable IRecipeTransferError transferError;
	private long lastUpdateTime = 0;

	public PreviewTooltipComponent(IRecipeLayoutDrawable<R> drawable) {
		this.drawable = drawable;
	}

	@Override
	public int getHeight() {
		return drawable.getRect().getHeight() + 10;
	}

	@Override
	public int getWidth(Font font) {
		return drawable.getRect().getWidth() + 4;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		PoseStack pose = guiGraphics.pose();
		pose.pushPose();
		{
			pose.translate(x + 2, y + 5, 0);
			drawable.drawRecipe(guiGraphics, 0, 0);
			updateTransferError();
			if (transferError != null) {
				Rect2i recipeRect = drawable.getRect();
				transferError.showError(guiGraphics, x, y, drawable.getRecipeSlotsView(), recipeRect.getX(), recipeRect.getY());
			}
		}
		pose.popPose();
	}

	private void updateTransferError() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdateTime < UPDATE_INTERVAL_MS) {
			return;
		}
		lastUpdateTime = currentTime;

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) {
			transferError = null;
			return;
		}
		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof AbstractContainerScreen<?> containerScreen) {
			AbstractContainerMenu container = containerScreen.getMenu();
			IRecipeTransferManager recipeTransferManager = Internal.getJeiRuntime().getRecipeTransferManager();
			transferError = RecipeTransferUtil.getTransferRecipeError(recipeTransferManager, container, drawable, player)
				.orElse(null);
		} else {
			transferError = null;
		}
	}
}
