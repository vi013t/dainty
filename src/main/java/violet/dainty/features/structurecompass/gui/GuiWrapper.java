package violet.dainty.features.structurecompass.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.features.structurecompass.items.ExplorersCompassItem;

public class GuiWrapper {
	
	public static void openGUI(Level level, Player player, ItemStack stack) {
		Minecraft.getInstance().setScreen(new ExplorersCompassScreen(level, player, stack, (ExplorersCompassItem) stack.getItem(), StructureCompass.allowedStructureKeys));
	}

}
