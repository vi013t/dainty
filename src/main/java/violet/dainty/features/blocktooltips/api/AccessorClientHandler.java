package violet.dainty.features.blocktooltips.api;

import java.util.List;
import java.util.function.Function;

import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;

public interface AccessorClientHandler<T extends Accessor<?>> {

	boolean shouldDisplay(T accessor);

	List<IServerDataProvider<T>> shouldRequestData(T accessor);

	void requestData(T accessor, List<IServerDataProvider<T>> providers);

	IElement getIcon(T accessor);

	void gatherComponents(T accessor, Function<IJadeProvider, ITooltip> tooltipProvider);

	default boolean isEnabled(IToggleableProvider provider) {
		if (!IWailaConfig.get().getGeneral().getEnableAccessibilityPlugin() && JadeIds.isAccess(provider.getUid())) {
			return false;
		}
		return IWailaConfig.get().getPlugin().get(provider);
	}
}
