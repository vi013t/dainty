package violet.dainty.features.zoom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * {@code ZoomData} is a data attachment applied to the player on the logical client that contains
 * data about how far they're zooming in. This data is
 * {@link violet.dainty.features.zoom.ZoomEventHandler#checkForZoom(net.neoforged.neoforge.client.event.ClientTickEvent.Post)
 * attached when} the player holds {@link violet.dainty.registries.DaintyKeyBindings#ZOOM the zoom keybinding}, modified
 * {@link violet.dainty.features.zoom.ZoomEventHandler#adjustZoom(net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent)
 * when the player scrolls their mouse while zooming}, and read 
 * {@link violet.dainty.features.zoom.ZoomEventHandler#zoom(net.neoforged.neoforge.client.event.ComputeFovModifierEvent)
 * when the game calculates the player's field of view}. All of these things happen only on the logical client, so this
 * data attachment never needs to be applied on the logical server. For that reason there exists no packet or packet
 * handler for this data, and you should never try to read or attach it from the logical server. This is also why this record
 * doesn't have a {@link net.minecraft.network.codec.StreamCodec StreamCodec}; So don't try to synchronize it between
 * logical sides or interact with it in any way from the logical server.
 * 
 * <br/><br/>
 * 
 * To attach or read this data, use {@link violet.dainty.registries.DaintyDataAttachments#ZOOM the zoom data attachment}. See
 * {@link violet.dainty.features.zoom.ZoomEventHandler#checkForZoom(net.neoforged.neoforge.client.event.ClientTickEvent.Post)
 * the logic in the event handler} for an example.
 */
public record ZoomData(float zoomFactor) {

	/**
	 * The {@link Codec} for serializing {@link ZoomData} attachments.
	 */
	public static final Codec<ZoomData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("isZooming").forGetter(ZoomData::zoomFactor)
	).apply(instance, ZoomData::new));

	/**
	 * Returns a new {@link ZoomData} instance representing a player that's not zooming in at all. 
	 * 
	 * @return The new zoom data instance
	 */
	public static ZoomData none() {
		return new ZoomData(1);
	}

	/**
	 * Returns a new {@link ZoomData} instance representing the "base" zoom amount; That is, the zoom amount
	 * when the zoom key is first held, before scrolling to zoom in further at all.
	 * 
	 * @return The new zoom data instance
	 */
	public static ZoomData base() {
		return new ZoomData(0.3f);
	}

	/**
	 * Returns whether this zoom data represents a player that's not zooming in at all.
	 * 
	 * @return
	 */
	public boolean isZooming() {
		return this.zoomFactor != 1;
	}

	/**
	 * Zooms in further by the given amount. The formula is 1 / totalZoom, where totalZoom
	 * is the current zoom value plus the given amount.
	 * 
	 * <br/><br/>
	 * 
	 * This is used by 
	 * {@link violet.dainty.features.zoom.ZoomEventHandler#adjustZoom(net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent)
	 * The corresponding part of the zoom event handler}. when the player scrolls while zoomed
	 * in.
	 * 
	 * <br/><br/>
	 * 
	 * This method returns a new instance of {@link ZoomData} and leaves this one unmodified,
	 * because data attachments generally should be treated as immutable; Though it isn't strictly
	 * necessarily like it is with data components. See
	 * <a href="https://docs.neoforged.net/docs/datastorage/attachments/">the corresponding part 
	 * of the Neoforge docs</a> for more information.
	 * 
	 * @param adjustmentFactor The amount to adjust the zoom by.
	 * 
	 * @return the new {@link ZoomData} instance after zooming in.
	 */
	public ZoomData adjustZoom(double adjustmentFactor) {
		double x = 1d / this.zoomFactor();
		double newX = x + adjustmentFactor;
		double y = 1d / newX;
		return new ZoomData((float) Math.min(ZoomData.base().zoomFactor(), y));
	}
}
