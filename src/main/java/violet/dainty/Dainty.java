package violet.dainty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import violet.dainty.data.DaintyDataAttachments;
import violet.dainty.data.DaintyDataComponents;
import violet.dainty.effects.DaintyPotions;
import violet.dainty.features.biomecompass.NaturesCompass;
import violet.dainty.features.carryon.CarryOn;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.item.DaintyItems;

@Mod(Dainty.MODID)
public class Dainty {

    public static final String MODID = "dainty"; 
	public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Dainty(final IEventBus modEventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DaintyConfig.SPEC);
		new CarryOn(modContainer);
		new NaturesCompass(modContainer);
		new StructureCompass(modContainer);

		DaintyDataComponents.register(modEventBus);
		DaintyDataAttachments.register(modEventBus);
		DaintyItems.register(modEventBus);
		DaintyPotions.register(modEventBus);
    }
}
