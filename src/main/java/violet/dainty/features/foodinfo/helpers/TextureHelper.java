package violet.dainty.features.foodinfo.helpers;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;

public class TextureHelper
{
	public static final ResourceLocation MOD_ICONS = ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "textures/icons.png");
	public static final ResourceLocation HUNGER_OUTLINE_SPRITE = ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "tooltip_hunger_outline");

	// Hunger
	public static final ResourceLocation FOOD_EMPTY_HUNGER_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_empty_hunger");
	public static final ResourceLocation FOOD_HALF_HUNGER_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_half_hunger");
	public static final ResourceLocation FOOD_FULL_HUNGER_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_full_hunger");
	public static final ResourceLocation FOOD_EMPTY_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_empty");
	public static final ResourceLocation FOOD_HALF_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_half");
	public static final ResourceLocation FOOD_FULL_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_full");

	public enum FoodType
	{
		EMPTY,
		HALF,
		FULL,
	}

	public static ResourceLocation getFoodTexture(boolean isRotten, FoodType type)
	{
		return switch (type)
		{
			case EMPTY -> isRotten ? FOOD_EMPTY_HUNGER_TEXTURE : FOOD_EMPTY_TEXTURE;
			case HALF -> isRotten ? FOOD_HALF_HUNGER_TEXTURE : FOOD_HALF_TEXTURE;
			case FULL -> isRotten ? FOOD_FULL_HUNGER_TEXTURE : FOOD_FULL_TEXTURE;
		};
	}

	// Hearts
	public static final ResourceLocation HEART_CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container");
	public static final ResourceLocation HEART_HARDCORE_CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container_hardcore");
	public static final ResourceLocation HEART_FULL = ResourceLocation.withDefaultNamespace("hud/heart/full");
	public static final ResourceLocation HEART_HARDCORE_FULL = ResourceLocation.withDefaultNamespace("hud/heart/hardcore_full");
	public static final ResourceLocation HEART_HALF = ResourceLocation.withDefaultNamespace("hud/heart/half");
	public static final ResourceLocation HEART_HARDCORE_HALF = ResourceLocation.withDefaultNamespace("hud/heart/hardcore_half");

	public enum HeartType
	{
		CONTAINER,
		FULL,
		HALF,
	}

	public static ResourceLocation getHeartTexture(boolean hardcore, HeartType type)
	{
		return switch (type)
		{
			case CONTAINER -> hardcore ? HEART_HARDCORE_CONTAINER : HEART_CONTAINER;
			case FULL -> hardcore ? HEART_HARDCORE_FULL : HEART_FULL;
			case HALF -> hardcore ? HEART_HARDCORE_HALF : HEART_HALF;
		};
	}
}
