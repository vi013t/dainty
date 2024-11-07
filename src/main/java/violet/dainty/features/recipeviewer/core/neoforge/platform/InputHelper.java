package violet.dainty.features.recipeviewer.core.neoforge.platform;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingCategoryBuilder;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformInputHelper;
import violet.dainty.features.recipeviewer.core.neoforge.input.ForgeJeiKeyMappingCategoryBuilder;

public class InputHelper implements IPlatformInputHelper {
	@Override
	public boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Key key) {
		return keyMapping.isActiveAndMatches(key);
	}

	@Override
	public IJeiKeyMappingCategoryBuilder createKeyMappingCategoryBuilder(String name) {
		return new ForgeJeiKeyMappingCategoryBuilder(name);
	}
}
