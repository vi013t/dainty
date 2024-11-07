package violet.dainty.features.recipeviewer.core.common.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import violet.dainty.features.recipeviewer.core.neoforge.platform.PlatformHelper;

public class Services {
	private static final Logger LOGGER = LogManager.getLogger();

	public static final IPlatformHelper PLATFORM = new PlatformHelper();

	// public static <T> T load(Class<T> serviceClass) {
	// 	T loadedService = ServiceLoader.load(serviceClass)
	// 		.findFirst()
	// 		.orElseThrow(() -> new NullPointerException("Failed to load service for " + serviceClass.getName()));
	// 	LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass);
	// 	return loadedService;
	// }
}
