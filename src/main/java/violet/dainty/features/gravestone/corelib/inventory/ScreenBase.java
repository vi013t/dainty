package violet.dainty.features.gravestone.corelib.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public static final int FONT_COLOR = 4210752;

    protected ResourceLocation texture;
    protected List<HoverArea> hoverAreas;

    public ScreenBase(ResourceLocation texture, T container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.texture = texture;
        this.hoverAreas = new ArrayList<>();
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, x, y, partialTicks);
        renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @SuppressWarnings("null")
	public void drawHoverAreas(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (HoverArea hoverArea : hoverAreas) {
            if (hoverArea.tooltip != null && hoverArea.isHovered(leftPos, topPos, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, hoverArea.tooltip.get(), mouseX, mouseY);
            }
        }
    }

    public int getBlitSize(int amount, int max, int size) {
        return size - (int) (((float) amount / (float) max) * (float) size);
    }

    public void drawCentered(GuiGraphics guiGraphics, Component text, int y, int color) {
        drawCentered(guiGraphics, text, imageWidth / 2, y, color);
    }

    public void drawCentered(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawCentered(font, guiGraphics, text, x, y, color);
    }

    public static void drawCentered(Font font, GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text, x - width / 2, y, color, false);
    }

    public static class HoverArea {
        private final int posX, posY;
        private final int width, height;
        @Nullable
        private final Supplier<List<FormattedCharSequence>> tooltip;

        public HoverArea(int posX, int posY, int width, int height) {
            this(posX, posY, width, height, null);
        }

        public HoverArea(int posX, int posY, int width, int height, Supplier<List<FormattedCharSequence>> tooltip) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.tooltip = tooltip;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Nullable
        public Supplier<List<FormattedCharSequence>> getTooltip() {
            return tooltip;
        }

        public boolean isHovered(int guiLeft, int guiTop, int mouseX, int mouseY) {
            if (mouseX >= guiLeft + posX && mouseX < guiLeft + posX + width) {
                if (mouseY >= guiTop + posY && mouseY < guiTop + posY + height) {
                    return true;
                }
            }
            return false;
        }
    }

}
