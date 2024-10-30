package violet.dainty;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import violet.dainty.data.DaintyDataAttachments;
import violet.dainty.data.DaintyDataComponents;
import violet.dainty.features.carryon.CarryOn;
import violet.dainty.item.DaintyItems;

@Mod(Dainty.MODID)
public class Dainty {

    public static final String MODID = "dainty";

    public Dainty(final IEventBus modEventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DaintyConfig.SPEC);
		new CarryOn(modContainer);

		DaintyDataComponents.register(modEventBus);
		DaintyDataAttachments.register(modEventBus);
		DaintyItems.register(modEventBus);
    }
}
