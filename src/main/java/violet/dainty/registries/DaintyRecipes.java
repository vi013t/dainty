package violet.dainty.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.stonefurnace.StoneFurnaceRecipe;

public class DaintyRecipes {

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Dainty.MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Dainty.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StoneFurnaceRecipe>> STONE_FURNACE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("stone_furnace", () -> new SimpleCookingSerializer<>(StoneFurnaceRecipe::new, 100));
	public static final DeferredHolder<RecipeType<?>, RecipeType<StoneFurnaceRecipe>> STONE_FURNACE = RECIPE_TYPES.register(
		"stone_furnace",
		() -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "stone_furnace"))
	);

	public static void register(IEventBus eventBus) {
		RECIPE_SERIALIZERS.register(eventBus);
		RECIPE_TYPES.register(eventBus);
	}
}
