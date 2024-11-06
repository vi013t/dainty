package violet.dainty.mixins.playerspecificloot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;

@Mixin(VehicleEntity.class)
public interface MixinVehicleEntity {
  @Invoker
  Item invokeGetDropItem();
}
