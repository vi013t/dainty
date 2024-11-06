package violet.dainty.features.playerspecificloot.api.data.entity;

import net.minecraft.world.entity.vehicle.VehicleEntity;
import violet.dainty.features.playerspecificloot.api.data.ILootrInfoProvider;

public interface ILootrCart extends ILootrInfoProvider {
  @Override
  default LootrInfoType getInfoType() {
    return LootrInfoType.CONTAINER_ENTITY;
  }

  default VehicleEntity asEntity () {
    if (this instanceof VehicleEntity entity) {
      return entity;
    }
    throw new IllegalStateException("asEntity called on non-VehicleEntity ILootrCart");
  }
}
