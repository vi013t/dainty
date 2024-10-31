package violet.dainty.features.nophantoms;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent.Result;
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
	 * Disables phantom spawning.
	 * 
	 * @param event The {@link PlayerSpawnPhantomsEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void disablePhantomSpawns(PlayerSpawnPhantomsEvent event) {
		event.setResult(Result.DENY);
	}
}
