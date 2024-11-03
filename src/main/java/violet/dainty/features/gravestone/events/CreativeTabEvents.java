package violet.dainty.features.gravestone.events;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import violet.dainty.features.gravestone.Gravestone;

public class CreativeTabEvents {

    @SubscribeEvent
    public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(new ItemStack(Gravestone.GRAVESTONE.get()));
        }
    }

}
