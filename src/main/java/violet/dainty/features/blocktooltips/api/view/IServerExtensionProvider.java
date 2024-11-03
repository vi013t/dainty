package violet.dainty.features.blocktooltips.api.view;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.IJadeProvider;

public interface IServerExtensionProvider<T> extends IJadeProvider {

	@Nullable
	List<ViewGroup<T>> getGroups(Accessor<?> accessor);

	default boolean shouldRequestData(Accessor<?> accessor) {
		return true;
	}

}
