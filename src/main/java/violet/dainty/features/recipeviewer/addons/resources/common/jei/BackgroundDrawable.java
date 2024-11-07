package violet.dainty.features.recipeviewer.addons.resources.common.jei;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Reference;
import violet.dainty.features.recipeviewer.addons.resources.common.util.RenderHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;

public class BackgroundDrawable implements IDrawable {
    private final int width, height;
    private final ResourceLocation resource;
    private static final int PADDING = 5;

    public BackgroundDrawable(String resource, int width, int height) {
        this.resource = ResourceLocation.fromNamespaceAndPath(Reference.ID, resource);
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width + PADDING * 2;
    }

    @Override
    public int getHeight() {
        return this.height + PADDING * 2;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
        RenderSystem.clearColor(1.0F, 1.0F,1.0F,1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resource);
        RenderHelper.drawTexturedModalRect(guiGraphics, xOffset + PADDING, yOffset + PADDING, 0, 0, this.width, this.height, 0);  
        RenderSystem.applyModelViewMatrix();
    }

    public ResourceLocation getResource() {
        return resource;
    }
}
