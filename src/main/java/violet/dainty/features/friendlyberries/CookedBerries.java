package violet.dainty.features.friendlyberries;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class CookedBerries extends Item {

	public CookedBerries() {
		super(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6f).build()));
	}
	
}
