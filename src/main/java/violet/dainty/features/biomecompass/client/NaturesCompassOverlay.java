package violet.dainty.features.biomecompass.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;
import violet.dainty.features.biomecompass.util.BiomeUtils;
import violet.dainty.features.biomecompass.util.CompassState;
import violet.dainty.features.biomecompass.util.ItemUtils;
import violet.dainty.features.biomecompass.util.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class NaturesCompassOverlay implements LayeredDraw.Layer {
	
	public static final Minecraft mc = Minecraft.getInstance();

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		if (mc.player != null && mc.level != null && !mc.options.hideGui && !mc.getDebugOverlay().showDebugScreen() && (mc.screen == null || mc.screen instanceof ChatScreen)) {
			final Player player = mc.player;
			final ItemStack stack = ItemUtils.getHeldNatureCompass(player);
			if (stack != null && stack.getItem() instanceof NaturesCompassItem) {
				final NaturesCompassItem compass = (NaturesCompassItem) stack.getItem();
				if (compass.getState(stack) == CompassState.SEARCHING) {
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.status"), 5, 5, 0xFFFFFF, 0);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.searching"), 5, 5, 0xAAAAAA, 1);

					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.biome"), 5, 5, 0xFFFFFF, 3);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, BiomeUtils.getBiomeName(mc.level, compass.getBiomeKey(stack)), 5, 5, 0xAAAAAA, 4);
					
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.radius"), 5, 5, 0xFFFFFF, 6);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, String.valueOf(compass.getSearchRadius(stack)), 5, 5, 0xAAAAAA, 7);
				} else if (compass.getState(stack) == CompassState.FOUND) {
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.status"), 5, 5, 0xFFFFFF, 0);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.found"), 5, 5, 0xAAAAAA, 1);

					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.biome"), 5, 5, 0xFFFFFF, 3);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, BiomeUtils.getBiomeName(mc.level, compass.getBiomeKey(stack)), 5, 5, 0xAAAAAA, 4);

					if (compass.shouldDisplayCoordinates(stack)) {
						RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.coordinates"), 5, 5, 0xFFFFFF, 6);
						RenderUtils.drawConfiguredStringOnHUD(guiGraphics, compass.getFoundBiomeX(stack) + ", " + compass.getFoundBiomeZ(stack), 5, 5, 0xAAAAAA, 7);
	
						RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.distance"), 5, 5, 0xFFFFFF, 9);
						RenderUtils.drawConfiguredStringOnHUD(guiGraphics, String.valueOf(BiomeUtils.getDistanceToBiome(player, compass.getFoundBiomeX(stack), compass.getFoundBiomeZ(stack))), 5, 5, 0xAAAAAA, 10);
					}
				} else if (compass.getState(stack) == CompassState.NOT_FOUND) {
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.status"), 5, 5, 0xFFFFFF, 0);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.notFound"), 5, 5, 0xAAAAAA, 1);

					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.biome"), 5, 5, 0xFFFFFF, 3);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, BiomeUtils.getBiomeName(mc.level, compass.getBiomeKey(stack)), 5, 5, 0xAAAAAA, 4);

					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.radius"), 5, 5, 0xFFFFFF, 6);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, String.valueOf(compass.getSearchRadius(stack)), 5, 5, 0xAAAAAA, 7);

					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, I18n.get("string.naturescompass.samples"), 5, 5, 0xFFFFFF, 9);
					RenderUtils.drawConfiguredStringOnHUD(guiGraphics, String.valueOf(compass.getSamples(stack)), 5, 5, 0xAAAAAA, 10);
				}
			}
		}
	}

}