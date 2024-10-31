package violet.dainty.features.linearxp;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyDataAttachments;

@EventBusSubscriber(modid = Dainty.MODID)
public class LinearXPEventHandler {

	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void linearizeXP(PlayerXpEvent.PickupXp event) {
		if (event.getEntity().level().isClientSide) return;

		// Add whole levels
		float nextLevelPercent = event.getEntity().getData(DaintyDataAttachments.LINEAR_EXPERIENCE_ATTACHMENT_TYPE);
		float levelsToAdd = nextLevelPercent + ((float) event.getOrb().getValue()) / xpDifferencePerLevel();
		event.getEntity().giveExperienceLevels((int) Math.floor(levelsToAdd));

		// Store & display fractional levels
		float newLevelPercent = levelsToAdd % 1;
		event.getEntity().setData(DaintyDataAttachments.LINEAR_EXPERIENCE_ATTACHMENT_TYPE, newLevelPercent);
		event.getEntity().experienceProgress = newLevelPercent;

		// Remove orb
		event.setCanceled(true);
		event.getOrb().kill();

		// Play sound
        event.getEntity().level().playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, event.getEntity().getSoundSource(), 1, (float) (0.8 + 0.4 * Math.random()));
	}

	private static float xpDifferencePerLevel() {
		int unchangedLevel = 30;
		return totalVanillaXp(unchangedLevel) / unchangedLevel;
	}

	private static float totalVanillaXp(int level) {
		return (float) (
			level < 17 ? Math.pow(level, 2) + 6 * level 
			: level < 32 ? 2.5 * Math.pow(level, 2) - 40.5 * level + 360 
			: 4.5 * Math.pow(level, 2) - 162.5 * level - 2220
		);
	}
}
