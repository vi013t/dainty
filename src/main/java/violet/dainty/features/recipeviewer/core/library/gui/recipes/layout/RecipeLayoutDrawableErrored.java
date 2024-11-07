package violet.dainty.features.recipeviewer.core.library.gui.recipes.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IScalableDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiInputHandler;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.RecipeSlotUnderMouse;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollBoxWidget;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.library.gui.OffsetJeiInputHandler;

public class RecipeLayoutDrawableErrored<R> implements IRecipeLayoutDrawable<R> {
	private final IRecipeCategory<R> recipeCategory;
	private final R recipe;
	private final IScrollBoxWidget scrollBoxWidget;
	private final IJeiInputHandler inputHandler;
	private final IScalableDrawable background;
	private final int borderPadding;
	private ImmutableRect2i area;

	public RecipeLayoutDrawableErrored(IRecipeCategory<R> recipeCategory, R recipe, IScalableDrawable background, int borderPadding) {
		this.recipeCategory = recipeCategory;
		this.recipe = recipe;
		this.area = new ImmutableRect2i(0, 0, Math.max(100, recipeCategory.getWidth()), recipeCategory.getHeight());
		this.background = background;
		this.borderPadding = borderPadding;

		List<FormattedText> lines = new ArrayList<>();
		lines.add(Component.translatable("gui.dainty.category.recipe.crashed").withStyle(ChatFormatting.RED));
		lines.add(Component.empty());
		lines.add(Component.literal(recipeCategory.getRecipeType().getUid().toString()).withStyle(ChatFormatting.GRAY));
		ResourceLocation registryName = recipeCategory.getRegistryName(recipe);
		if (registryName != null) {
			lines.add(Component.empty());
			lines.add(Component.literal(registryName.toString()).withStyle(ChatFormatting.GRAY));
		}

		IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
		IJeiHelpers jeiHelpers = jeiRuntime.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		this.scrollBoxWidget = guiHelper.createScrollBoxWidget(area.width(), area.getHeight(), 0, 0)
			.setContents(lines);

		this.inputHandler = new OffsetJeiInputHandler(this.scrollBoxWidget, this::getScreenPosition);
	}

	private ScreenPosition getScreenPosition() {
		return this.area.getScreenPosition();
	}

	@Override
	public void setPosition(int posX, int posY) {
		this.area = this.area.setPosition(posX, posY);
	}

	@Override
	public void drawRecipe(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		background.draw(guiGraphics, getRectWithBorder());

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		{
			poseStack.translate(area.x(), area.y(), 0);
			int recipeMouseX = mouseX - area.x();
			int recipeMouseY = mouseY - area.y();
			ScreenPosition position = scrollBoxWidget.getPosition();
			poseStack.pushPose();
			{
				poseStack.translate(position.x(), position.y(), 0);
				scrollBoxWidget.drawWidget(guiGraphics, recipeMouseX - position.x(), recipeMouseY - position.y());
			}
		}
		poseStack.popPose();
	}

	@Override
	public void drawOverlays(GuiGraphics guiGraphics, int mouseX, int mouseY) {

	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return area.contains(mouseX, mouseY);
	}

	@Override
	public <T> Optional<T> getIngredientUnderMouse(int mouseX, int mouseY, IIngredientType<T> ingredientType) {
		return Optional.empty();
	}

	@Override
	public Optional<IRecipeSlotDrawable> getRecipeSlotUnderMouse(double mouseX, double mouseY) {
		return Optional.empty();
	}

	@Override
	public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
		return Optional.empty();
	}

	@Override
	public Rect2i getRect() {
		return area.toMutable();
	}

	@Override
	public Rect2i getRectWithBorder() {
		return area.expandBy(borderPadding).toMutable();
	}

	@Override
	public Rect2i getRecipeTransferButtonArea() {
		return new Rect2i(0, 0, 0, 0);
	}

	@Override
	public Rect2i getRecipeBookmarkButtonArea() {
		return new Rect2i(0, 0, 0, 0);
	}

	@Override
	public IRecipeSlotsView getRecipeSlotsView() {
		return List::of;
	}

	@Override
	public IRecipeCategory<R> getRecipeCategory() {
		return recipeCategory;
	}

	@Override
	public R getRecipe() {
		return recipe;
	}

	@Override
	public IJeiInputHandler getInputHandler() {
		return inputHandler;
	}

	@Override
	public void tick() {

	}
}
