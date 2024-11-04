package violet.dainty.features.gravestone.gui;

import java.util.Arrays;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class Page {

    private static final int ITEM_START_Y = 60;

    private ItemStack[] items;
    private ObituaryScreen gui;

    public Page(ItemStack[] it, ObituaryScreen gui) {
        this.gui = gui;
        int arraySize = 10;
        items = new ItemStack[10];
        if (it.length < 10) {
            arraySize = it.length;
        }
        for (int i = 0; i < items.length && i < arraySize; i++) {
            items[i] = it[i];
        }
    }

    @SuppressWarnings("null")
	public void drawPage(GuiGraphics guiGraphics, int page, int pageCount, int mouseX, int mouseY) {
        gui.drawCentered(guiGraphics, gui.getFontRenderer(), Component.translatable("gui.obituary.title.items").withStyle(ChatFormatting.UNDERLINE), gui.width / 2, 30, ChatFormatting.BLACK.getColor());
        gui.drawCentered(guiGraphics, gui.getFontRenderer(), Component.translatable("gui.obituary.page", page, pageCount), gui.width / 2, 43, ChatFormatting.DARK_GRAY.getColor());

        int y = ITEM_START_Y;
        final int space = 12;

        for (ItemStack s : items) {
            if (s == null || s.isEmpty()) {
                continue;
            }
            gui.drawItem(guiGraphics, Component.translatable(s.getDescriptionId()).withStyle(ChatFormatting.ITALIC), y);
            gui.drawItemSize(guiGraphics, Component.literal(String.valueOf(s.getCount())), y);
            y = y + space;
        }

        if (mouseX >= gui.getGuiLeft() + ObituaryScreen.ITEM_SIZE_OFFSET_LEFT && mouseX <= gui.getGuiLeft() + ObituaryScreen.TEXTURE_X - ObituaryScreen.OFFSET_RIGHT) {
            if (mouseY >= ITEM_START_Y && mouseY <= ITEM_START_Y + 10 * space) {
                int index = (mouseY + 3 - ITEM_START_Y) / 12;
                ItemStack stack = items[Math.max(0, Math.min(items.length - 1, index))];
                if (stack != null && !stack.isEmpty()) {
                    guiGraphics.renderTooltip(gui.getFontRenderer(), stack, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(items);
    }

}
