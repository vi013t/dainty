package violet.dainty.features.carryon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

public class ListHandler {
    private static Set<String> FORBIDDEN_TILES = new HashSet<>();
    private static Set<String> FORBIDDEN_ENTITIES = new HashSet<>();
    private static Set<String> ALLOWED_ENTITIES = new HashSet<>();
    private static Set<String> ALLOWED_TILES = new HashSet<>();
    private static Set<String> FORBIDDEN_STACKING = new HashSet<>();
    private static Set<String> ALLOWED_STACKING = new HashSet<>();

    private static List<TagKey<Block>> FORBIDDEN_TILES_TAGS = new ArrayList<>();
    private static List<TagKey<EntityType<?>>> FORBIDDEN_ENTITIES_TAGS = new ArrayList<>();
    private static List<TagKey<EntityType<?>>> ALLOWED_ENTITIES_TAGS = new ArrayList<>();
    private static List<TagKey<Block>> ALLOWED_TILES_TAGS = new ArrayList<>();
    private static List<TagKey<EntityType<?>>> FORBIDDEN_STACKING_TAGS = new ArrayList<>();
    private static List<TagKey<EntityType<?>>> ALLOWED_STACKING_TAGS = new ArrayList<>();

    private static Set<Property<?>> PROPERTY_EXCEPTION_CLASSES = new HashSet<>();

	private static String[] forbiddenTiles = {
		"#forge:immovable", "#forge:relocation_not_supported", "#neoforge:immovable", "#neoforge:relocation_not_supported", "minecraft:end_portal", "minecraft:piston_head",
		"#c:relocation_not_supported",
		"minecraft:end_gateway", "minecraft:tall_grass", "minecraft:large_fern", "minecraft:peony",
		"minecraft:rose_bush", "minecraft:lilac", "minecraft:sunflower", "minecraft:*_bed",
		"minecraft:*_door", "minecraft:big_dripleaf_stem", "minecraft:waterlily", "minecraft:cake",
		"minecraft:nether_portal", "minecraft:tall_seagrass", "animania:block_trough",
		"animania:block_invisiblock", "colossalchests:*", "ic2:*", "bigreactors:*", "forestry:*",
		"tconstruct:*", "rustic:*", "botania:*", "astralsorcery:*", "quark:colored_bed_*",
		"immersiveengineering:*", "embers:block_furnace", "embers:ember_bore",
		"embers:ember_activator", "embers:mixer", "embers:heat_coil", "embers:large_tank",
		"embers:crystal_cell", "embers:alchemy_pedestal", "embers:boiler", "embers:combustor",
		"embers:catalzyer", "embers:field_chart", "embers:inferno_forge",
		"storagedrawers:framingtable", "skyresources:*", "lootbags:*", "exsartagine:*",
		"aquamunda:tank", "opencomputers:*", "malisisdoors:*", "industrialforegoing:*",
		"minecolonies:*", "thaumcraft:pillar*", "thaumcraft:infernal_furnace",
		"thaumcraft:placeholder*", "thaumcraft:infusion_matrix", "thaumcraft:golem_builder",
		"thaumcraft:thaumatorium*", "magneticraft:oil_heater", "magneticraft:solar_panel",
		"magneticraft:steam_engine", "magneticraft:shelving_unit", "magneticraft:grinder",
		"magneticraft:sieve", "magneticraft:solar_tower", "magneticraft:solar_mirror",
		"magneticraft:container", "magneticraft:pumpjack", "magneticraft:solar_panel",
		"magneticraft:refinery", "magneticraft:oil_heater", "magneticraft:hydraulic_press",
		"magneticraft:multiblock_gap", "refinedstorage:*", "mcmultipart:*", "enderstorage:*",
		"betterstorage:*", "practicallogistics2:*", "wearablebackpacks:*", "rftools:screen",
		"rftools:creative_screen", "create:*", "magic_doorknob:*", "iceandfire:*", "ftbquests:*",
		"waystones:*", "contact:*", "framedblocks:*", "securitycraft:*", "forgemultipartcbe:*", "integrateddynamics:cable",
		"mekanismgenerators:wind_generator", "cookingforblockheads:cabinet", "cookingforblockheads:corner", "cookingforblockheads:counter",
		"cookingforblockheads:oven", "cookingforblockheads:toaster", "cookingforblockheads:milk_jar", "cookingforblockheads:cow_jar",
		"cookingforblockheads:fruit_basket", "cookingforblockheads:cooking_table", "cookingforblockheads:fridge", "cookingforblockheads:sink",
		"chipped:*", "irons_spellbooks:*", "create*:*", "simple_pipes:*", "libmultipart:*", "quark:tiny_potato", "ait:*",
		"vampirism:*", "extrastorage:*", "relics:researching_table", "sophisticatedstorage:*chest",
		"powah:*", "advancementtrophies:trophy", "mekanismgenerators:heat_generator", "mna:filler_block", "create_enchantment_industry:*", "graveyard:*", "immersivepetroleum:*", "tardis:interior_door", "cuffed:*"
	};

	private static String[] forbiddenEntities = {
		"#c:capturing_not_supported", "#c:teleporting_not_supported",
		"minecraft:end_crystal", "minecraft:ender_dragon", "minecraft:ghast",
		"minecraft:shulker", "minecraft:leash_knot", "minecraft:armor_stand",
		"minecraft:item_frame", "minecraft:painting", "minecraft:shulker_bullet",
		"animania:hamster", "animania:ferret*", "animania:hedgehog*", "animania:cart",
		"animania:wagon", "mynko:*", "pixelmon:*", "mocreatures:*", "quark:totem", "vehicle:*",
		"securitycraft:*", "taterzens:npc", "easy_npc:*", "bodiesbodies:dead_body"
	};

	private static String[] placementStateExceptions = {
		"minecraft:chest[type]",
		"minecraft:stone_button[face]",
		"minecraft:vine[north,east,south,west,up]",
		"minecraft:creeper_head[rotation]",
		"minecraft:glow_lichen[north,east,south,west,up,down]",
		"minecraft:oak_sign[rotation]",
		"minecraft:oak_trapdoor[half]",
	};

    public static boolean isPermitted(Block block) {
        return !doCheck(block, FORBIDDEN_TILES, FORBIDDEN_TILES_TAGS);
    }

    public static boolean isPermitted(Entity entity) {
        return !doCheck(entity, FORBIDDEN_ENTITIES, FORBIDDEN_ENTITIES_TAGS);
    }

    public static boolean isStackingPermitted(Entity entity) {
        return !doCheck(entity, FORBIDDEN_STACKING, FORBIDDEN_STACKING_TAGS);
    }

    public static boolean isPropertyException(Property<?> prop) {
        return PROPERTY_EXCEPTION_CLASSES.contains(prop);
    }

    private static boolean doCheck(Block block, Set<String> regular, List<TagKey<Block>> tags) {
        String name = BuiltInRegistries.BLOCK.getKey(block).toString();
        if (regular.contains(name)) return true;
        for (TagKey<Block> tag : tags) if(block.defaultBlockState().is(tag)) return true;
        return false;
    }

    private static boolean doCheck(Entity entity, Set<String> regular, List<TagKey<EntityType<?>>> tags) {
        String name = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        if (regular.contains(name)) return true;
        for (TagKey<EntityType<?>> tag : tags) if (entity.getType().is(tag)) return true;
        return false;
    }

    public static void initConfigLists() {
        FORBIDDEN_ENTITIES.clear();
        FORBIDDEN_ENTITIES_TAGS.clear();
        FORBIDDEN_STACKING.clear();
        FORBIDDEN_STACKING_TAGS.clear();
        FORBIDDEN_TILES.clear();
        FORBIDDEN_TILES_TAGS.clear();
        ALLOWED_ENTITIES.clear();
        ALLOWED_ENTITIES_TAGS.clear();
        ALLOWED_STACKING.clear();
        ALLOWED_STACKING_TAGS.clear();
        ALLOWED_TILES.clear();
        ALLOWED_TILES_TAGS.clear();
        PROPERTY_EXCEPTION_CLASSES.clear();

        Map<ResourceLocation, TagKey<Block>> blockTags = BuiltInRegistries.BLOCK.getTagNames().collect(Collectors.toMap(t -> t.location(), t -> t));
        Map<ResourceLocation, TagKey<EntityType<?>>> entityTags = BuiltInRegistries.ENTITY_TYPE.getTagNames().collect(Collectors.toMap(t -> t.location(), t -> t));

        List<String> forbidden = new ArrayList<>(List.of(forbiddenTiles));
        forbidden.add("#carryon:block_blacklist");
        addWithWildcards(forbidden, FORBIDDEN_TILES, BuiltInRegistries.BLOCK, blockTags, FORBIDDEN_TILES_TAGS);

        List<String> forbiddenEntity = new ArrayList<>(List.of(forbiddenEntities));
        forbiddenEntity.add("#carryon:entity_blacklist");
        addWithWildcards(forbiddenEntity, FORBIDDEN_ENTITIES, BuiltInRegistries.ENTITY_TYPE, entityTags, FORBIDDEN_ENTITIES_TAGS);

        List<String> allowedEntities = new ArrayList<>();
        allowedEntities.add("#carryon:entity_whitelist");
        addWithWildcards(allowedEntities, ALLOWED_ENTITIES, BuiltInRegistries.ENTITY_TYPE, entityTags, ALLOWED_ENTITIES_TAGS);

        List<String> allowedBlocks = new ArrayList<>();
        allowedBlocks.add("#carryon:block_whitelist");
        addWithWildcards(allowedBlocks, ALLOWED_TILES, BuiltInRegistries.BLOCK, blockTags, ALLOWED_TILES_TAGS);

        List<String> forbiddenStacking = new ArrayList<>(List.of(new String[] { "minecraft:horse" }));
        forbiddenStacking.add("#carryon:stacking_blacklist");
        addWithWildcards(forbiddenStacking, FORBIDDEN_STACKING, BuiltInRegistries.ENTITY_TYPE, entityTags, FORBIDDEN_STACKING_TAGS);

        List<String> allowedStacking = new ArrayList<>();
        allowedStacking.add("#carryon:stacking_whitelist");
        addWithWildcards(allowedStacking, ALLOWED_STACKING, BuiltInRegistries.ENTITY_TYPE, entityTags, ALLOWED_STACKING_TAGS);

        for (String propString : placementStateExceptions) {
            if (!propString.contains("[") || !propString.contains("]")) continue;
            String name = propString.substring(0, propString.indexOf("["));
            String props = propString.substring(propString.indexOf("[") + 1, propString.indexOf("]"));
            Block blk = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name));
            for (String propName : props.split(",")) {
                for (Property<?> prop : blk.defaultBlockState().getProperties()) {
                    if (prop.getName().equals(propName)) PROPERTY_EXCEPTION_CLASSES.add(prop);
                }
            }
        }
    }

    private static <T> void addTag(String tag, Map<ResourceLocation, TagKey<T>> tagMap, List<TagKey<T>> tags) {
        String sub = tag.substring(1);
        TagKey<T> t = tagMap.get(ResourceLocation.parse(sub));
        if (t != null) tags.add(t);
    }

    private static <T> void addWithWildcards(List<String> entries, Set<String> toAddTo, Registry<T> registry, Map<ResourceLocation, TagKey<T>> tags, List<TagKey<T>> toAddTags) {
        ResourceLocation[] keys = registry.keySet().toArray(new ResourceLocation[0]);
        for (int i = 0; i < entries.size(); i++) {
            String current = entries.get(i);
            if (!current.startsWith("#")) {
                if (current.contains("*")) {
                    String[] filter = current.replace("*", ",").split(",");
                    for (ResourceLocation key : keys) {
                        if (containsAll(key.toString(), filter)) toAddTo.add(key.toString());
                    }
                }
                else toAddTo.add(current);
            }
            else addTag(current, tags, toAddTags);
        }
    }

    public static boolean containsAll(String str, String... strings) {
        return StringHelper.matchesWildcards(str, strings);
    }

    public static void addForbiddenTiles(String toAdd) {
        FORBIDDEN_TILES.add(toAdd);
    }

    public static void addForbiddenEntities(String toAdd) {
        FORBIDDEN_ENTITIES.add(toAdd);
    }

    public static void addForbiddenStacking(String toAdd) {
        FORBIDDEN_STACKING.add(toAdd);
    }

    public static void addAllowedTiles(String toAdd) {
        ALLOWED_TILES.add(toAdd);
    }

    public static void addAllowedEntities(String toAdd) {
        ALLOWED_ENTITIES.add(toAdd);
    }

    public static void addAllowedStacking(String toAdd) {
        ALLOWED_ENTITIES.add(toAdd);
    }
}