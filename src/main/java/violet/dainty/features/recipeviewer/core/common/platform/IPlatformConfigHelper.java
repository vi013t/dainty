package violet.dainty.features.recipeviewer.core.common.platform;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public interface IPlatformConfigHelper {
	Path getModConfigDir();

	Optional<Screen> getConfigScreen();

	default Path createJeiConfigDir() {
		Path configDir = getModConfigDir()
			.resolve(ModIds.JEI_ID);

		try {
			Files.createDirectories(configDir);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create JEI config directory: " + configDir, e);
		}
		return configDir;
	}
}
