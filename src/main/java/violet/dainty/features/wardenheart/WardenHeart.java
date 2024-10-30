package violet.dainty.features.wardenheart;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class WardenHeart extends Item {

	public WardenHeart() {
		super(new Item.Properties().rarity(Rarity.RARE).fireResistant());
	}
	
}
