package violet.dainty.features.carryon;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import violet.dainty.features.carryon.client.CarryOnKeybinds;

public class CarryOnCommonClient {

	public static void checkForKeybinds() {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player != null) {
			CarryOnData carry = CarryOnDataManager.getCarryData(player);
			if ((CarryOnKeybinds.carryKey.isUnbound() ? player.isShiftKeyDown() : (CarryOnKeybinds.carryKey.isDown() || checkMouse())) && !carry.isKeyPressed()) {
				CarryOnKeybinds.onCarryKey(true);
				carry.setKeyPressed(true);
				CarryOnDataManager.setCarryData(player, carry);
			} 
			else if (!(CarryOnKeybinds.carryKey.isUnbound() ? player.isShiftKeyDown() : (CarryOnKeybinds.carryKey.isDown() || checkMouse()) ) && carry.isKeyPressed()) {
				CarryOnKeybinds.onCarryKey(false);
				carry.setKeyPressed(false);
				CarryOnDataManager.setCarryData(player, carry);
			}
		}
	}

	private static boolean checkMouse() {
		Minecraft mc = Minecraft.getInstance();
		return (CarryOnKeybinds.carryKey.matchesMouse(0) && mc.mouseHandler.isLeftPressed()) || (CarryOnKeybinds.carryKey.matchesMouse(1) && mc.mouseHandler.isRightPressed()) || (CarryOnKeybinds.carryKey.matchesMouse(3) && mc.mouseHandler.isMiddlePressed());
	}

	public static void onCarryClientTick() {
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		if(player != null) {
			CarryOnData carry = CarryOnDataManager.getCarryData(player);
			if(carry.isCarrying())
			{
				player.getInventory().selected = carry.getSelected();
			}
		}
	}

	@SuppressWarnings("resource")
	public static Player getPlayer() {
		return Minecraft.getInstance().player;
	}
}