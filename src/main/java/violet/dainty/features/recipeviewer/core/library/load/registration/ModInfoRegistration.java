package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.google.common.collect.ImmutableSetMultimap;

import violet.dainty.features.recipeviewer.core.commonapi.registration.IModInfoRegistration;
import violet.dainty.features.recipeviewer.core.core.collect.SetMultiMap;

import java.util.Collection;

public class ModInfoRegistration implements IModInfoRegistration {
	private final SetMultiMap<String, String> modAliases = new SetMultiMap<>();

	@Override
	public void addModAliases(String modId, Collection<String> aliases) {
		modAliases.putAll(modId, aliases);
	}

	public ImmutableSetMultimap<String, String> getModAliases() {
		return modAliases.toImmutable();
	}
}
