package violet.dainty.features.veinmine;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import violet.dainty.registries.DaintyDataAttachments;
import violet.dainty.registries.DaintyKeyBindings;

public class VeinMineSettingsOverlay implements LayeredDraw.Layer {

	private static final Queue<Integer> shifts = new LinkedList<>();

	private int shiftsFrame;

	private static int centerOffset = 0;
	private static int maxStringWidth = 0;

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;

		// Guard checks
		if (player == null) return;
		if (!DaintyKeyBindings.VEINMINE.get().isDown()) return;

		VeinMineSettings settings = player.getData(DaintyDataAttachments.VEIN_MINE_SETTINGS_ATTACHMENT_TYPE);

		float timePerShift = 0.5f;
		int lineHeight = 12;
		int lines = VeinMineSettings.Shape.values().length;
		int maxLinesOnScreen = 5;

		int line = 0;

		Integer shift = shifts.peek();
		int yOffset = shift == null ? 0 : (int) ((shift * lineHeight) * ((float) shiftsFrame / (timePerShift * 20f)));

		// If finished, reset frame counter and add to center offset
		if (shiftsFrame == Math.round(timePerShift * 20f)) {
			shiftsFrame = 0;
			centerOffset += shifts.poll() * lineHeight;
		} 
		
		// Increment frame counter
		else if (!shifts.isEmpty()) {
			shiftsFrame++;
		}

		// Draw shapes
		for (VeinMineSettings.Shape shape : VeinMineSettings.Shape.values()) {
			int y = centerOffset + line * lineHeight + yOffset;
			while (y < 0) y += lines * lineHeight;
			y %= lines * lineHeight;
			if (y >= lineHeight * maxLinesOnScreen) {
				line++;
				continue;
			}
			int color = (y / lineHeight) == (maxLinesOnScreen / 2) ? 0xFFFFFF : 0x999999;
			int textWidth = drawConfiguredStringOnHUD(guiGraphics, shape.toString(), 2, y, color, 0);
			maxStringWidth = Math.max(maxStringWidth, textWidth);
			line++;
		}

		guiGraphics.renderOutline(0, (maxLinesOnScreen + 1) / 2 * lineHeight - lineHeight / 2 + 2, maxStringWidth + 4, lineHeight + 2, 0xFFFFFFFF);
	}

	private static int drawStringLeft(GuiGraphics guiGraphics, String string, Font font, int x, int y, int color) {
		return guiGraphics.drawString(font, string, x, y, color, true);
	}

	@SuppressWarnings("resource")
	private static int drawConfiguredStringOnHUD(GuiGraphics guiGraphics, String string, int xOffset, int yOffset, int color, int relLineOffset) {
		yOffset += (relLineOffset + 1) * 9;
		return drawStringLeft(guiGraphics, string, Minecraft.getInstance().font, xOffset + 2, yOffset + 2, color);
	}

	public static void shift(int amount) {
		shifts.add(-amount);
	}
	
}
