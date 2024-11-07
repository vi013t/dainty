package violet.dainty.features.recipeviewer.core.gui.recipes;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferErrorInternal;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

public class RecipeTransferButton extends GuiIconToggleButton {
	public static RecipeTransferButton create(
		IRecipeLayoutDrawable<?> recipeLayout,
		Runnable onClose,
		@Nullable AbstractContainerMenu container,
		@Nullable Player player
	) {
		Rect2i buttonArea = recipeLayout.getRecipeTransferButtonArea();
		Rect2i layoutArea = recipeLayout.getRect();
		buttonArea.setX(buttonArea.getX() + layoutArea.getX());
		buttonArea.setY(buttonArea.getY() + layoutArea.getY());

		Textures textures = Internal.getTextures();
		IDrawable icon = textures.getRecipeTransfer();
		RecipeTransferButton transferButton = new RecipeTransferButton(icon, recipeLayout, onClose);
		transferButton.updateBounds(buttonArea);
		transferButton.update(container, player);
		return transferButton;
	}

	private final IRecipeLayoutDrawable<?> recipeLayout;
	private final Runnable onClose;
	private @Nullable IRecipeTransferError recipeTransferError;
	private @Nullable AbstractContainerMenu parentContainer;

	private RecipeTransferButton(IDrawable icon, IRecipeLayoutDrawable<?> recipeLayout, Runnable onClose) {
		super(icon, icon);
		this.recipeLayout = recipeLayout;
		this.onClose = onClose;
	}

	public void update(@Nullable AbstractContainerMenu parentContainer, @Nullable Player player) {
		this.parentContainer = parentContainer;

		if (parentContainer != null && player != null) {
			IRecipeTransferManager recipeTransferManager = Internal.getJeiRuntime().getRecipeTransferManager();
			this.recipeTransferError = RecipeTransferUtil.getTransferRecipeError(recipeTransferManager, parentContainer, recipeLayout, player)
				.orElse(null);
		} else {
			this.recipeTransferError = RecipeTransferErrorInternal.INSTANCE;
		}

		if (recipeTransferError == null ||
			recipeTransferError.getType().allowsTransfer) {
			this.button.active = true;
			this.button.visible = true;
		} else {
			this.button.active = false;
			IRecipeTransferError.Type type = this.recipeTransferError.getType();
			this.button.visible = (type == IRecipeTransferError.Type.USER_FACING);
		}
	}

	@Override
	protected boolean onMouseClicked(UserInput input) {
		if (!input.isSimulate()) {
			IRecipeTransferManager recipeTransferManager = Internal.getJeiRuntime().getRecipeTransferManager();
			boolean maxTransfer = Screen.hasShiftDown();
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (parentContainer != null && player != null && RecipeTransferUtil.transferRecipe(recipeTransferManager, parentContainer, recipeLayout, player, maxTransfer)) {
				onClose.run();
			}
		}
		return true;
	}

	@Override
	protected void getTooltips(JeiTooltip tooltip) {
		if (recipeTransferError == null) {
			Component tooltipTransfer = Component.translatable("dainty.tooltip.transfer");
			tooltip.add(tooltipTransfer);
		} else {
			recipeTransferError.getTooltip(tooltip);
		}
	}

	@Override
	protected boolean isIconToggledOn() {
		return false;
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.draw(guiGraphics, mouseX, mouseY, partialTicks);
		IRecipeTransferError recipeTransferError = this.recipeTransferError;
		if (recipeTransferError != null) {
			if (recipeTransferError.getType() == IRecipeTransferError.Type.COSMETIC) {
				guiGraphics.fill(
					RenderType.guiOverlay(),
					this.button.getX(),
					this.button.getY(),
					this.button.getX() + this.button.getWidth(),
					this.button.getY() + this.button.getHeight(),
					recipeTransferError.getButtonHighlightColor()
				);
			}
			if (isMouseOver(mouseX, mouseY)) {
				IRecipeSlotsView recipeSlotsView = recipeLayout.getRecipeSlotsView();
				Rect2i recipeRect = recipeLayout.getRect();
				recipeTransferError.showError(guiGraphics, mouseX, mouseY, recipeSlotsView, recipeRect.getX(), recipeRect.getY());
			}
		}
	}

	public int getMissingCountHint() {
		if (recipeTransferError == null) {
			return 0;
		}
		return recipeTransferError.getMissingCountHint();
	}
}
