package violet.dainty.features.biomecompass.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import violet.dainty.features.biomecompass.NaturesCompass;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;

public class GuiWrapper {
	
	public static void openGUI(Level level, Player player, ItemStack stack) {
		Minecraft.getInstance().setScreen(new NaturesCompassScreen(level, player, stack, (NaturesCompassItem) stack.getItem(), NaturesCompass.allowedBiomes));
	}

}
