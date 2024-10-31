package violet.dainty.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.wardenpotion.WardenPotionEffect;

public class DaintyPotions {

	private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Dainty.MODID);
	private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, Dainty.MODID);

	public static final DeferredHolder<MobEffect, WardenPotionEffect> WARDEN_EFFECT = MOB_EFFECTS.register("warden_potion_effect", WardenPotionEffect::new);
	public static final DeferredHolder<Potion, Potion> WARDEN_POTION = POTIONS.register("warden_potion", () -> new Potion(new MobEffectInstance(WARDEN_EFFECT, 3600)));
	public static final DeferredHolder<Potion, Potion> LONG_WARDEN_POTION = POTIONS.register("warden_potion_long", () -> new Potion(new MobEffectInstance(WARDEN_EFFECT, 6000)));

	public static void register(IEventBus eventBus) {
		MOB_EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}
}
