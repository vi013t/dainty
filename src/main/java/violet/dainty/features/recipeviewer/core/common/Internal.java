package violet.dainty.features.recipeviewer.core.common;

import com.google.common.base.Preconditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import violet.dainty.features.recipeviewer.core.common.config.ClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IJeiClientConfigs;
import violet.dainty.features.recipeviewer.core.common.gui.textures.JeiSpriteUploader;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * For JEI internal use only, these are normally accessed from the API.
 */
public final class Internal {
	@Nullable
	private static Textures textures;
	@Nullable
	private static IConnectionToServer serverConnection;
	@Nullable
	private static IInternalKeyMappings keyMappings;
	@Nullable
	private static IClientToggleState toggleState;
	@Nullable
	private static IJeiClientConfigs jeiClientConfigs;
	@Nullable
	private static IJeiRuntime jeiRuntime;
	private static final JeiFeatures jeiFeatures = new JeiFeatures();

	private Internal() {

	}

	public static Textures getTextures() {
		if (textures == null) {
			Minecraft minecraft = Minecraft.getInstance();
			TextureManager textureManager = minecraft.getTextureManager();
			JeiSpriteUploader spriteUploader = new JeiSpriteUploader(textureManager);
			textures = new Textures(spriteUploader);
		}
		return textures;
	}

	public static IConnectionToServer getServerConnection() {
		Preconditions.checkState(serverConnection != null, "Server Connection has not been created yet.");
		return serverConnection;
	}

	public static void setServerConnection(IConnectionToServer serverConnection) {
		Internal.serverConnection = serverConnection;
	}

	public static IInternalKeyMappings getKeyMappings() {
		Preconditions.checkState(keyMappings != null, "Key Mappings have not been created yet.");
		return keyMappings;
	}

	public static void setKeyMappings(IInternalKeyMappings keyMappings) {
		Internal.keyMappings = keyMappings;
	}

	public static IClientToggleState getClientToggleState() {
		if (toggleState == null) {
			toggleState = new ClientToggleState();
		}
		return toggleState;
	}

	public static IJeiClientConfigs getJeiClientConfigs() {
		Preconditions.checkState(jeiClientConfigs != null, "Jei Client Configs have not been created yet.");
		return jeiClientConfigs;
	}

	public static Optional<IJeiClientConfigs> getOptionalJeiClientConfigs() {
		return Optional.ofNullable(jeiClientConfigs);
	}

	public static void setJeiClientConfigs(IJeiClientConfigs jeiClientConfigs) {
		Internal.jeiClientConfigs = jeiClientConfigs;
	}

	public static JeiFeatures getJeiFeatures() {
		return jeiFeatures;
	}

	public static void setRuntime(@Nullable IJeiRuntime jeiRuntime) {
		Internal.jeiRuntime = jeiRuntime;
	}

	public static IJeiRuntime getJeiRuntime() {
		Preconditions.checkState(jeiRuntime != null, "Jei Client Configs have not been created yet.");

		return jeiRuntime;
	}
}
