package violet.dainty.features.recipeviewer.core.library.load;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.library.plugins.jei.JeiInternalPlugin;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.VanillaPlugin;

public class PluginHelper {
	public static void sortPlugins(List<IModPlugin> plugins, VanillaPlugin vanillaPlugin, @Nullable JeiInternalPlugin jeiInternalPlugin) {
		plugins.remove(vanillaPlugin);
		plugins.addFirst(vanillaPlugin);

		if (jeiInternalPlugin != null) {
			plugins.remove(jeiInternalPlugin);
			plugins.add(jeiInternalPlugin);
		}
	}

	public static <T extends IModPlugin> Optional<T> getPluginWithClass(Class<? extends T> pluginClass, List<IModPlugin> modPlugins) {
		for (IModPlugin modPlugin : modPlugins) {
			if (pluginClass.isInstance(modPlugin)) {
				T cast = pluginClass.cast(modPlugin);
				return Optional.of(cast);
			}
		}
		return Optional.empty();
	}
}
