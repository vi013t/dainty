package violet.dainty.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.bag.Bag;
import violet.dainty.features.reinforcer.Reinforcer;
import violet.dainty.features.wardenheart.WardenHeart;

public class DaintyItems {

	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Dainty.MODID);
	private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dainty.MODID);

	private static final List<DeferredItem<?>> CREATIVE_TAB_ITEMS = new ArrayList<>();

	public static final DeferredItem<Reinforcer> REINFORCER = item("reinforcer", Reinforcer::new);
	
	public static final DeferredItem<Bag> BAG = item("bag", Bag.create(Bag.Tier.BASIC));
	public static final DeferredItem<Bag> IRON_BAG = item("iron_bag", Bag.create(Bag.Tier.IRON));
	public static final DeferredItem<Item> WARDEN_HEART = item("warden_heart", WardenHeart::new);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("dainty", () -> CreativeModeTab
		.builder()
		.title(Component.translatable("itemGroup." + Dainty.MODID))
		.icon(() -> BAG.get().getDefaultInstance())
		.displayItems((parameters, output) -> {
			for (DeferredItem<? extends Item> item : CREATIVE_TAB_ITEMS) {
				output.accept(item.get().getDefaultInstance());
			}
		})
		.build()
	);

	public static <T extends Item> DeferredItem<T> item(String id, Supplier<T> supplier) {
		DeferredItem<T> deferredItem = ITEMS.register(id, supplier);
		CREATIVE_TAB_ITEMS.add(deferredItem);
		return deferredItem;
	}

	public static DeferredItem<Item> basicItem(String id) {
		DeferredItem<Item> deferredItem = ITEMS.register(id, () -> new Item(new Item.Properties()));
		CREATIVE_TAB_ITEMS.add(deferredItem);
		return deferredItem;
	}

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
		CREATIVE_TABS.register(eventBus);
	}
}
