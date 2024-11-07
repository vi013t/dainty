package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformModHelper;

public class ModHelper implements IPlatformModHelper {
	private final Map<String, String> cache = new HashMap<>();

	@Override
	public String getModNameForModId(String modId) {
		return cache.computeIfAbsent(modId, this::computeModNameForModId);
	}

	private String computeModNameForModId(String modId) {
		return ModList.get()
			.getModContainerById(modId)
			.map(ModContainer::getModInfo)
			.map(IModInfo::getDisplayName)
			.orElseGet(() -> StringUtils.capitalize(modId));
	}

	@Override
	public boolean isInDev() {
		return !FMLLoader.isProduction();
	}
}
