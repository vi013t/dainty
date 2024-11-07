package violet.dainty.features.recipeviewer.core.neoforge.plugins.neoforge;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.JeiPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRuntimeRegistration;
import violet.dainty.features.recipeviewer.core.gui.startup.JeiEventHandlers;
import violet.dainty.features.recipeviewer.core.gui.startup.JeiGuiStarter;
import violet.dainty.features.recipeviewer.core.gui.startup.ResourceReloadHandler;
import violet.dainty.features.recipeviewer.core.neoforge.events.RuntimeEventSubscriptions;
import violet.dainty.features.recipeviewer.core.neoforge.startup.EventRegistration;

@JeiPlugin
public class NeoForgeGuiPlugin implements IModPlugin {
	private static final Logger LOGGER = LogManager.getLogger();
	private static @Nullable ResourceReloadHandler resourceReloadHandler;

	private final RuntimeEventSubscriptions runtimeSubscriptions = new RuntimeEventSubscriptions(NeoForge.EVENT_BUS);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "neoforge_gui");
	}

	@Override
	public void registerRuntime(IRuntimeRegistration registration) {
		if (!runtimeSubscriptions.isEmpty()) {
			LOGGER.error("JEI GUI is already running.");
			runtimeSubscriptions.clear();
		}

		JeiEventHandlers eventHandlers = JeiGuiStarter.start(registration);
		resourceReloadHandler = eventHandlers.resourceReloadHandler();

		EventRegistration.registerEvents(runtimeSubscriptions, eventHandlers);
	}

	@Override
	public void onRuntimeUnavailable() {
		LOGGER.info("Stopping JEI GUI");
		runtimeSubscriptions.clear();
		resourceReloadHandler = null;
	}

	public static Optional<ResourceReloadHandler> getResourceReloadHandler() {
		return Optional.ofNullable(resourceReloadHandler);
	}
}
