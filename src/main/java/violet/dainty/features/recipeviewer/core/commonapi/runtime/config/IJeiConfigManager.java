package violet.dainty.features.recipeviewer.core.commonapi.runtime.config;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;

import java.util.Collection;

/**
 * Gives access to JEI's config files.
 * Useful for mods that let users change configs in-game.
 *
 * Get an instance from {@link IJeiRuntime#getConfigManager()}
 *
 * @since 12.1.0
 */
public interface IJeiConfigManager {
	/**
	 * @return all of JEI's config files.
	 * @see IJeiConfigFile
	 *
	 * @since 12.1.0
	 */
	@Unmodifiable
	Collection<IJeiConfigFile> getConfigFiles();
}
