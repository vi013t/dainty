package violet.dainty.registries;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.stonefurnace.StoneFurnaceBlockEntity;

public class DaintyBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Dainty.MODID);

	@SuppressWarnings("null")
	public static final Supplier<BlockEntityType<StoneFurnaceBlockEntity>> STONE_FURNACE = BLOCK_ENTITIES.register(
        "stone_furnace",
        () -> BlockEntityType.Builder.of(StoneFurnaceBlockEntity::new, DaintyBlocks.STONE_FURNACE.get()).build(null)
	);

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}
