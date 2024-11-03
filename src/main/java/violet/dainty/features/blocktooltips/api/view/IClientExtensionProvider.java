package violet.dainty.features.blocktooltips.api.view;

import java.util.List;

import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.IJadeProvider;

public interface IClientExtensionProvider<IN, OUT> extends IJadeProvider {

	List<ClientViewGroup<OUT>> getClientGroups(Accessor<?> accessor, List<ViewGroup<IN>> groups);

}
