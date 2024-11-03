package violet.dainty.features.witherskulls;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyItems;

@EventBusSubscriber(modid = Dainty.MODID)
public class WitherSkullEventHandler {

	@SubscribeEvent
	public static void onWitherSkeletonKill(LivingDropsEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer player && event.getEntity() instanceof WitherSkeleton) {
			int smallDropAmount = 1 + (int) (Math.random() * 3d);
			int largeDropAmount = Math.random() < 0.1 ? 2 : Math.random() < 0.25 ? 1 : 0;
			event.getDrops().add(new ItemEntity(player.level(), event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, new ItemStack(DaintyItems.SMALL_WITHER_SKULL_FRAGMENT.get(), smallDropAmount)));
			event.getDrops().add(new ItemEntity(player.level(), event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, new ItemStack(DaintyItems.LARGE_WITHER_SKULL_FRAGMENT.get(), largeDropAmount)));
		}
	}
}
