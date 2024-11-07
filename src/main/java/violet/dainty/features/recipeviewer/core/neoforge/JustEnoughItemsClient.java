package violet.dainty.features.recipeviewer.core.neoforge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.gui.config.InternalKeyMappings;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.IngredientsTooltipComponent;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.PreviewTooltipComponent;
import violet.dainty.features.recipeviewer.core.library.gui.ingredients.TagContentTooltipComponent;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.JeiShapedRecipe;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeSerializers;
import violet.dainty.features.recipeviewer.core.library.startup.JeiStarter;
import violet.dainty.features.recipeviewer.core.library.startup.StartData;
import violet.dainty.features.recipeviewer.core.neoforge.events.PermanentEventSubscriptions;
import violet.dainty.features.recipeviewer.core.neoforge.network.NetworkHandler;
import violet.dainty.features.recipeviewer.core.neoforge.plugins.neoforge.NeoForgeGuiPlugin;
import violet.dainty.features.recipeviewer.core.neoforge.startup.ForgePluginFinder;
import violet.dainty.features.recipeviewer.core.neoforge.startup.StartEventObserver;

public class JustEnoughItemsClient {
	private final PermanentEventSubscriptions subscriptions;

	public JustEnoughItemsClient(
		NetworkHandler networkHandler,
		PermanentEventSubscriptions subscriptions
	) {
		this.subscriptions = subscriptions;

		InternalKeyMappings keyMappings = createKeyMappings(subscriptions);
		Internal.setKeyMappings(keyMappings);

		IConnectionToServer serverConnection = networkHandler.getConnectionToServer();

		List<IModPlugin> plugins = ForgePluginFinder.getModPlugins();
		StartData startData = new StartData(
			plugins,
			serverConnection,
			keyMappings
		);

		JeiStarter jeiStarter = new JeiStarter(startData);

		StartEventObserver startEventObserver = new StartEventObserver(jeiStarter::start, jeiStarter::stop);
		startEventObserver.register(subscriptions);
	}

	public void register() {
		subscriptions.register(RegisterClientReloadListenersEvent.class, this::onRegisterReloadListenerEvent);
		subscriptions.register(RegisterClientTooltipComponentFactoriesEvent.class, this::onRegisterClientTooltipEvent);

		IEventBus modEventBus = subscriptions.getModEventBus();
		DeferredRegister<RecipeSerializer<?>> deferredRegister = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ModIds.JEI_ID);
		deferredRegister.register(modEventBus);

		Supplier<RecipeSerializer<?>> jeiShaped = deferredRegister.register("jei_shaped", JeiShapedRecipe.Serializer::new);
		RecipeSerializers.register(jeiShaped);
	}

	private void onRegisterReloadListenerEvent(RegisterClientReloadListenersEvent event) {
		Textures textures = Internal.getTextures();
		event.registerReloadListener(textures.getSpriteUploader());
		event.registerReloadListener(createReloadListener());
	}

	private void onRegisterClientTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(IngredientsTooltipComponent.class, Function.identity());
		event.register(PreviewTooltipComponent.class, Function.identity());
		event.register(TagContentTooltipComponent.class, Function.identity());
	}

	private ResourceManagerReloadListener createReloadListener() {
		return (ResourceManager resourceManager) -> {
			NeoForgeGuiPlugin.getResourceReloadHandler()
				.ifPresent(r -> r.onResourceManagerReload(resourceManager));
		};
	}

	private static InternalKeyMappings createKeyMappings(PermanentEventSubscriptions subscriptions) {
		Set<KeyMapping> keysToRegister = new HashSet<>();
		subscriptions.register(RegisterKeyMappingsEvent.class, e -> keysToRegister.forEach(e::register));
		return new InternalKeyMappings(keysToRegister::add);
	}
}
