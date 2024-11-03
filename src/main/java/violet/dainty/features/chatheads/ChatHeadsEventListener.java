package violet.dainty.features.chatheads;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;

public class ChatHeadsEventListener {
	
	 private static void renderHead(GuiGraphics guiGraphics, int x, int y, PlayerInfo owner) {
        ResourceLocation skinLocation = owner.getSkin().texture();
		guiGraphics.blit(skinLocation, x, y, 8, 8, 8, 8, 8, 8);
		// guiGraphics.blit(skinLocation, x, y, 40.0f, 8, 8, 8, 8, 8, 64, 64, color);
    }
}
