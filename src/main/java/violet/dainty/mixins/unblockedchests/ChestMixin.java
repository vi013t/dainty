package violet.dainty.mixins.unblockedchests;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import violet.dainty.config.DaintyConfig;

@Mixin(ChestBlock.class)
public class ChestMixin {

	@Inject(method = "isChestBlockedAt", at = @At("HEAD"), cancellable = true)
    private static void isChestBlockedAt(LevelAccessor level, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (DaintyConfig.ENABLE_OPENING_BLOCKED_CHESTS.get()) {
			callbackInfo.setReturnValue(false);
		}
    }
}
