package violet.dainty.features.durabilityhud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DurabilityHudOverlay implements Layer {

	private static ItemStack helmet;
	private static ItemStack chestplate;
	private static ItemStack leggings;
	private static ItemStack boots;
	private static ItemStack tool;
	
	private static float helmetTick = 0;
	private static float chestplateTick = 0;
	private static float leggingsTick = 0;
	private static float bootsTick = 0;
	private static float toolTick;
	
	private static final int TICKS_PER_SECOND = 20;
	private static final int SECONDS_ON_SCREEN = 3;
	private static final int RENDERED_ITEM_SIZE = 16;
	private static final int DURABILITY_STRING_PADDING = 4;
	private static final int TEXTURE_PADDING = 2;

	@Override
	@SuppressWarnings("resource")
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

		// Helmet
		if (helmet != null) {
			guiGraphics.renderItem(
				helmet, 
				TEXTURE_PADDING, 
				guiGraphics.guiHeight() / 2 - RENDERED_ITEM_SIZE * 2
			);
			guiGraphics.drawString(
				Minecraft.getInstance().font, 
				Integer.toString(helmet.getMaxDamage() - helmet.getDamageValue()), 
				TEXTURE_PADDING + RENDERED_ITEM_SIZE + DURABILITY_STRING_PADDING, 
				guiGraphics.guiHeight() / 2 - RENDERED_ITEM_SIZE * 2 + Minecraft.getInstance().font.lineHeight / 2,
				helmet.getBarColor()
			);
			helmetTick += deltaTracker.getRealtimeDeltaTicks();

			if (helmetTick > TICKS_PER_SECOND * SECONDS_ON_SCREEN) {
				helmetTick = 0;
				helmet = null;
			}
		}

		// Chestplate
		if (chestplate != null) {
			guiGraphics.renderItem(
				chestplate, 
				TEXTURE_PADDING, 
				guiGraphics.guiHeight() / 2 - RENDERED_ITEM_SIZE
			);
			guiGraphics.drawString(
				Minecraft.getInstance().font, 
				Integer.toString(chestplate.getMaxDamage() - chestplate.getDamageValue()), 
				TEXTURE_PADDING + RENDERED_ITEM_SIZE + DURABILITY_STRING_PADDING, 
				guiGraphics.guiHeight() / 2 - RENDERED_ITEM_SIZE + Minecraft.getInstance().font.lineHeight / 2,
				chestplate.getBarColor()
			);
			chestplateTick += deltaTracker.getRealtimeDeltaTicks();

			if (chestplateTick > TICKS_PER_SECOND * SECONDS_ON_SCREEN) {
				chestplateTick = 0;
				chestplate = null;
			}
		}

		// Leggings
		if (leggings != null) {
			guiGraphics.renderItem(
				leggings, 
				TEXTURE_PADDING, 
				guiGraphics.guiHeight() / 2
			);
			guiGraphics.drawString(
				Minecraft.getInstance().font, 
				Integer.toString(leggings.getMaxDamage() - leggings.getDamageValue()), 
				TEXTURE_PADDING + RENDERED_ITEM_SIZE + DURABILITY_STRING_PADDING, 
				guiGraphics.guiHeight() / 2 + Minecraft.getInstance().font.lineHeight / 2,
				leggings.getBarColor()
			);
			leggingsTick += deltaTracker.getRealtimeDeltaTicks();

			if (leggingsTick > TICKS_PER_SECOND * SECONDS_ON_SCREEN) {
				leggingsTick = 0;
				leggings = null;
			}
		}

		// Boots
		if (boots != null) {
			guiGraphics.renderItem(
				boots, 
				TEXTURE_PADDING, 
				guiGraphics.guiHeight() / 2 + RENDERED_ITEM_SIZE
			);
			guiGraphics.drawString(
				Minecraft.getInstance().font, 
				Integer.toString(boots.getMaxDamage() - boots.getDamageValue()), 
				TEXTURE_PADDING + RENDERED_ITEM_SIZE + DURABILITY_STRING_PADDING, 
				guiGraphics.guiHeight() / 2 + RENDERED_ITEM_SIZE + Minecraft.getInstance().font.lineHeight / 2,
				boots.getBarColor()
			);
			bootsTick += deltaTracker.getRealtimeDeltaTicks();

			if (bootsTick > TICKS_PER_SECOND * SECONDS_ON_SCREEN) {
				bootsTick = 0;
				boots = null;
			}
		}

		// Tool
		if (tool != null) {
			System.out.println(tool);
			guiGraphics.renderItem(
				tool, 
				TEXTURE_PADDING, 
				guiGraphics.guiHeight() / 2 + 2 * RENDERED_ITEM_SIZE
			);
			guiGraphics.drawString(
				Minecraft.getInstance().font, 
				Integer.toString(tool.getMaxDamage() - tool.getDamageValue()), 
				TEXTURE_PADDING + RENDERED_ITEM_SIZE + DURABILITY_STRING_PADDING, 
				guiGraphics.guiHeight() / 2 + 2 * RENDERED_ITEM_SIZE + Minecraft.getInstance().font.lineHeight / 2,
				tool.getBarColor()
			);
			toolTick += deltaTracker.getRealtimeDeltaTicks();

			if (toolTick > TICKS_PER_SECOND * SECONDS_ON_SCREEN) {
				toolTick = 0;
				tool = null;
			}
		}
	}

	public static void setHelmet(ItemStack newHelmet) {
		helmet = newHelmet;
		helmetTick = 0;
	}
	
	public static void setChestplate(ItemStack newChestplate) {
		chestplate = newChestplate;
		chestplateTick = 0;
	}

	public static void setLeggings(ItemStack newLeggings) {
		leggings = newLeggings;
		leggingsTick = 0;
	}
	
	public static void setBoots(ItemStack newBoots) {
		boots = newBoots;
		bootsTick = 0;
	}

	public static void setTool(ItemStack newTool) {
		tool = newTool;
		toolTick = 0;
	}

	public static boolean containsItem(Item item) {
		if (helmet != null && helmet.getItem() == item) return true;
		if (chestplate != null && chestplate.getItem() == item) return true;
		if (leggings != null && leggings.getItem() == item) return true;
		if (boots != null && boots.getItem() == item) return true;
		if (tool != null && tool.getItem() == item) return true;

		return false;
	}
}
