package violet.dainty.features.recipeviewer.core.library.render;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.rendering.BatchRenderElement;
import violet.dainty.features.recipeviewer.core.library.render.batch.ItemStackBatchRendererCache;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackRenderer implements IIngredientRenderer<ItemStack> {
	private final ItemStackBatchRendererCache batchRenderer = new ItemStackBatchRendererCache();

	@Override
	public void render(GuiGraphics guiGraphics, @Nullable ItemStack ingredient) {
		render(guiGraphics, ingredient, 0, 0);
	}

	@Override
	public void render(GuiGraphics guiGraphics, @Nullable ItemStack ingredient, int posX, int posY) {
		if (ingredient != null) {
			RenderSystem.enableDepthTest();

			Minecraft minecraft = Minecraft.getInstance();
			Font font = getFontRenderer(minecraft, ingredient);
			guiGraphics.renderFakeItem(ingredient, posX, posY);
			guiGraphics.renderItemDecorations(font, ingredient, posX, posY);
			RenderSystem.disableBlend();
		}
	}

	@Override
	public void renderBatch(GuiGraphics guiGraphics, List<BatchRenderElement<ItemStack>> batchRenderElements) {
		batchRenderer.renderBatch(guiGraphics, this, batchRenderElements);
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
		return ingredient.getTooltipLines(tooltipContext, player, tooltipFlag);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, ItemStack ingredient, TooltipFlag tooltipFlag) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
		List<Component> tooltipLines = ingredient.getTooltipLines(tooltipContext, player, tooltipFlag);
		tooltip.addAll(tooltipLines);
	}

	@Override
	public Font getFontRenderer(Minecraft minecraft, ItemStack ingredient) {
		IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
		return renderHelper.getFontRenderer(minecraft, ingredient);
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}
}
