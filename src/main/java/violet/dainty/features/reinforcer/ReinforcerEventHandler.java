package violet.dainty.features.reinforcer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID)
public class ReinforcerEventHandler {

	@SubscribeEvent
	public static void createAnvilResult(AnvilUpdateEvent event) {
		if (event.getRight().getItem() instanceof Reinforcer) {
			ItemStack output = event.getLeft().copy();
			output.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
			output.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
			event.setOutput(output);	
			event.setCost(30);
		}
	}
	
}
