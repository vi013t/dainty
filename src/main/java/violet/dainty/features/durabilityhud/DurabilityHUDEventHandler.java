package violet.dainty.features.durabilityhud;

import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID)
public class DurabilityHudEventHandler {

	@SubscribeEvent
	public static void showArmor(ArmorHurtEvent event) {
		DurabilityHudOverlay.setHelmet(event.getArmorItemStack(EquipmentSlot.HEAD));
		DurabilityHudOverlay.setChestplate(event.getArmorItemStack(EquipmentSlot.CHEST));
		DurabilityHudOverlay.setLeggings(event.getArmorItemStack(EquipmentSlot.LEGS));
		DurabilityHudOverlay.setBoots(event.getArmorItemStack(EquipmentSlot.FEET));
	}
}
