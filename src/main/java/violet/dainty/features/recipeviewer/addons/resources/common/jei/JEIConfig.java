package violet.dainty.features.recipeviewer.addons.resources.common.jei;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.AbstractVillagerEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.DungeonEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.MobEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.PlantEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.WorldGenEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.dungeon.DungeonCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.enchantment.EnchantmentCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.enchantment.EnchantmentMaker;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.enchantment.EnchantmentWrapper;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.mob.MobCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.plant.PlantCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.villager.VillagerCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.worldgen.WorldGenCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Reference;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.DungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.MobRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.PlantRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.VillagerRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.WorldGenRegistry;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.JeiPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCategoryRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;


@JeiPlugin
public class JEIConfig implements IModPlugin {
    public static final ResourceLocation MOB = ResourceLocation.fromNamespaceAndPath(Reference.ID, "mob");
    public static final RecipeType<MobEntry> MOB_TYPE = new RecipeType<>(MOB, MobEntry.class);
    public static final ResourceLocation DUNGEON = ResourceLocation.fromNamespaceAndPath(Reference.ID , "dungeon");
    public static final RecipeType<DungeonEntry> DUNGEON_TYPE = new RecipeType<>(DUNGEON, DungeonEntry.class);
    public static final ResourceLocation WORLD_GEN = ResourceLocation.fromNamespaceAndPath(Reference.ID , "worldgen");
    public static final RecipeType<WorldGenEntry> WORLD_GEN_TYPE = new RecipeType<>(WORLD_GEN, WorldGenEntry.class);
    public static final ResourceLocation PLANT = ResourceLocation.fromNamespaceAndPath(Reference.ID , "plant");
    public static final RecipeType<PlantEntry> PLANT_TYPE = new RecipeType<>(PLANT, PlantEntry.class);
    public static final ResourceLocation ENCHANTMENT = ResourceLocation.fromNamespaceAndPath(Reference.ID , "enchantment");
    public static final RecipeType<EnchantmentWrapper> ENCHANTMENT_TYPE = new RecipeType<>(ENCHANTMENT, EnchantmentWrapper.class);
    public static final ResourceLocation VILLAGER = ResourceLocation.fromNamespaceAndPath(Reference.ID , "villager");
    public static final RecipeType<AbstractVillagerEntry> VILLAGER_TYPE = new RecipeType<>(VILLAGER, AbstractVillagerEntry.class);
    public static final Map<ResourceLocation, RecipeType<?>> TYPES = new HashMap<>();
    static {
        TYPES.put(MOB, MOB_TYPE);
        TYPES.put(DUNGEON, DUNGEON_TYPE);
        TYPES.put(WORLD_GEN, WORLD_GEN_TYPE);
        TYPES.put(PLANT, PLANT_TYPE);
        TYPES.put(ENCHANTMENT, ENCHANTMENT_TYPE);
        TYPES.put(VILLAGER, VILLAGER_TYPE);
    }

    private static IJeiHelpers jeiHelpers;
    private static IJeiRuntime jeiRuntime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Reference.ID, "minecraft");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DUNGEON_TYPE, DungeonRegistry.getInstance().getDungeons());
        registration.addRecipes(ENCHANTMENT_TYPE, EnchantmentMaker.createRecipes(registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM_STACK)));
        registration.addRecipes(MOB_TYPE, MobRegistry.getInstance().getMobs());
        registration.addRecipes(PLANT_TYPE, PlantRegistry.getInstance().getAllPlants());
        registration.addRecipes(VILLAGER_TYPE, VillagerRegistry.getInstance().getVillagers());
        registration.addRecipes(WORLD_GEN_TYPE, WorldGenRegistry.getInstance().getWorldGen());
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JEIConfig.jeiRuntime = jeiRuntime;
        hideCategories(Settings.hiddenCategories);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        JEIConfig.jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(
            new DungeonCategory(),
            new EnchantmentCategory(),
            new MobCategory(),
            new PlantCategory(),
            new VillagerCategory(),
            new WorldGenCategory()
        );
        Services.PLATFORM.getProxy().initCompatibility(); 
    }

    public static void resetCategories() {
        if (jeiRuntime != null) {
            for (RecipeType<?> recipeType : TYPES.values()) {
                jeiRuntime.getRecipeManager().unhideRecipeCategory(recipeType);
            }
        }
    }

    public static void hideCategories(String[] categories) {
        if (jeiRuntime != null) {
            for (String category : categories) {
                jeiRuntime.getRecipeManager().hideRecipeCategory(TYPES.get(ResourceLocation.fromNamespaceAndPath(Reference.ID, category)));
            }
        }
    }

    public static IJeiHelpers getJeiHelpers() {
        return JEIConfig.jeiHelpers;
    }
}
