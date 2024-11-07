package violet.dainty.features.recipeviewer.core.neoforge.input;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.input.KeyNameUtil;
import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingInternal;

public class NeoForgeJeiKeyMapping implements IJeiKeyMappingInternal {
	private final KeyMapping keyMapping;

	public NeoForgeJeiKeyMapping(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
	}

	@Override
	public boolean isActiveAndMatches(InputConstants.Key key) {
		return keyMapping.isActiveAndMatches(key);
	}

	@Override
	public boolean isUnbound() {
		return keyMapping.isUnbound();
	}

	@Override
	public Component getTranslatedKeyMessage() {
		InputConstants.Key key = keyMapping.getKey();
		return keyMapping.getKeyModifier().getCombinedName(key, () -> KeyNameUtil.getKeyDisplayName(key));
	}

	@Override
	public IJeiKeyMappingInternal register(Consumer<KeyMapping> registerMethod) {
		registerMethod.accept(keyMapping);
		return this;
	}
}
