package violet.dainty.features.biomecompass.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.biomecompass.NaturesCompass;

public class ItemUtils {

	public static ItemStack getHeldNatureCompass(Player player) {
		return getHeldItem(player, NaturesCompass.naturesCompass);
	}

	public static ItemStack getHeldItem(Player player, Item item) {
		if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == item) {
			return player.getMainHandItem();
		} else if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() == item) {
			return player.getOffhandItem();
		}

		return ItemStack.EMPTY;
	}

}
