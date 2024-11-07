package violet.dainty.features.recipeviewer.core.gui.plugins;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.JeiPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IGuiHandlerRegistration;
import violet.dainty.features.recipeviewer.core.gui.GuiProperties;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipesGui;

@JeiPlugin
public class JeiGuiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "gui");
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiScreenHandler(AbstractContainerScreen.class, GuiProperties::create);
		registration.addGuiScreenHandler(RecipesGui.class, RecipesGui::getProperties);
	}
}
