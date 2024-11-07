package violet.dainty.features.recipeviewer.core.common.config.file;

import violet.dainty.features.recipeviewer.core.common.config.ConfigManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigFile;

public interface IConfigSchema extends IJeiConfigFile {
	void register(FileWatcher fileWatcher, ConfigManager configManager);

	void loadIfNeeded();

	void markDirty();
}
