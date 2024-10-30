package violet.dainty.features.witherskulls;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import violet.dainty.Dainty;
import violet.dainty.DaintyConfig;

@EventBusSubscriber(modid = Dainty.MODID)
public class WitherSkullEventHandler {

	@SubscribeEvent
	public static void onWitherSkeletonKill(LivingDropsEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer player && event.getEntity() instanceof WitherSkeleton) {
			int amountKilled = player.getStats().getValue(Stats.ENTITY_KILLED.get(EntityType.WITHER_SKELETON));
			if (amountKilled != 0 && amountKilled % DaintyConfig.witherSkeletonKillsPerGuaranteedSkull() == 0) {
				event.getDrops().add(new ItemEntity(player.level(), event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, new ItemStack(Items.WITHER_SKELETON_SKULL)));
			}
		}
	}
}
