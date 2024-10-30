package violet.dainty.features.wardenheart;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import violet.dainty.Dainty;
import violet.dainty.DaintyConfig;
import violet.dainty.item.DaintyItems;

@EventBusSubscriber(modid = Dainty.MODID)
public class WardenHeartEventListener {

	@SubscribeEvent
	public static void dropWardenHeart(LivingDropsEvent event) {
		if (event.getEntity() instanceof Warden && Math.random() < DaintyConfig.wardenHeartDropChance()) {
			event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, new ItemStack(DaintyItems.WARDEN_HEART.get())));
		}
	}
}
