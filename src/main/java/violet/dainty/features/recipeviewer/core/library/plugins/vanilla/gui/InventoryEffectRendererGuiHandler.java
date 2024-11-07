package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.AbstractContainerMenu;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformScreenHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGuiContainerHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class InventoryEffectRendererGuiHandler<T extends AbstractContainerMenu> implements IGuiContainerHandler<EffectRenderingInventoryScreen<T>> {
	/**
	 * Modeled after {@link DisplayEffectsScreen#drawActivePotionEffects()}
	 */
	@SuppressWarnings("JavadocReference")
	@Override
	public List<Rect2i> getGuiExtraAreas(EffectRenderingInventoryScreen<T> containerScreen) {
		if (!Internal.getJeiFeatures().getInventoryEffectRendererGuiHandlerEnabled()) {
			return List.of();
		}
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) {
			return Collections.emptyList();
		}
		Collection<MobEffectInstance> activePotionEffects = player.getActiveEffects();
		if (activePotionEffects.isEmpty()) {
			return Collections.emptyList();
		}

		IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
		List<Rect2i> areas = new ArrayList<>();
		int x = screenHelper.getGuiLeft(containerScreen) + screenHelper.getXSize(containerScreen) + 2;
		int y = screenHelper.getGuiTop(containerScreen);
		// JEI always forces the potion effect renderer to "compact" width mode when JEI is open.
		int width = 32;

		int height = 33;
		if (activePotionEffects.size() > 5) {
			height = 132 / (activePotionEffects.size() - 1);
		}
		IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
		for (MobEffectInstance potionEffect : activePotionEffects) {
			if (renderHelper.shouldRender(potionEffect)) {
				areas.add(new Rect2i(x, y, width, height));
				y += height;
			}
		}
		return areas;
	}
}
