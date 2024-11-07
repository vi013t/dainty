package violet.dainty.features.recipeviewer.core.common.config;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.common.config.file.ConfigSchema;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigFile;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigManager;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements IJeiConfigManager {
	private final Map<Path, ConfigSchema> configFiles = new HashMap<>();

	public ConfigManager() {

	}

	public void registerConfigFile(ConfigSchema configFile) {
		this.configFiles.put(configFile.getPath(), configFile);
	}

	@Override
	public @Unmodifiable Collection<IJeiConfigFile> getConfigFiles() {
		return Collections.unmodifiableCollection(configFiles.values());
	}

	public void onJeiStarted() {
		configFiles.values().forEach(ConfigSchema::markDirty);
	}
}
