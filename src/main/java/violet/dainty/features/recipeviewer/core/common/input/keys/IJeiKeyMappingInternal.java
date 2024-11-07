package violet.dainty.features.recipeviewer.core.common.input.keys;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.Consumer;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;

public interface IJeiKeyMappingInternal extends IJeiKeyMapping {
	@Override
	boolean isActiveAndMatches(InputConstants.Key key);

	@Override
	boolean isUnbound();

	@Override
	Component getTranslatedKeyMessage();

	IJeiKeyMapping register(Consumer<KeyMapping> registerMethod);
}
