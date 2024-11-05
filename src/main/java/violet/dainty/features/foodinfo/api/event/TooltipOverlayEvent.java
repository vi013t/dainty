package violet.dainty.features.foodinfo.api.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class TooltipOverlayEvent extends Event implements ICancellableEvent
{
	/**
	 * If cancelled, will stop all rendering from happening.
	 */
	public static class Pre extends TooltipOverlayEvent
	{
		public Pre(ItemStack itemStack, FoodProperties defaultFood, FoodProperties modifiedFood)
		{
			super(itemStack, defaultFood, modifiedFood);
		}
	}

	/**
	 * If cancelled, will reserve space for the food values, but will not
	 * render them.
	 */
	public static class Render extends TooltipOverlayEvent
	{
		public Render(ItemStack itemStack, int x, int y, GuiGraphics guiGraphics, FoodProperties defaultFood, FoodProperties modifiedFood)
		{
			super(itemStack, defaultFood, modifiedFood);
			this.guiGraphics = guiGraphics;
			this.x = x;
			this.y = y;
		}

		public int x;
		public int y;
		public GuiGraphics guiGraphics;
	}

	private TooltipOverlayEvent(ItemStack itemStack, FoodProperties defaultFood, FoodProperties modifiedFood)
	{
		this.itemStack = itemStack;
		this.defaultFood = defaultFood;
		this.modifiedFood = modifiedFood;
	}

	public final FoodProperties defaultFood;
	public final FoodProperties modifiedFood;

	public final ItemStack itemStack;
}
