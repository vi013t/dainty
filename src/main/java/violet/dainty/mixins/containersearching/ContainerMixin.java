package violet.dainty.mixins.containersearching;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import violet.dainty.config.DaintyConfig;

@Mixin(AbstractContainerScreen.class)
public class ContainerMixin {

	@Shadow
	private int imageWidth;

	@Shadow
	private int imageHeight;

	@Shadow
	private AbstractContainerMenu menu;

	private EditBox searchBox;

	private static final int HEIGHT = 12;
	private static final int VERTICAL_PADDING = 6;
	private static final int HORIZONTAL_PADDING = 18;
	private static final int OFFSET = 2;
	private static final int INNER_LEFT_PADDING = 2;

    @SuppressWarnings("resource")
	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"))
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo callbackInfo) {
		if (!DaintyConfig.ENABLE_INVENTORY_SEARCHING.get()) return;

		if ((Object) this instanceof AbstractContainerScreen screen && DaintyConfig.ENABLE_CONTAINER_SEARCHING.get()) {

			// Blacklisted screens
			if (this.isBlacklistedScreen()) return;

			// Create search box
			if (this.searchBox == null) {
				this.searchBox = new EditBox(
					Minecraft.getInstance().font, 
					screen.width / 2 + this.imageWidth / 2 - this.width() - HORIZONTAL_PADDING, 
					screen.height / 2 - this.imageHeight / 2 + VERTICAL_PADDING, 
					this.width(), 
					HEIGHT, 
					Component.literal("Search")
				);
				this.searchBox.setBordered(false);
			}

			// Render search box
			this.drawSearchBox(guiGraphics, mouseX, mouseY, partialTick);

			// Darken filtered slots
			if (this.searchBox.isFocused()) {
				for (int slotIndex = 0; slotIndex < this.menu.slots.size(); slotIndex++) {
					Slot slot = this.menu.slots.get(slotIndex);
					ItemStack stackInSlot = this.menu.slots.get(slotIndex).getItem();
					if (stackInSlot.isEmpty() || !stackInSlot.getDisplayName().getString().toLowerCase().contains(this.searchBox.getValue().toLowerCase())) {
						AbstractContainerScreen.renderSlotHighlight(
							guiGraphics, 
							slot.x + screen.width / 2 - this.imageWidth / 2, 
							slot.y + screen.height / 2 - this.imageHeight / 2, 
							0, 
							translucent(0x000000, 0.5f)
						);
					}
				}
			}
		}
    }
	
	@Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
		// Blacklisted screens
		if (this.isBlacklistedScreen()) return;

		this.searchBox.setFocused(this.searchBox.isHovered());
    }    
	
	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {

		// Blacklisted screens
		if (this.isBlacklistedScreen()) return;

		if (this.searchBox.isFocused()) {

			// Letter
			if (keyCode >= 'A' && keyCode <= 'Z') {
				this.searchBox.setValue(this.searchBox.getValue() + Character.toLowerCase((char) keyCode));
				callbackInfo.cancel();
			} 
			
			// Backspace
			else if (keyCode == InputConstants.KEY_BACKSPACE && !this.searchBox.getValue().isEmpty()) {
				this.searchBox.setValue(this.searchBox.getValue().substring(0, this.searchBox.getValue().length() - 1));
			}
		}
    }

	private void drawSearchBox(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

		// Bottom while line
		guiGraphics.fill(
			this.searchBox.getX() - INNER_LEFT_PADDING, 
			this.searchBox.getY() - OFFSET, 
			this.searchBox.getX() + this.searchBox.getWidth() - INNER_LEFT_PADDING,
			this.searchBox.getY() + this.searchBox.getHeight() - OFFSET, 
			translucent(0xFFFFFF, 1)
		);

		// Middle gray portion
		guiGraphics.fill(
			this.searchBox.getX() + 1 - INNER_LEFT_PADDING,
			this.searchBox.getY() + 1 - OFFSET, 
			this.searchBox.getX() + this.searchBox.getWidth() - 1 - INNER_LEFT_PADDING,
			this.searchBox.getY() + this.searchBox.getHeight() - 1 - OFFSET, 
			translucent(0xAAAAAA, 1)
		);
		
		// Top dark line
		guiGraphics.fill(
			this.searchBox.getX() - INNER_LEFT_PADDING,
			this.searchBox.getY() - OFFSET, 
			this.searchBox.getX() + this.searchBox.getWidth() - INNER_LEFT_PADDING,
			this.searchBox.getY() + 1 - OFFSET,
			translucent(0x555555, 1)
		);

		// Left dark line
		guiGraphics.fill(
			this.searchBox.getX() - INNER_LEFT_PADDING,
			this.searchBox.getY() - OFFSET, 
			this.searchBox.getX() + 1 - INNER_LEFT_PADDING, 
			this.searchBox.getY() + this.searchBox.getHeight() - OFFSET,
			translucent(0x555555, 1)
		);

		// Text itself
		searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	private static int translucent(int color, float opacity) {
		return ((int) (opacity * 255f) << 24) | (color & 0x00ffffff);
	}

	private boolean isBlacklistedScreen() {
		return ((Object) this instanceof CreativeModeInventoryScreen);
	}

	@SuppressWarnings("resource")
	public int width() {
		String title = ((AbstractContainerScreen<?>) (Object) this).getTitle().getString();
		return isPlayerInventory() ? 75 : this.imageWidth - Minecraft.getInstance().font.width(title) - HORIZONTAL_PADDING - 12;
	}

	@SuppressWarnings("resource")
	private static boolean isPlayerInventory() {
		return Minecraft.getInstance().screen instanceof InventoryScreen;
	}
}
