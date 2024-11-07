package violet.dainty.features.recipeviewer.core.neoforge;

import violet.dainty.features.recipeviewer.core.neoforge.events.PermanentEventSubscriptions;
import violet.dainty.features.recipeviewer.core.neoforge.network.NetworkHandler;

public class JustEnoughItemsClientSafeRunner {
	private final NetworkHandler networkHandler;
	private final PermanentEventSubscriptions subscriptions;

	public JustEnoughItemsClientSafeRunner(
		NetworkHandler networkHandler,
		PermanentEventSubscriptions subscriptions
	) {
		this.networkHandler = networkHandler;
		this.subscriptions = subscriptions;
	}

	public void registerClient() {
		JustEnoughItemsClient jeiClient = new JustEnoughItemsClient(networkHandler, subscriptions);
		jeiClient.register();
	}
}
