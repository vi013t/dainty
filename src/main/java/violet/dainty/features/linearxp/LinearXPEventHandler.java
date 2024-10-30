package violet.dainty.features.linearxp;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import violet.dainty.Dainty;
import violet.dainty.data.DaintyDataAttachments;

@EventBusSubscriber(modid = Dainty.MODID)
public class LinearXPEventHandler {

	@SubscribeEvent
	@SuppressWarnings("resource")
	public static void linearizeXP(PlayerXpEvent.PickupXp event) {
		if (event.getEntity().level().isClientSide) return;

		// Add whole levels
		float nextLevelPercent = event.getEntity().getData(DaintyDataAttachments.LINEAR_EXPERIENCE_ATTACHMENT_TYPE);
		float levelsToAdd = nextLevelPercent + ((float) event.getOrb().getValue()) / 46.5f;
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
}
