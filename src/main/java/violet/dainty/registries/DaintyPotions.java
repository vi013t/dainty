package violet.dainty.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.wardenpotion.WardenPotionEffect;

/**
 * Class for creating and registering custom potions and effects added by the Dainty mod.
 * 
 * <br/><br/>
 * 
 * For more information, see <a href="https://docs.neoforged.net/docs/items/mobeffects/">The relevant part of the Neoforge documentation.</a>
 */
public class DaintyPotions {

	/**
	 * The register of all custom potion effects in the mod. All custom potion effects, such as {@link #SILENT_SIGHT_EFFECT silent sight}, are registered
	 * in this registry; The registry itself is then registered to Neoforge when {@link #register(IEventBus)} is called.
	 */
	private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Dainty.MODID);

	/**
	 * The register of all custom potions in the mod. All custom potions, such as {@link #SILENT_SIGHT_POTION the silent sight potion}, are registered
	 * in this registry; The registry itself is then registered to Neoforge when {@link #register(IEventBus)} is called.
	 */
	private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, Dainty.MODID);

	/**
	 * The "silent sight" potion effect given by the {@link #SILENT_SIGHT_POTION Potion of the Deep Dark}. This potion effect makes it so that the player is
	 * immune to the "Darkness" effect applied in ancient cities, and makes their footsteps silent. It's brewed with a
	 * {@link violet.dainty.registries.DaintyItems#WARDEN_HEART warden heart}, meaning the player needs to have some ability to combat wardens to obtain it.
	 */
	public static final DeferredHolder<MobEffect, WardenPotionEffect> SILENT_SIGHT_EFFECT = MOB_EFFECTS.register("silent_sight", WardenPotionEffect::new);

	/**
	 * The standard "silent sight" potion, which applies the {@link #SILENT_SIGHT_EFFECT silent sight effect} when drank (or splashed). This potion lasts 3 minutes,
	 * and {@link #SILENT_SIGHT_POTION_LONG The long version} lasts 5 minutes.
	 */
	public static final DeferredHolder<Potion, Potion> SILENT_SIGHT_POTION = POTIONS.register("warden_potion", () -> new Potion(new MobEffectInstance(SILENT_SIGHT_EFFECT, 3600)));

	/**
	 * The standard "silent sight" potion, which applies the {@link #SILENT_SIGHT_EFFECT silent sight effect} when drank (or splashed). This potion lasts 5 minutes,
	 * and {@link #SILENT_SIGHT_POTION The short version} lasts 3 minutes.
	 */
	public static final DeferredHolder<Potion, Potion> SILENT_SIGHT_POTION_LONG = POTIONS.register("warden_potion_long", () -> new Potion(new MobEffectInstance(SILENT_SIGHT_EFFECT, 6000)));

	/**
	 * Registers the mod's custom potion effects and potions on the given event bus. This should be called exactly once in 
	 * {@link violet.dainty.Dainty#Dainty(IEventBus, net.neoforged.fml.ModContainer) the main mod class's constructor} when the mod is loaded by Neoforge.
	 * 
	 * @param eventBus The event bus given by Neoforge when loading the mod.
	 */
	public static void register(IEventBus eventBus) {
		MOB_EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}
}
