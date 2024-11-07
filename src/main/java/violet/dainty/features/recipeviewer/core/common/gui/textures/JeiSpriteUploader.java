package violet.dainty.features.recipeviewer.core.common.gui.textures;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Constants;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public class JeiSpriteUploader extends TextureAtlasHolder {
	public JeiSpriteUploader(TextureManager textureManager) {
		super(textureManager, Constants.LOCATION_JEI_GUI_TEXTURE_ATLAS, ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "gui"));
	}

	/**
	 * Overridden to make it public
	 */
	@Override
	public TextureAtlasSprite getSprite(ResourceLocation location) {
		return super.getSprite(location);
	}

}
