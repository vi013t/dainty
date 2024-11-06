package violet.dainty.registries;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.stonefurnace.StoneFurnaceBlock;

public class DaintyBlocks {
	
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Dainty.MODID);

	public static final DeferredBlock<StoneFurnaceBlock> STONE_FURNACE = block("stone_furnace", StoneFurnaceBlock::new);

	private static <T extends Block> DeferredBlock<T> block(String name, Supplier<T> supplier) {
		DeferredBlock<T> registryBlock = BLOCKS.register(name, supplier);
		DeferredItem<BlockItem> blockItem = DaintyItems.ITEMS.register(name, () -> new BlockItem(registryBlock.get(), new Item.Properties()));
		DaintyItems.CREATIVE_TAB_ITEMS.add(blockItem);
		return registryBlock;
	}

	public static void register(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
	}
}
