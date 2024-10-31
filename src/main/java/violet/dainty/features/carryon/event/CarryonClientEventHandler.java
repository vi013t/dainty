package violet.dainty.features.carryon.event;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import violet.dainty.Dainty;
import violet.dainty.features.carryon.CarryOnCommonClient;
import violet.dainty.features.carryon.CarryOnData;
import violet.dainty.features.carryon.CarryOnDataManager;
import violet.dainty.features.carryon.client.CarriedObjectRender;
import violet.dainty.features.carryon.client.CarryRenderHelper;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = Dainty.MODID, value = Dist.CLIENT)
public class CarryonClientEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void renderHand(RenderHandEvent event) {
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		MultiBufferSource buffer = event.getMultiBufferSource();
		PoseStack matrix = event.getPoseStack();
		int light = event.getPackedLight();
		float partialTicks = event.getPartialTick();

		if (CarriedObjectRender.drawFirstPerson(player, buffer, matrix, light, partialTicks) && CarryRenderHelper.getPerspective() == 0) event.setCanceled(true);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) CarriedObjectRender.drawThirdPerson(event.getPartialTick().getGameTimeDeltaPartialTick(true), event.getPoseStack().last().pose());
	}

	@SuppressWarnings("null")
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onGuiInit(ScreenEvent.Init.Pre event) {
		if (event.getScreen() != null) {
			boolean inventory = event.getScreen() instanceof AbstractContainerScreen;
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;

			if (player != null && inventory) {
				CarryOnData carry = CarryOnDataManager.getCarryData(player);
				if (carry.isCarrying()) {
					mc.player.closeContainer();
					mc.screen = null;
					mc.mouseHandler.grabMouse();
					event.setCanceled(true);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		CarryOnCommonClient.checkForKeybinds();
		CarryOnCommonClient.onCarryClientTick();
	}
}