package violet.dainty.features.blocktooltips.impl.ui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.ITextElement;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;

public class SpecialTextElement extends TextElement {

	private float scale = 1;
	private int zOffset;

	public SpecialTextElement(FormattedText text) {
		super(text);
	}

	@Override
	public Vec2 getSize() {
		@SuppressWarnings("resource")
		Font font = Minecraft.getInstance().font;
		return new Vec2(font.width(text) * scale, font.lineHeight * scale + 1);
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
		PoseStack matrixStack = guiGraphics.pose();
		matrixStack.pushPose();
		matrixStack.translate(x, y + scale, zOffset);
		matrixStack.scale(scale, scale, 1);
		DisplayHelper.INSTANCE.drawText(guiGraphics, text, 0, 0, IThemeHelper.get().getNormalColor());
		matrixStack.popPose();
	}

	@Override
	public SpecialTextElement toSpecial() {
		return this;
	}

	@Override
	public ITextElement scale(float scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public ITextElement zOffset(int zOffset) {
		this.zOffset = zOffset;
		return this;
	}
}
