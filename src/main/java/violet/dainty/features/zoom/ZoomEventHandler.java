package violet.dainty.features.zoom;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import violet.dainty.Dainty;
import violet.dainty.config.DaintyConfig;
import violet.dainty.registries.DaintyDataAttachments;
import violet.dainty.registries.DaintyKeyBindings;

/**
 * The event handler for the zoom feature. This handles the logic for {@link #checkForZoom(net.neoforged.neoforge.client.event.ClientTickEvent.Post)
 * checking if the zoom keybinding is pressed} (and attaching the appropriate {@link DaintyDataAttachments#ZOOM data attachment}),
 * {@link #zoom(ComputeFovModifierEvent) rendering the zoom effect}, {@link #adjustZoom(MouseScrollingEvent) handling scrolling to zoom further},
 * and {@link #renderHand(RenderHandEvent) disabling hand rendering}. Zooming is purely a client-side feature, and all functionality in
 * this class only runs on the logical client.
 */
@EventBusSubscriber(modid = Dainty.MODID)
public class ZoomEventHandler {
	
	/**
	 * Called by Neoforge when the game listens for field of view (FOV) changes (every tick); See the {@link ComputeFovModifierEvent}
	 * documentation for more details about when this is fired. This method will lower the FOV dramatically if the player is currently
	 * "zooming in", as detected by {@link #checkForZoom(net.neoforged.neoforge.client.event.ClientTickEvent.Post)}, which runs every
	 * tick on the logical client.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The {@link ComputeFovModifierEvent} given by Neoforge.
	 */
	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void zoom(ComputeFovModifierEvent event) {
		if (Minecraft.getInstance().player == null || !DaintyConfig.ENABLE_ZOOMING.get()) return;

		@SuppressWarnings("null")
		ZoomData zoomData = Minecraft.getInstance().player.getData(DaintyDataAttachments.ZOOM);

		// Player is zooming - adjust FOV to create zoom effect
		if (zoomData.isZooming()) {
			event.setNewFovModifier(zoomData.zoomFactor());
		}

	}

	/**
	 * Called when the game attempts to render the player's hand. This method checks if the player is zooming, and if so,
	 * tells the game not to render the player's hand, because the hand shouldn't render when zooming.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The {@link RenderHandEvent} given by Neoforge.
	 */
	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void renderHand(RenderHandEvent event) {
		if (Minecraft.getInstance().player == null) return;

		@SuppressWarnings("null")
		ZoomData zoomData = Minecraft.getInstance().player.getData(DaintyDataAttachments.ZOOM);

		// Player is zooming - don't render hand
		if (zoomData.isZooming()) {
			event.setCanceled(true);
		}
	}

	/**
	 * Called every tick on the logical client by Neoforge. This method checks if the player is holding the zoom
	 * keybinding, and applies the appropriate {@link ZoomData} to the player as a {@link DaintyDataAttachments#ZOOM 
	 * zoom data attachment}.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The {@link ClientTickEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	@SuppressWarnings({ "resource", "null" })
	public static void checkForZoom(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().player == null) return;

		// Player is zooming - attach {@link ZoomData}.
		if (DaintyKeyBindings.ZOOM.get().isDown()) {
			ZoomData zoomData = Minecraft.getInstance().player.getData(DaintyDataAttachments.ZOOM);
			if (!zoomData.isZooming()) Minecraft.getInstance().player.setData(DaintyDataAttachments.ZOOM, ZoomData.base());
		} 
		
		// Player isn't zooming - give the player empty zoom data
		else {
			Minecraft.getInstance().player.setData(DaintyDataAttachments.ZOOM, ZoomData.none());
		}
	}	

	/**
	 * Adjusts the zoom to zoom in our out further when the player scrolls their scroll wheel.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The {@link MouseScrollingEvent} fired by Neoforge
	 */
	@SubscribeEvent
	@SuppressWarnings({ "null", "resource" })
	public static void adjustZoom(MouseScrollingEvent event) {
		if (Minecraft.getInstance().player == null) return;

		if (DaintyKeyBindings.ZOOM.get().isDown()) {
			ZoomData zoomData = Minecraft.getInstance().player.getData(DaintyDataAttachments.ZOOM);
			ZoomData newZoomData = zoomData.adjustZoom(event.getScrollDeltaY());
			Minecraft.getInstance().player.setData(DaintyDataAttachments.ZOOM, newZoomData);
			event.setCanceled(true);
		}
	}
}
