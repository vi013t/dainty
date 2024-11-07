package violet.dainty.features.recipeviewer.core.library.startup;

import java.util.List;

import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;

public record StartData(
	List<IModPlugin> plugins,
	IConnectionToServer serverConnection,
	IInternalKeyMappings keyBindings
) {
}
