package violet.dainty.features.zoom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
