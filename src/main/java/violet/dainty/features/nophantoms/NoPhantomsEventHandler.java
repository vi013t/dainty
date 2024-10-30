package violet.dainty.features.nophantoms;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.SpawnClusterSizeEvent;
import violet.dainty.Dainty;
import violet.dainty.DaintyConfig;

@EventBusSubscriber(modid=Dainty.MODID)
public class NoPhantomsEventHandler {

	/**
	 * Makes it so that endermen can drop phantom membrane, since phantoms are removed.
	 * 
	 * @param event The {@link LivingDropsEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void makeEndermanDropPhantomMembrane(LivingDropsEvent event) {
		if (event.getEntity() instanceof EnderMan enderman && !enderman.level().isClientSide() && Math.random() < DaintyConfig.endermanPhantomMembraneDropChance()) {
			event.getDrops().add(new ItemEntity(enderman.level(), enderman.getX(), enderman.getY(), enderman.getZ(), new ItemStack(Items.PHANTOM_MEMBRANE)));
		}
	}
	
	/**
	 * Disables phantom spawning by listening for a spawn cluster and setting the size to 0.
	 * 
	 * @param event The {@link SpawnClusterSizeEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void disablePhantomSpawns(SpawnClusterSizeEvent event) {
		if (event.getEntity() instanceof Phantom && DaintyConfig.disablePhantoms()) {
			event.setSize(0);
		}
	}
}
