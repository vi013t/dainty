package violet.dainty.features.carryon;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CarryOn {

	public CarryOn(ModContainer container) {
        // CarryOnCommon.registerConfig();
        container.getEventBus().addListener(this::setup);
        container.getEventBus().addListener(this::registerPackets);

        // ConfigLoaderImpl.initialize(container);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    public void registerPackets(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar("1.0.0");

        CarryOnCommon.registerServerPackets(registrar);
        CarryOnCommon.registerClientPackets(registrar);
    }	
}
