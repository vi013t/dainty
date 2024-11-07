package violet.dainty.features.recipeviewer.core.common.platform;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingCategoryBuilder;

public interface IPlatformInputHelper {
	boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Key key);

	IJeiKeyMappingCategoryBuilder createKeyMappingCategoryBuilder(String name);
}
