package violet.dainty.mixins.containersearching;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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

	private static final int WIDTH = 100;
	private static final int HEIGHT = 10;
	private static final int PADDING = 5;

    @SuppressWarnings("resource")
	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"))
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo callbackInfo) {
		if ((Object) this instanceof AbstractContainerScreen screen && DaintyConfig.ENABLE_CONTAINER_SEARCHING.get()) {
			if (this.searchBox == null) {
				this.searchBox = new EditBox(Minecraft.getInstance().font, screen.width / 2 - WIDTH / 2, screen.height / 2 - this.imageHeight / 2 + PADDING, WIDTH, HEIGHT, Component.literal("Search"));
			}
			searchBox.render(guiGraphics, mouseX, mouseY, partialTick);

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
		this.searchBox.setFocused(this.searchBox.isHovered());
    }    
	
	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.searchBox.isFocused()) {
			if (keyCode >= 'A' && keyCode <= 'Z') {
				this.searchBox.setValue(this.searchBox.getValue() + Character.toLowerCase((char) keyCode));
				callbackInfo.cancel();
			}
		}
    }

	private static int translucent(int color, float opacity) {
		return ((int) (opacity * 255f) << 24) | (color & 0x00ffffff);
	}
}
