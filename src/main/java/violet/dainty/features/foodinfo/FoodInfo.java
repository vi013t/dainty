package violet.dainty.features.foodinfo;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import violet.dainty.features.foodinfo.client.DebugInfoHandler;
import violet.dainty.features.foodinfo.client.HUDOverlayHandler;
import violet.dainty.features.foodinfo.client.TooltipOverlayHandler;
import violet.dainty.features.foodinfo.network.SyncHandler;

public class FoodInfo {

	public FoodInfo(IEventBus modEventBus, ModContainer container)
	{
		modEventBus.addListener(this::onRegisterPayloadHandler);
		if (FMLEnvironment.dist.isClient())
		{
			modEventBus.addListener(this::preInitClient);
			modEventBus.addListener(this::onRegisterHudHandler);
			modEventBus.addListener(this::onRegisterClientTooltipComponentFactories);
		}
	}

	private void preInitClient(final FMLClientSetupEvent event)
	{
		DebugInfoHandler.init();
		TooltipOverlayHandler.init();
	}

	private void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event)
	{
		TooltipOverlayHandler.register(event);
	}

	@SubscribeEvent
	private void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event)
	{
		SyncHandler.register(event);
	}

	@SubscribeEvent
	private void onRegisterHudHandler(final RegisterGuiLayersEvent event)
	{
		HUDOverlayHandler.register(event);
	}
}
