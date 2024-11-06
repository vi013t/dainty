package violet.dainty.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
import violet.dainty.features.friendlyberries.CookedBerries;
import violet.dainty.features.reinforcer.Reinforcer;
import violet.dainty.features.wardenheart.WardenHeart;
import violet.dainty.features.witherskulls.WitherSkullFragmentItem;

/**
 * Class that holds the custom items (and corresponding registries) added by Dainty. All items that are added into the mod are created here and referenced from here.
 */
public class DaintyItems {

	/**
	 * The item registry for the Dainty mod. All items that are added into the mod are registered here, typically through {@link #item(String, Supplier)}. As per
	 * usual with items, each item must be registered with a unique string identifier (different to all other items in the mod; Not necessarily distinct from items
	 * in other mods in the same pack), and a supplier that creates an instance of the item when called.
	 * 
	 * <br/><br/>
	 * 
	 * See <a href="https://docs.neoforged.net/docs/items/">The corresponding section of the Neoforge docs</a> for more information.
	 */
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Dainty.MODID);
	private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dainty.MODID);
	public static final List<DeferredItem<?>> CREATIVE_TAB_ITEMS = new ArrayList<>();

	public static final DeferredItem<Bag> BAG = item("bag", Bag.create(Bag.Tier.BASIC));
	public static final DeferredItem<Bag> IRON_BAG = item("iron_bag", Bag.create(Bag.Tier.IRON));
	public static final DeferredItem<Bag> GOLD_BAG = item("gold_bag", Bag.create(Bag.Tier.GOLD));
	public static final DeferredItem<Bag> DIAMOND_BAG = item("diamond_bag", Bag.create(Bag.Tier.DIAMOND));
	public static final DeferredItem<Bag> NETHERITE_BAG = item("netherite_bag", Bag.create(Bag.Tier.NETHERITE));
	public static final DeferredItem<WitherSkullFragmentItem> SMALL_WITHER_SKULL_FRAGMENT = item("wither_skull_fragment_small", WitherSkullFragmentItem.create());
	public static final DeferredItem<WitherSkullFragmentItem> LARGE_WITHER_SKULL_FRAGMENT = item("wither_skull_fragment_large", WitherSkullFragmentItem.create());
	public static final DeferredItem<CookedBerries> COOKED_BERRIES = item("cooked_berries", CookedBerries::new);
	
	/**
	 * The reinforcer item. This is an item that can be combined with any other item in an anvil to make it unbreakable.
	 * The anvil recipe logic is handled in 
	 * {@link violet.dainty.features.reinforcer.ReinforcerEventHandler#createAnvilResult(net.neoforged.neoforge.event.AnvilUpdateEvent)
	 * the corresponding part of the reinforcer event handler}.
	 */
	public static final DeferredItem<Reinforcer> REINFORCER = item("reinforcer", Reinforcer::new);

	/**
	 * The "warden heart" item. This is an item dropped by wardens, with its drop chance configurable via {@link violet.dainty.config.DaintyConfig#wardenHeartDropChance()}
	 * (guaranteed by default). The item itself does nothing; It's just a crafting ingredient for later-game items.
	 * 
	 * <br/><br/>
	 * 
	 * See {@link violet.dainty.features.wardenheart.WardenHeart} for more information.
	 */
	public static final DeferredItem<WardenHeart> WARDEN_HEART = item("warden_heart", WardenHeart::new);

	/**
	 * The single creative tab for items from the Dainty mod. All custom items added by the mod are stored in this creative tab.
	 */
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

	/**
	 * Creates a deferred item by registering it with {@link #ITEMS the Dainty item registry}.
	 * 
	 * @param <T> The type of item being created
	 * 
	 * @param id The item ID; This must be unique to all other items in the mod
	 * @param supplier A function that produces a new instance of the item
	 * 
	 * @return The item as a registered deferred item.
	 */
	private static <T extends Item> DeferredItem<T> item(String id, Supplier<T> supplier) {
		DeferredItem<T> deferredItem = ITEMS.register(id, supplier);
		CREATIVE_TAB_ITEMS.add(deferredItem);
		return deferredItem;
	}

	/**
	 * Registers the mod's custom items. This should be called exactly once during 
	 * {@link violet.dainty.Dainty#Dainty(IEventBus, net.neoforged.fml.ModContainer) the main class's constructor} at
	 * the time of mod initialization.
	 * 
	 * @param eventBus The event bus supplied by Neoforge.
	 */
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
		CREATIVE_TABS.register(eventBus);
	}
}
