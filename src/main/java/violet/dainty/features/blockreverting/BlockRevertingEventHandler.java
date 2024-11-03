package violet.dainty.features.blockreverting;

import java.util.Set;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID)
public class BlockRevertingEventHandler {

	private static final Set<ConversionTool<?>> CONVERSION_TOOLS = Set.of(
		new AxeConversion(),
		new ShovelConversion(),
		new HoeConversion()
	);
	
	@SubscribeEvent
	public static void revert(BlockEvent.BlockToolModificationEvent event) {
		if (event.isSimulated()) return;

		for (ConversionTool<?> conversionTool : CONVERSION_TOOLS) {
			if (conversionTool.getToolClass().isInstance(event.getHeldItemStack().getItem())) {
				BlockState convertTo = conversionTool.getReverseConversions().get(event.getState());
				if (convertTo != null) {
					if (!event.getLevel().isClientSide()) event.getLevel().setBlock(event.getPos(), convertTo, 1 | 2);
					conversionTool.performExtraConversionSteps((Level) event.getLevel(), event.getPlayer(), event.getState(), event.getPos());
					event.getPlayer().playSound(conversionTool.getConversionSound());
					event.getPlayer().swing(event.getPlayer().getUsedItemHand());
				}
			}
		}
	}
}
