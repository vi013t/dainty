package violet.dainty.features.recipeviewer.core.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import violet.dainty.features.recipeviewer.core.common.config.IServerConfig;
import violet.dainty.features.recipeviewer.core.common.util.MinecraftLocaleSupplier;
import violet.dainty.features.recipeviewer.core.common.util.Translator;
import violet.dainty.features.recipeviewer.core.neoforge.config.ServerConfig;
import violet.dainty.features.recipeviewer.core.neoforge.events.PermanentEventSubscriptions;
import violet.dainty.features.recipeviewer.core.neoforge.network.NetworkHandler;

public class JustEnoughItems {

	public JustEnoughItems(IEventBus modEventBus, Dist dist) {
		Translator.setLocaleSupplier(new MinecraftLocaleSupplier());
		IEventBus eventBus = NeoForge.EVENT_BUS;
		PermanentEventSubscriptions subscriptions = new PermanentEventSubscriptions(eventBus, modEventBus);

		IServerConfig serverConfig = new ServerConfig();

		NetworkHandler networkHandler = new NetworkHandler("3", serverConfig);
		networkHandler.registerPacketHandlers(subscriptions);

		JustEnoughItemsClientSafeRunner clientSafeRunner = new JustEnoughItemsClientSafeRunner(networkHandler, subscriptions);
		if (dist.isClient()) {
			clientSafeRunner.registerClient();
		}
	}
}
