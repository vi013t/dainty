package violet.dainty.features.carryon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.entity.player.Player;


public class GamestageCompat
{
	private static Method hasStage;

	static {
		try {
			Class<?> gamestageHelper = Class.forName("net.darkhax.gamestages.GameStageHelper");
			hasStage = gamestageHelper.getMethod("hasStage", Player.class, String.class);

		} catch (Exception e) {
			System.out.println("Gamestages not found. Disabling.");
		}
	}

	public static boolean hasStage(Player player, String stage) {
		if(hasStage == null) return true;
		try {
			return (boolean) hasStage.invoke(null, player, stage);
		} 
		catch (IllegalAccessException | InvocationTargetException e) {}
		return true;
	}
}
