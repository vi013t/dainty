package violet.dainty.mixins.playerspecificloot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;

@Mixin(PoiType.class)
public class MixinPoiType {
  @Unique
  private boolean fishermanCheck;
  @Unique
  private boolean isFisherman;

  @Inject(method = "is", at = @At(value = "RETURN"), cancellable = true)
  private void LootrGetBlockStates(BlockState state, CallbackInfoReturnable<Boolean> cir) {
    PoiType thisPoi = (PoiType) (Object) this;
    if (!fishermanCheck) {
      fishermanCheck = true;
      isFisherman = PoiTypes.FISHERMAN.location().equals(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getKey(thisPoi));
    }
    if (isFisherman) {
      if (state.is(LootrRegistry.getBarrelBlock())) {
        cir.setReturnValue(true);
        cir.cancel();
      }
    }
  }
}
