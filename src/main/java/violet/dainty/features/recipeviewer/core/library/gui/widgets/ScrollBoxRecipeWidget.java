package violet.dainty.features.recipeviewer.core.library.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.FormattedText;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IJeiClientConfigs;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableBlank;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableWrappedText;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiInputHandler;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollBoxWidget;

import org.joml.Matrix4f;

import java.util.List;

public class ScrollBoxRecipeWidget extends AbstractScrollWidget implements IScrollBoxWidget, IJeiInputHandler {
	private IDrawable contents = DrawableBlank.EMPTY;

	public ScrollBoxRecipeWidget(int width, int height, int xPos, int yPos) {
		super(new ImmutableRect2i(xPos, yPos, width, height));
	}

	@Override
	public int getContentAreaWidth() {
		return contentsArea.width();
	}

	@Override
	public int getContentAreaHeight() {
		return contentsArea.height();
	}

	@Override
	public IScrollBoxWidget setContents(IDrawable contents) {
		this.contents = contents;
		return this;
	}

	@Override
	public IScrollBoxWidget setContents(List<FormattedText> text) {
		this.contents = new DrawableWrappedText(text, getContentAreaWidth());
		return this;
	}

	@Override
	protected int getVisibleAmount() {
		return contentsArea.height();
	}

	@Override
	protected int getHiddenAmount() {
		return Math.max(contents.getHeight() - contentsArea.height(), 0);
	}

	@Override
	protected void drawContents(GuiGraphics guiGraphics, double mouseX, double mouseY, float scrollOffsetY) {
		PoseStack poseStack = guiGraphics.pose();
		PoseStack.Pose last = poseStack.last();
		Matrix4f pose = last.pose();

		ScreenRectangle scissorArea = MathUtil.transform(contentsArea, pose);
		guiGraphics.enableScissor(
			scissorArea.left(),
			scissorArea.top(),
			scissorArea.right(),
			scissorArea.bottom()
		);
		poseStack.pushPose();
		float scrollAmount = getHiddenAmount() * scrollOffsetY;
		poseStack.translate(0.0, -scrollAmount, 0.0);
		try {
			contents.draw(guiGraphics);
		} finally {
			poseStack.popPose();
			guiGraphics.disableScissor();
		}
	}

	@Override
	protected float calculateScrollAmount(double scrollDeltaY) {
		IJeiClientConfigs jeiClientConfigs = Internal.getJeiClientConfigs();
		IClientConfig clientConfig = jeiClientConfigs.getClientConfig();
		int smoothScrollRate = clientConfig.getSmoothScrollRate();

		int totalHeight = contents.getHeight();
		double scrollAmount = scrollDeltaY * smoothScrollRate;
		return (float) (scrollAmount / (double) totalHeight);
	}
}
