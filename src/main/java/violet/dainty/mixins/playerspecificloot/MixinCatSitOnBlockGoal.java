package violet.dainty.mixins.playerspecificloot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrChestBlockEntity;

@Mixin(CatSitOnBlockGoal.class)
public class MixinCatSitOnBlockGoal {
  @Redirect(method = "isValidTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
  protected boolean LootrIsIn(BlockState state, Block block) {
    return state.is(block) || state.is(LootrRegistry.getChestBlock()) || state.is(LootrRegistry.getTrappedChestBlock());
  }

  @Inject(method = "isValidTarget", at = @At(target = "Lnet/minecraft/world/level/block/entity/ChestBlockEntity;getOpenCount(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)I", value = "INVOKE"), cancellable = true)
  protected void LootrPlayersUsing(LevelReader reader, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
    if (LootrChestBlockEntity.getOpenCount(reader, pos) < 1) {
      info.setReturnValue(true);
      info.cancel();
    }
  }
}
