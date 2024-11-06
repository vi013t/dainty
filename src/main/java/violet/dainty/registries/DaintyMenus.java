package violet.dainty.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.stonefurnace.StoneFurnaceMenu;

public class DaintyMenus {

	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Dainty.MODID);
	
	public static final DeferredHolder<MenuType<?>, MenuType<StoneFurnaceMenu>> STONE_FURNACE = MENUS.register(
		"stone_furnace", 
		() -> new MenuType<>(StoneFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS)
	);

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}
