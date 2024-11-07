package violet.dainty.features.recipeviewer.core.common.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.gui.GuiGraphics;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Constants;
import violet.dainty.features.recipeviewer.core.common.gui.textures.JeiSpriteUploader;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;

import org.joml.Matrix4f;

public class DrawableSprite implements IDrawableStatic {
	private final JeiSpriteUploader spriteUploader;
	private final ResourceLocation location;
	private final int width;
	private final int height;
	private int trimLeft;
	private int trimRight;
	private int trimTop;
	private int trimBottom;

	public DrawableSprite(JeiSpriteUploader spriteUploader, ResourceLocation location, int width, int height) {
		this.spriteUploader = spriteUploader;
		this.location = location;
		this.width = width;
		this.height = height;
	}

	public DrawableSprite trim(int left, int right, int top, int bottom) {
		this.trimLeft = left;
		this.trimRight = right;
		this.trimTop = top;
		this.trimBottom = bottom;
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		draw(guiGraphics, xOffset, yOffset, 0, 0, 0, 0);
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		TextureAtlasSprite sprite = spriteUploader.getSprite(location);
		int textureWidth = this.width;
		int textureHeight = this.height;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, Constants.LOCATION_JEI_GUI_TEXTURE_ATLAS);

		maskTop += trimTop;
		maskBottom += trimBottom;
		maskLeft += trimLeft;
		maskRight += trimRight;

		int x = xOffset + maskLeft;
		int y = yOffset + maskTop;
		int width = textureWidth - maskRight - maskLeft;
		int height = textureHeight - maskBottom - maskTop;
		float uSize = sprite.getU1() - sprite.getU0();
		float vSize = sprite.getV1() - sprite.getV0();

		float minU = sprite.getU0() + uSize * (maskLeft / (float) textureWidth);
		float minV = sprite.getV0() + vSize * (maskTop / (float) textureHeight);
		float maxU = sprite.getU1() - uSize * (maskRight / (float) textureWidth);
		float maxV = sprite.getV1() - vSize * (maskBottom / (float) textureHeight);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		Matrix4f matrix = guiGraphics.pose().last().pose();
		bufferBuilder.addVertex(matrix, x, y + height, 0)
			.setUv(minU, maxV);
		bufferBuilder.addVertex(matrix, x + width, y + height, 0)
			.setUv(maxU, maxV);
		bufferBuilder.addVertex(matrix, x + width, y, 0)
			.setUv(maxU, minV);
		bufferBuilder.addVertex(matrix, x, y, 0)
			.setUv(minU, minV);

		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
	}
}
