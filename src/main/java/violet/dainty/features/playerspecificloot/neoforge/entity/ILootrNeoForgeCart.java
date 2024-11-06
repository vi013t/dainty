package violet.dainty.features.playerspecificloot.neoforge.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.playerspecificloot.api.data.entity.ILootrCart;
import violet.dainty.features.playerspecificloot.neoforge.network.toClient.PacketCloseCart;
import violet.dainty.features.playerspecificloot.neoforge.network.toClient.PacketOpenCart;
import violet.dainty.mixins.playerspecificloot.MixinVehicleEntity;

public interface ILootrNeoForgeCart extends ILootrCart {
  @Override
  default void performOpen(ServerPlayer player) {  
    PacketDistributor.sendToPlayer(player, new PacketOpenCart(asEntity().getId()));
  }

  @Override
  default void performOpen() {
    PacketDistributor.sendToPlayersTrackingEntity(asEntity(), new PacketOpenCart(asEntity().getId()));
  }

  @Override
  default void performClose(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new PacketCloseCart(asEntity().getId()));
  }

  @Override
  default void performClose () {
    PacketDistributor.sendToPlayersTrackingEntity(asEntity(), new PacketCloseCart(asEntity().getId()));
  }

  @Override
  default void performDecay() {
    if (!(this instanceof Entity entity1)) {
      return;
    }
    if (this instanceof AbstractMinecartContainer entity2) {
      entity2.destroy(entity1.level().damageSources().fellOutOfWorld());
    } else if (this instanceof VehicleEntity entity3) {
      entity3.destroy(((MixinVehicleEntity) entity3).invokeGetDropItem());
      if (this instanceof ContainerEntity entity4) {
        entity4.chestVehicleDestroyed(entity1.level().damageSources().fellOutOfWorld(), entity1.level(), entity1);
      }
    }
  }
}
