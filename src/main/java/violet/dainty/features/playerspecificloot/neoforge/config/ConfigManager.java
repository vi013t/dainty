package violet.dainty.features.playerspecificloot.neoforge.config; 

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.LootrTags;
import violet.dainty.features.playerspecificloot.api.data.ILootrInfoProvider;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;

public class ConfigManager {

	public static final boolean REPORT_UNRESOLVED_TABLES = false;
	public static final boolean RANDOMISE_SEED = true;
	public static final boolean DISABLE = false;
	public static final int MAXIMUM_AGE = 60 * 20 * 15;
	public static final boolean CONVERT_MINESHAFTS = true;
	public static final boolean CONVERT_ELYTRAS = true;
	public static final boolean DISABLE_BREAK = false;
	public static final boolean ENABLE_BREAK = false;
	public static final boolean ENABLE_FAKE_PLAYER_BREAK = false;
	public static final boolean CHECK_WORLD_BORDER = false;
	public static final List<? extends String> DIMENSION_WHITELIST = Collections.emptyList();
	public static final List<? extends String> DIMENSION_BLACKLIST = Collections.emptyList();
	public static final List<? extends String> LOOT_TABLE_BLACKLIST = Collections.emptyList();
	public static final List<? extends String> LOOT_MODID_BLACKLIST = Collections.emptyList();
	public static final List<? extends String> MODID_DIMENSION_WHITELIST = Collections.emptyList();
	public static final List<? extends String> MODID_DIMENSION_BLACKLIST = Collections.emptyList();
	public static final int DECAY_VALUE = 5 * 60 * 20;
	public static final boolean DECAY_ALL = false;
	public static final boolean PERFORM_DECAY_WHILE_TICKING = true;
	public static final boolean START_DECAY_WHILE_TICKING = false;
	public static final List<? extends String> DECAY_MODIDS = Collections.emptyList();
	public static final List<? extends String> DECAY_LOOT_TABLES = Collections.emptyList();
	public static final List<? extends String> DECAY_DIMENSIONS = Collections.emptyList();
	public static final int REFRESH_VALUE = 20 * 60 * 20;
	public static final boolean REFRESH_ALL = false;
	public static final boolean PERFORM_REFRESH_WHILE_TICKING = true;
	public static final boolean START_REFRESH_WHILE_TICKING = true;
	public static final List<? extends String> REFRESH_MODIDS = Collections.emptyList();
	public static final List<? extends String> REFRESH_LOOT_TABLES = Collections.emptyList();
	public static final List<? extends String> REFRESH_DIMENSIONS = Collections.emptyList();
	public static final boolean POWER_COMPARATORS = true;
	public static final boolean BLAST_RESISTANT = false;
	public static final boolean BLAST_IMMUNE = false;
	public static final int NOTIFICATION_DELAY = 30 * 20 * -1;
	public static final boolean DISABLE_NOTIFICATIONS = false;
	public static final boolean DISABLE_MESSAGE_STYLES = false;
	public static final boolean TRAPPED_CUSTOM = false;
	public static final boolean VANILLA_TEXTURES = false;
	public static final boolean OLD_TEXTURES = false;

	private static final List<ResourceLocation> PROBLEMATIC_CHESTS = Arrays.asList(LootrAPI.rl("twilightforest", "structures/stronghold_boss"), LootrAPI.rl("atum", "chests/pharaoh"));

	// Caches
	private static Set<String> DECAY_MODS = null;
	private static Set<ResourceKey<LootTable>> DECAY_TABLES = null;
	private static Set<String> REFRESH_MODS = null;
	private static Set<ResourceKey<LootTable>> REFRESH_TABLES = null;
	private static Set<ResourceKey<Level>> DIM_WHITELIST = null;
	private static Set<String> MODID_DIM_WHITELIST = null;
	private static Set<ResourceKey<Level>> DIM_BLACKLIST = null;
	private static Set<String> MODID_DIM_BLACKLIST = null;
	private static Set<ResourceKey<Level>> DECAY_DIMS = null;
	private static Set<ResourceKey<Level>> REFRESH_DIMS = null;
	private static Set<ResourceKey<LootTable>> LOOT_BLACKLIST = null;
	private static Set<String> LOOT_MODIDS = null;
	private static Map<Block, Block> replacements = null;

	public static void loadConfig(ModConfigSpec spec, Path path) {
		CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		configData.load();
	}

	public static void configEvent(ModConfigEvent event) {
		if (event.getConfig().getType() == ModConfig.Type.COMMON) {
		replacements = null;
		MODID_DIM_WHITELIST = null;
		MODID_DIM_BLACKLIST = null;
		DIM_WHITELIST = null;
		DIM_BLACKLIST = null;
		LOOT_BLACKLIST = null;
		DECAY_MODS = null;
		DECAY_TABLES = null;
		DECAY_DIMS = null;
		LOOT_MODIDS = null;
		REFRESH_DIMS = null;
		REFRESH_MODS = null;
		REFRESH_TABLES = null;
		}
	}

	public static Set<ResourceKey<Level>> getDimensionWhitelist() {
		if (DIM_WHITELIST == null) {
		DIM_WHITELIST = DIMENSION_WHITELIST.stream().map(o -> ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return DIM_WHITELIST;
	}

	public static Set<String> getDimensionModidWhitelist() {
		if (MODID_DIM_WHITELIST == null) {
		MODID_DIM_WHITELIST = MODID_DIMENSION_WHITELIST.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
		}
		return MODID_DIM_WHITELIST;
	}

	public static Set<ResourceKey<Level>> getDimensionBlacklist() {
		if (DIM_BLACKLIST == null) {
		DIM_BLACKLIST = DIMENSION_BLACKLIST.stream().map(o -> ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return DIM_BLACKLIST;
	}

	public static Set<String> getDimensionModidBlacklist() {
		if (MODID_DIM_BLACKLIST == null) {
		MODID_DIM_BLACKLIST = MODID_DIMENSION_BLACKLIST.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
		}
		return MODID_DIM_BLACKLIST;
	}

	public static Set<ResourceKey<Level>> getDecayDimensions() {
		if (DECAY_DIMS == null) {
		DECAY_DIMS = DECAY_DIMENSIONS.stream().map(o -> ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return DECAY_DIMS;
	}

	public static Set<ResourceKey<Level>> getRefreshDimensions() {
		if (REFRESH_DIMS == null) {
		REFRESH_DIMS = REFRESH_DIMENSIONS.stream().map(o -> ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return REFRESH_DIMS;
	}

	public static Set<ResourceKey<LootTable>> getLootBlacklist() {
		if (LOOT_BLACKLIST == null) {
		LOOT_BLACKLIST = LOOT_TABLE_BLACKLIST.stream().map(o -> ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(o))).collect(Collectors.toSet());
		// Fixes for #79 and #74
		PROBLEMATIC_CHESTS.forEach(o -> LOOT_BLACKLIST.add(ResourceKey.create(Registries.LOOT_TABLE, o)));
		}
		return LOOT_BLACKLIST;
	}

	public static Set<String> getLootModids() {
		if (LOOT_MODIDS == null) {
		LOOT_MODIDS = LOOT_MODID_BLACKLIST.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
		}
		return LOOT_MODIDS;
	}

	public static boolean isLootTableBlacklisted(ResourceKey<LootTable> table) {
		if (!getLootBlacklist().isEmpty() && getLootBlacklist().contains(table)) {
		return true;
		}

		return !getLootModids().isEmpty() && getLootModids().contains(table.location().getNamespace());
	}

	public static Set<ResourceKey<LootTable>> getDecayingTables() {
		if (DECAY_TABLES == null) {
		DECAY_TABLES = DECAY_LOOT_TABLES.stream().map(o -> ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return DECAY_TABLES;
	}

	public static Set<String> getDecayMods() {
		if (DECAY_MODS == null) {
		DECAY_MODS = DECAY_MODIDS.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
		}
		return DECAY_MODS;
	}

	public static Set<ResourceKey<LootTable>> getRefreshingTables() {
		if (REFRESH_TABLES == null) {
		REFRESH_TABLES = REFRESH_LOOT_TABLES.stream().map(o -> ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(o))).collect(Collectors.toSet());
		}
		return REFRESH_TABLES;
	}

	public static Set<String> getRefreshMods() {
		if (REFRESH_MODS == null) {
		REFRESH_MODS = REFRESH_MODIDS.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
		}
		return REFRESH_MODS;
	}

	public static boolean isDimensionBlocked(ResourceKey<Level> key) {
		if (!getDimensionModidWhitelist().isEmpty() && !getDimensionModidWhitelist().contains(key.location().getNamespace()) || getDimensionModidBlacklist().contains(key.location().getNamespace())) {
		return true;
		}

		return (!getDimensionWhitelist().isEmpty() && !getDimensionWhitelist().contains(key)) || getDimensionBlacklist().contains(key);
	}

	public static boolean isDimensionDecaying(ResourceKey<Level> key) {
		return !getDecayDimensions().isEmpty() && getDecayDimensions().contains(key);
	}

	public static boolean isDimensionRefreshing(ResourceKey<Level> key) {
		return !getRefreshDimensions().isEmpty() && getRefreshDimensions().contains(key);
	}

	public static boolean isDecaying(ILootrInfoProvider provider) {
		if (DECAY_ALL) {
		return true;
		}
		if (provider.getInfoLootTable() != null) {
		if (!getDecayingTables().isEmpty() && getDecayingTables().contains(provider.getInfoLootTable())) {
			return true;
		}
		if (!getDecayMods().isEmpty() && getDecayMods().contains(provider.getInfoLootTable().location().getNamespace())) {
			return true;
		}
		}
		return isDimensionDecaying(provider.getInfoDimension());
	}

	public static boolean isRefreshing(ILootrInfoProvider provider) {
		if (REFRESH_ALL) {
		return true;
		}
		if (provider.getInfoLootTable() != null) {
		if (!getRefreshingTables().isEmpty() && getRefreshingTables().contains(provider.getInfoLootTable())) {
			return true;
		}
		if (!getRefreshMods().isEmpty() && getRefreshMods().contains(provider.getInfoLootTable().location().getNamespace())) {
			return true;
		}
		}
		return isDimensionRefreshing(provider.getInfoDimension());
	}


	public static boolean shouldNotify(int remaining) {
		int delay = NOTIFICATION_DELAY;
		return !DISABLE_NOTIFICATIONS && (delay == -1 || remaining <= delay);
	}

	public static boolean isVanillaTextures() {
		return VANILLA_TEXTURES;
	}

	public static boolean isOldTextures() {
		return OLD_TEXTURES;
	}

	public static BlockState replacement(BlockState original) {
		if (original.is(LootrTags.Blocks.CONVERT_BLACKLIST)) {
			return null;
		}

		if (original.is(LootrTags.Blocks.CONTAINERS)) {
			return null;
		}

		if (replacements == null) {
			replacements = new HashMap<>();
		}

		if (replacements.get(original.getBlock()) == null && original.is(LootrTags.Blocks.CONVERT_BLOCK)) {
			if (original.getBlock() instanceof EntityBlock entityBlock) {
				BlockEntity be = entityBlock.newBlockEntity(BlockPos.ZERO, original);
				if (be instanceof RandomizableContainerBlockEntity) {
					if (original.is(LootrTags.Blocks.CONVERT_TRAPPED_CHESTS)) {
						replacements.put(original.getBlock(), LootrRegistry.getTrappedChestBlock());
					} else if (original.is(LootrTags.Blocks.CONVERT_BARRELS)) {
						replacements.put(original.getBlock(), LootrRegistry.getBarrelBlock());
					} else if (original.is(LootrTags.Blocks.CONVERT_CHESTS)) {
						replacements.put(original.getBlock(), LootrRegistry.getChestBlock());
					} else if (original.is(LootrTags.Blocks.CONVERT_SHULKERS)) {
						replacements.put(original.getBlock(), LootrRegistry.getShulkerBlock());
					}
				}
			}
		}

		Block replacement = replacements.get(original.getBlock());

		if (replacement != null) {
			BlockState state = replacement.defaultBlockState();
			for (Property<?> prop : original.getProperties()) {
				if (state.hasProperty(prop)) {
					state = safeReplace(state, original, prop);
				}
			}
			return state;
		}

		return null;
	}

	private static <V extends Comparable<V>> BlockState safeReplace(BlockState state, BlockState original, Property<V> property) {
		if (property == ChestBlock.TYPE && state.hasProperty(property)) {
			return state.setValue(ChestBlock.TYPE, ChestType.SINGLE);
		}
		if (original.hasProperty(property) && state.hasProperty(property)) {
			return state.setValue(property, original.getValue(property));
		}
		return state;
	}
}
