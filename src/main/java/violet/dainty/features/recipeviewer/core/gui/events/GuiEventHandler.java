package violet.dainty.features.recipeviewer.core.gui.events;

import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.config.DebugConfig;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformScreenHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.RectDebugger;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGuiClickableArea;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientListOverlay;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.BookmarkOverlay;

public class GuiEventHandler {
	private final IngredientListOverlay ingredientListOverlay;
	private final IScreenHelper screenHelper;
	private final BookmarkOverlay bookmarkOverlay;

	public GuiEventHandler(
		IScreenHelper screenHelper,
		BookmarkOverlay bookmarkOverlay,
		IngredientListOverlay ingredientListOverlay
	) {
		this.screenHelper = screenHelper;
		this.bookmarkOverlay = bookmarkOverlay;
		this.ingredientListOverlay = ingredientListOverlay;
	}

	public void onGuiInit(Screen screen) {
		Set<ImmutableRect2i> guiExclusionAreas = screenHelper.getGuiExclusionAreas(screen)
			.map(ImmutableRect2i::new)
			.collect(Collectors.toUnmodifiableSet());
		ingredientListOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.updateExclusionAreas(guiExclusionAreas)
			.update();
		bookmarkOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.updateExclusionAreas(guiExclusionAreas)
			.update();
	}

	public void onGuiOpen(Screen screen) {
		ingredientListOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.update();
		bookmarkOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.update();
	}

	/**
	 * Draws above most ContainerScreen elements, but below the tooltips.
	 */
	public void onDrawForeground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		var poseStack = guiGraphics.pose();
		poseStack.pushPose();
		{
			IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
			poseStack.translate(-screenHelper.getGuiLeft(screen), -screenHelper.getGuiTop(screen), 0);
			bookmarkOverlay.drawOnForeground(guiGraphics, mouseX, mouseY);
			ingredientListOverlay.drawOnForeground(guiGraphics, mouseX, mouseY);
		}
		poseStack.popPose();
	}

	public void onDrawScreenPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		Minecraft minecraft = Minecraft.getInstance();

		Set<ImmutableRect2i> guiExclusionAreas = screenHelper.getGuiExclusionAreas(screen)
			.map(ImmutableRect2i::new)
			.collect(Collectors.toUnmodifiableSet());
		ingredientListOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.updateExclusionAreas(guiExclusionAreas)
			.update();
		bookmarkOverlay.getScreenPropertiesUpdater()
			.updateScreen(screen)
			.updateExclusionAreas(guiExclusionAreas)
			.update();

		DeltaTracker deltaTracker = minecraft.getTimer();
		float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
		ingredientListOverlay.drawScreen(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
		bookmarkOverlay.drawScreen(minecraft, guiGraphics, mouseX, mouseY, partialTicks);

		if (screen instanceof AbstractContainerScreen<?> guiContainer) {
			IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
			int guiLeft = screenHelper.getGuiLeft(guiContainer);
			int guiTop = screenHelper.getGuiTop(guiContainer);
			this.screenHelper.getGuiClickableArea(guiContainer, mouseX - guiLeft, mouseY - guiTop)
				.filter(IGuiClickableArea::isTooltipEnabled)
				.findFirst()
				.ifPresent(area -> {
					JeiTooltip tooltip = new JeiTooltip();
					area.getTooltip(tooltip);
					if (tooltip.isEmpty()) {
						tooltip.add(Component.translatable("dainty.tooltip.show.recipes"));
					}
					tooltip.draw(guiGraphics, mouseX, mouseY);
				});
		}

		ingredientListOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
		bookmarkOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);

		if (DebugConfig.isDebugGuisEnabled()) {
			drawDebugInfoForScreen(screen, guiGraphics);
		}
	}

	public boolean renderCompactPotionIndicators() {
		return ingredientListOverlay.isListDisplayed();
	}

	private void drawDebugInfoForScreen(Screen screen, GuiGraphics guiGraphics) {
		RectDebugger.INSTANCE.draw(guiGraphics);

		screenHelper.getGuiProperties(screen)
			.ifPresent(guiProperties -> {
				Set<Rect2i> guiExclusionAreas = screenHelper.getGuiExclusionAreas(screen)
					.collect(Collectors.toUnmodifiableSet());

				RenderSystem.disableDepthTest();

				// draw the gui exclusion areas
				for (Rect2i area : guiExclusionAreas) {
					guiGraphics.fill(
						RenderType.gui(),
						area.getX(),
						area.getY(),
						area.getX() + area.getWidth(),
						area.getY() + area.getHeight(),
						0x44FF0000
					);
				}

				// draw the gui area
				guiGraphics.fill(
					RenderType.gui(),
					guiProperties.guiLeft(),
					guiProperties.guiTop(),
					guiProperties.guiLeft() + guiProperties.guiXSize(),
					guiProperties.guiTop() + guiProperties.guiYSize(),
					0x22CCCC00
				);

				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			});
	}
}
