package violet.dainty.features.wardenpotion;

import java.util.List;

import com.mojang.datafixers.util.Either;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyItems;
import violet.dainty.registries.DaintyPotions;

@EventBusSubscriber(modid = Dainty.MODID)
public class WardenPotionEventHandler {

	@SubscribeEvent
	public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
		PotionBrewing.Builder builder = event.getBuilder();
		builder.addMix(
			Potions.AWKWARD,
			DaintyItems.WARDEN_HEART.get(),
			DaintyPotions.SILENT_SIGHT_POTION
		);
	}

	@SubscribeEvent
	public static void silent(PlaySoundEvent event) {
		if (event.getSound().getSource() == SoundSource.PLAYERS) {
		}
	}

	@SubscribeEvent
	public static void colorWardenPotionItemName(RenderTooltipEvent.GatherComponents event) {
		List<MobEffect> effects = event
			.getItemStack()
			.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
			.potion()
			.get()
			.value()
			.getEffects()
			.stream()
			.map(effect -> effect.getEffect().value())
			.toList();
		if (effects.contains(DaintyPotions.SILENT_SIGHT_EFFECT.get())) {
			event.getTooltipElements().set(0, Either.left(FormattedText.of(event.getTooltipElements().get(0).left().get().getString(), Style.EMPTY.withColor(ChatFormatting.AQUA))));
		}
	}	
}
