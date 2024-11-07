package violet.dainty.features.recipeviewer.core.gui.startup;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import violet.dainty.features.recipeviewer.core.core.util.LoggedTimer;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IngredientFilter;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientListOverlay;

public class ResourceReloadHandler implements ResourceManagerReloadListener {
	private final IngredientListOverlay ingredientListOverlay;
	private final IngredientFilter ingredientFilter;

	public ResourceReloadHandler(IngredientListOverlay ingredientListOverlay, IngredientFilter ingredientFilter) {
		this.ingredientListOverlay = ingredientListOverlay;
		this.ingredientFilter = ingredientFilter;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		LoggedTimer timer = new LoggedTimer();
		timer.start("Rebuilding ingredient filter");
		ingredientFilter.rebuildItemFilter();
		timer.stop();

		Minecraft minecraft = Minecraft.getInstance();
		ingredientListOverlay.getScreenPropertiesUpdater()
			.updateScreen(minecraft.screen)
			.update();
	}
}
