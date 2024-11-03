package violet.dainty.features.blocktooltips.api.ui;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

/**
 * A basic render-able element to be drawn in Jade's tooltip.
 *
 * @author Snownee
 */
public interface IElement {

	/**
	 * Force a size that this element reserve
	 */
	IElement size(@Nullable Vec2 size);

	/**
	 * Calculate the default reserved area of this element.
	 * <p>
	 * Modders call getCachedSize instead
	 */
	Vec2 getSize();

	Vec2 getCachedSize();

	/**
	 * Draw method for the renderer.
	 *
	 * @param guiGraphics
	 * @param x
	 * @param y
	 * @param maxX        Max width this element can expand to
	 * @param maxY        Max height this element can expand to
	 */
	void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY);

	IElement align(Align align);

	Align getAlignment();

	/**
	 * Reposition this element with an offset
	 */
	IElement translate(Vec2 translation);

	Vec2 getTranslation();

	/**
	 * Tag this element for identify by other component providers or
	 * adding transition animation if this element is a progress bar
	 */
	IElement tag(ResourceLocation tag);

	ResourceLocation getTag();

	@Nullable
	default String getMessage() {
		return null;
	}

	@Nullable
	String getCachedMessage();

	IElement clearCachedMessage();

	IElement message(@Nullable String message);

	enum Align {
		LEFT, RIGHT, CENTER;
		public static final Align[] VALUES = values();
	}
}
