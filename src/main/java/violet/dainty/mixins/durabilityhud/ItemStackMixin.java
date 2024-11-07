package violet.dainty.mixins.durabilityhud;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.durabilityhud.DurabilityHudOverlay;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@SuppressWarnings({ "null", "resource" })
	@Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("RETURN"))
	public void hurtAndBreak(int damage, ServerLevel level, LivingEntity entity, Consumer<Item> itemConsumer, CallbackInfo CallbackInfo) {
		if ((Object) this instanceof ItemStack stack && Minecraft.getInstance().player != null) {
			if (Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(stack.getItem()) || Minecraft.getInstance().player.getItemInHand(InteractionHand.OFF_HAND).getItem().equals(stack.getItem())) {
				if (!DurabilityHudOverlay.containsItem(stack.getItem())) {
					DurabilityHudOverlay.setTool(stack);
				}
			}
		}
	}
}