package violet.dainty.features.foodinfo.helpers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import violet.dainty.features.foodinfo.api.event.FoodValuesEvent;

public class FoodHelper
{
	public static boolean isFood(ItemStack itemStack, Player player)
	{
		return itemStack.getFoodProperties(player) != null;
	}

	public static boolean canConsume(Player player, FoodProperties foodProperties)
	{
		return player.canEat(foodProperties.canAlwaysEat());
	}

	public static FoodProperties EMPTY_FOOD_PROPERTIES = new FoodProperties.Builder().build();

	/**
	 * Assumes itemStack is known to be a food, always returns a non-null FoodComponent
	 */
	public static FoodProperties getDefaultFoodValues(ItemStack itemStack, Player player)
	{
		var properties = itemStack.getFoodProperties(player);
		return properties != null ? properties : EMPTY_FOOD_PROPERTIES;
	}

	public static class QueriedFoodResult
	{
		public FoodProperties defaultFoodProperties;
		public FoodProperties modifiedFoodProperties;

		public final ItemStack itemStack;

		public QueriedFoodResult(FoodProperties defaultFoodProperties, FoodProperties modifiedFoodProperties, ItemStack itemStack)
		{
			this.defaultFoodProperties = defaultFoodProperties;
			this.modifiedFoodProperties = modifiedFoodProperties;
			this.itemStack = itemStack;
		}
	}

	@Nullable
	public static QueriedFoodResult query(ItemStack itemStack, Player player)
	{
		if (!isFood(itemStack, player))
			return null;

		FoodProperties defaultFood = getDefaultFoodValues(itemStack, player);

		FoodValuesEvent foodValuesEvent = new FoodValuesEvent(player, itemStack, defaultFood, defaultFood);
		NeoForge.EVENT_BUS.post(foodValuesEvent);

		return new QueriedFoodResult(foodValuesEvent.defaultFoodProperties, foodValuesEvent.modifiedFoodProperties, itemStack);
	}


	public static boolean isRotten(FoodProperties foodProperties)
	{
		for (FoodProperties.PossibleEffect effect : foodProperties.effects())
		{
			MobEffectInstance effectInstance = effect.effect();
			if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
			{
				return true;
			}
		}
		return false;
	}

	public static float getEstimatedHealthIncrement(Player player, FoodProperties foodProperties)
	{
		if (!player.isHurt())
			return 0;

		FoodData stats = player.getFoodData();
		Level world = player.getCommandSenderWorld();

		int foodLevel = Math.min(stats.getFoodLevel() + foodProperties.nutrition(), 20);
		float healthIncrement = 0;

		// health for natural regen
		if (foodLevel >= 18.0F && world != null && world.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION))
		{
			float saturationLevel = Math.min(stats.getSaturationLevel() + foodProperties.saturation(), (float) foodLevel);
			float exhaustionLevel = stats.getExhaustionLevel();
			healthIncrement = getEstimatedHealthIncrement(foodLevel, saturationLevel, exhaustionLevel);
		}

		// health for regeneration effect
		for (FoodProperties.PossibleEffect effect : foodProperties.effects())
		{
			MobEffectInstance effectInstance = effect.effect();
			if (effectInstance.is(MobEffects.REGENERATION))
			{
				int amplifier = effectInstance.getAmplifier();
				int duration = effectInstance.getDuration();

				// Refer: https://minecraft.fandom.com/wiki/Regeneration
				// Refer: net.minecraft.world.effect.MobEffect.isDurationEffectTick
				healthIncrement += (float) Math.floor(duration / Math.max(50 >> amplifier, 1));
				break;
			}
		}

		return healthIncrement;
	}

	public static float REGEN_EXHAUSTION_INCREMENT = 6.0F;
	public static float MAX_EXHAUSTION = 4.0F;

	public static float getEstimatedHealthIncrement(int foodLevel, float saturationLevel, float exhaustionLevel)
	{
		float health = 0;

		if (!Float.isFinite(exhaustionLevel) || !Float.isFinite(saturationLevel))
			return 0;

		while (foodLevel >= 18)
		{
			while (exhaustionLevel > MAX_EXHAUSTION)
			{
				exhaustionLevel -= MAX_EXHAUSTION;
				if (saturationLevel > 0)
					saturationLevel = Math.max(saturationLevel - 1, 0);
				else
					foodLevel -= 1;
			}
			// Without this Float.compare, it's possible for this function to get stuck in an infinite loop
			// if saturationLevel is small enough that exhaustionLevel does not actually change representation
			// when it's incremented. This Float.compare makes it so we treat such close-to-zero values as zero.
			if (foodLevel >= 20 && Float.compare(saturationLevel, Float.MIN_NORMAL) > 0)
			{
				// fast regen health
				//
				// Because only health and exhaustionLevel increase in this branch,
				// we know that we will enter this branch again and again on each iteration
				// if exhaustionLevel is not incremented above MAX_EXHAUSTION before the
				// next iteration.
				//
				// So, instead of actually performing those iterations, we can calculate
				// the number of iterations it would take to reach max exhaustion, and
				// add all the health/exhaustion in one go. In practice, this takes the
				// worst-case number of iterations performed in this function from the millions
				// all the way down to around 18.
				//
				// Note: Due to how floating point works, the results of actually doing the
				// iterations and 'simulating' them using multiplication will differ. That is, small increments
				// in a loop can end up with a different (and higher) final result than multiplication
				// due to floating point rounding. In degenerate cases, the difference can be fairly high
				// (when testing, I found a case that had a difference of ~0.3), but this isn't a concern in
				// this particular instance because the 'real' difference as seen by the player
				// would likely take hundreds of thousands of ticks to materialize (since the
				// `limitedSaturationLevel / REGEN_EXHAUSTION_INCREMENT` value must be very
				// small for a difference to occur at all, and therefore numIterationsUntilAboveMax would
				// be very large).
				float limitedSaturationLevel = Math.min(saturationLevel, REGEN_EXHAUSTION_INCREMENT);
				float exhaustionUntilAboveMax = Math.nextUp(MAX_EXHAUSTION) - exhaustionLevel;
				int numIterationsUntilAboveMax = Math.max(1, (int) Math.ceil(exhaustionUntilAboveMax / limitedSaturationLevel));

				health += (limitedSaturationLevel / REGEN_EXHAUSTION_INCREMENT) * numIterationsUntilAboveMax;
				exhaustionLevel += limitedSaturationLevel * numIterationsUntilAboveMax;
			}
			else if (foodLevel >= 18)
			{
				// slow regen health
				health += 1;
				exhaustionLevel += REGEN_EXHAUSTION_INCREMENT;
			}
		}

		return health;
	}
}
