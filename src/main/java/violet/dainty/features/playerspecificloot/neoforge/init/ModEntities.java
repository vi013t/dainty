package violet.dainty.features.playerspecificloot.neoforge.init; 

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.common.entity.LootrChestMinecartEntity;

public class ModEntities {
  private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Dainty.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<LootrChestMinecartEntity>> LOOTR_MINECART_ENTITY = REGISTER.register("lootr_minecart", () -> EntityType.Builder.<LootrChestMinecartEntity>of(LootrChestMinecartEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("lootr_minecart"));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
