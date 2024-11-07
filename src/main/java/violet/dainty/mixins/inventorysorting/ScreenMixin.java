package violet.dainty.mixins.inventorysorting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.inventorysorting.SortBy;
import violet.dainty.features.inventorysorting.SortPacket;
import violet.dainty.features.inventorysorting.SortPosition;
import violet.dainty.features.playerspecificloot.neoforge.init.ModBlocks;
import violet.dainty.registries.DaintyDataAttachments;
import violet.dainty.config.DaintyConfig;

@Mixin(AbstractContainerScreen.class)
public class ScreenMixin {

	private static final int BUTTON_WIDTH = 12;
	private static final int BUTTON_HEIGHT = 12;
	private static final int BUTTON_PADDING = 4;

	@Shadow
	private int imageWidth;

	@Shadow
	private int imageHeight;
	
	@SuppressWarnings({ "null", "resource" })
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"))
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo callbackInfo) {
		if (!DaintyConfig.ENABLE_INVENTORY_SORT_BUTTON.get()) return; 
		if ((Object) this instanceof AbstractContainerScreen screen) {

			// Blacklisted screens
			if (this.isBlacklisted()) return;

			// Blacklisted screens
			if (screen instanceof CreativeModeInventoryScreen) return;

			// Inventory
			if (isPlayerInventory()) {
				SortBy ordering = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_ORDER);
				guiGraphics.blit(ordering.buttonTexture(this.isButtonHovered(screen, mouseX, mouseY)), this.buttonX(screen), this.buttonY(screen), 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
				return;
			}

			// Container
			if (!hasPositionData()) return;
			SortPosition sortPosition = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE);
			BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(sortPosition.position());
			if (blockEntity == null) return;
			SortBy ordering = blockEntity.getData(DaintyDataAttachments.SORT_ORDER);
			guiGraphics.blit(ordering.buttonTexture(this.isButtonHovered(screen, mouseX, mouseY)), this.buttonX(screen), this.buttonY(screen), 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
    }
	
    @SuppressWarnings({ "null", "resource" })
	@Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {

		// Blacklisted screens
		if (this.isBlacklisted()) return;

		AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
		if (isButtonHovered(screen, mouseX, mouseY)) {
			Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value());

			// Left click - Perform sort
			if (button == 0) {

				// Perform sort on player inventory
				if (isPlayerInventory()) {
					SortBy ordering = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_ORDER);
					PacketDistributor.sendToServer(new SortPacket(new BlockPos(0, 0, 0), ordering, true));
				} 
				
				// Perform sort on container
				else {
					if (!hasPositionData()) return;
					SortPosition sortPosition = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE);
					SortBy ordering = Minecraft.getInstance().level.getBlockEntity(sortPosition.position()).getData(DaintyDataAttachments.SORT_ORDER);
					PacketDistributor.sendToServer(new SortPacket(sortPosition.position(), ordering, false));
				}
			}

			// Right click - change sorting mode
			else if (button == 1) {

				// Change sorting mode for player inventory
				if (isPlayerInventory()) {
					SortBy oldOrdering = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_ORDER);
					SortBy newOrdering = oldOrdering.next();
					Minecraft.getInstance().player.setData(DaintyDataAttachments.SORT_ORDER, newOrdering);
				} 
				
				// Change sorting mode for container
				else {
					if (!hasPositionData()) return;
					SortPosition sortPosition = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE);
					SortBy oldOrdering = Minecraft.getInstance().level.getBlockEntity(sortPosition.position()).getData(DaintyDataAttachments.SORT_ORDER);
					SortBy newOrdering = oldOrdering.next();
					Minecraft.getInstance().level.getBlockEntity(sortPosition.position()).setData(DaintyDataAttachments.SORT_ORDER, newOrdering);
				}
			}
		}
    }

	private boolean isButtonHovered(AbstractContainerScreen<?> screen, double mouseX, double mouseY) {
		int buttonX = this.buttonX(screen);
		int buttonY = this.buttonY(screen);

		return mouseX > buttonX && mouseX < buttonX + BUTTON_WIDTH && mouseY > buttonY && mouseY < buttonY +  BUTTON_HEIGHT;
	}

	private int buttonX(AbstractContainerScreen<?> screen) {
		return (screen.width - this.imageWidth) / 2 + this.imageWidth - BUTTON_WIDTH - BUTTON_PADDING;
	}

	private int buttonY(AbstractContainerScreen<?> screen) {
		return (screen.height - this.imageHeight) / 2 + BUTTON_PADDING;
	}

	@SuppressWarnings({ "null", "resource" })
	private static boolean hasPositionData() {
		if (!Minecraft.getInstance().player.hasData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE)) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("resource")
	private static boolean isPlayerInventory() {
		return Minecraft.getInstance().screen instanceof InventoryScreen;
	}

	@SuppressWarnings({ "resource", "null" })
	private boolean isBlacklisted() {
		if (Minecraft.getInstance().player == null) return false;

		// Lootr doesn't work for now...
		if (hasPositionData()) {
			SortPosition sortPosition = Minecraft.getInstance().player.getData(DaintyDataAttachments.SORT_INVENTORY_ATTACHMENT_TYPE);
			Block block = Minecraft.getInstance().level.getBlockState(sortPosition.position()).getBlock();

			if (block == ModBlocks.CHEST.get()) return true;
			if (block == ModBlocks.BARREL.get()) return true;
			if (block == ModBlocks.SHULKER.get()) return true;
			if (block == ModBlocks.TRAPPED_CHEST.get()) return true;
		}

		// No creative mode inventory
		if ((Object) this instanceof CreativeModeInventoryScreen) return true;

		// Otherwise, we're fine (probably...)
		return false;
	}
}
