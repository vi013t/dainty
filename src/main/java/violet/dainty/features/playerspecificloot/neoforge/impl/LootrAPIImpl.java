package violet.dainty.features.playerspecificloot.neoforge.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import violet.dainty.features.playerspecificloot.api.ILootrAPI;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.MenuBuilder;
import violet.dainty.features.playerspecificloot.api.client.ClientTextureType;
import violet.dainty.features.playerspecificloot.api.data.DefaultLootFiller;
import violet.dainty.features.playerspecificloot.api.data.ILootrInfoProvider;
import violet.dainty.features.playerspecificloot.api.data.ILootrSavedData;
import violet.dainty.features.playerspecificloot.api.data.LootFiller;
import violet.dainty.features.playerspecificloot.api.data.inventory.ILootrInventory;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.data.DataStorage;
import violet.dainty.features.playerspecificloot.neoforge.config.ConfigManager;
import violet.dainty.features.playerspecificloot.neoforge.event.HandleChunk;
import violet.dainty.features.playerspecificloot.neoforge.network.client.ClientHandlers;

public class LootrAPIImpl implements ILootrAPI {
	@Override
	public void handleProviderSneak(@Nullable ILootrInfoProvider provider, ServerPlayer player) {
		if (provider == null) {
			return;
		}
		if (provider.removeVisualOpener(player)) {
			provider.performClose(player);
			provider.performUpdate(player);
		}
	}

	@Override
	public void handleProviderOpen(@Nullable ILootrInfoProvider provider, ServerPlayer player) {
		if (provider == null) {
			return;
		}
		if (player.isSpectator()) {
		player.openMenu(null);
			return;
		}

		if (provider.getInfoUUID() == null) {
			player.displayClientMessage(Component.translatable("dainty.message.invalid_block").setStyle(LootrAPI.getInvalidStyle()), true);
			return;
		}
		if (LootrAPI.isDecayed(provider)) {
			provider.performDecay();
			player.displayClientMessage(Component.translatable("dainty.message.decayed").setStyle(LootrAPI.getDecayStyle()), true);
			LootrAPI.removeDecayed(provider);
			return;
		} else {
			int decayValue = LootrAPI.getRemainingDecayValue(provider);
			if (decayValue > 0 && LootrAPI.shouldNotify(decayValue)) {
				player.displayClientMessage(Component.translatable("dainty.message.decay_in", decayValue / 20).setStyle(LootrAPI.getDecayStyle()), true);
			} else if (decayValue == -1) {
				if (LootrAPI.isDecaying(provider)) {
					LootrAPI.setDecaying(provider);
					player.displayClientMessage(Component.translatable("dainty.message.decay_start", LootrAPI.getDecayValue() / 20).setStyle(LootrAPI.getDecayStyle()), true);
				}
			}
		}
		provider.performTrigger(player);
		boolean shouldUpdate = false;
		if (LootrAPI.isRefreshed(provider)) {
		provider.performRefresh();
		provider.performClose();
		LootrAPI.removeRefreshed(provider);
		player.displayClientMessage(Component.translatable("dainty.message.refreshed").setStyle(LootrAPI.getRefreshStyle()), true);
		shouldUpdate = true;
		}
		int refreshValue = LootrAPI.getRemainingRefreshValue(provider);
		if (refreshValue > 0 && LootrAPI.shouldNotify(refreshValue)) {
		player.displayClientMessage(Component.translatable("dainty.message.refresh_in", refreshValue / 20).setStyle(LootrAPI.getRefreshStyle()), true);
		} else if (refreshValue == -1) {
		if (LootrAPI.isRefreshing(provider)) {
			LootrAPI.setRefreshing(provider);
			player.displayClientMessage(Component.translatable("dainty.message.refresh_start", LootrAPI.getRefreshValue() / 20).setStyle(LootrAPI.getRefreshStyle()), true);
		}
		}
		MenuProvider menuProvider = LootrAPI.getInventory(provider, player, DefaultLootFiller.getInstance());
		if (menuProvider == null) {
		return;
		}
		if (!provider.hasOpened(player)) {
		player.awardStat(LootrRegistry.getLootedStat());
		LootrRegistry.getStatTrigger().trigger(player);
		}
		if (provider.addOpener(player)) {
		provider.performOpen(player);
		shouldUpdate = true;
		}

		if (shouldUpdate) {
		provider.performUpdate(player);
		}
		player.openMenu(menuProvider);
		PiglinAi.angerNearbyPiglins(player, true);
	}

	@Override
	public void handleProviderTick(@Nullable ILootrInfoProvider provider) {
		if (provider == null) {
		return;
		}

		if (provider.getInfoUUID() == null) {
		return;
		}

		if (provider.hasBeenOpened() && LootrAPI.shouldPerformDecayWhileTicking() && LootrAPI.isDecayed(provider)) {
		provider.performDecay();
		LootrAPI.removeDecayed(provider);
		return;
		} else if (provider.hasBeenOpened() && LootrAPI.shouldStartDecayWhileTicking() && !LootrAPI.isDecayed(provider)) {
		int decayValue = LootrAPI.getRemainingDecayValue(provider);
		if (decayValue == -1) {
			if (LootrAPI.isDecaying(provider)) {
			LootrAPI.setDecaying(provider);
			}
		}
		}
		if (provider.hasBeenOpened() && LootrAPI.shouldPerformRefreshWhileTicking() && LootrAPI.isRefreshed(provider)) {
		provider.performRefresh();
		provider.performClose();
		LootrAPI.removeRefreshed(provider);
		provider.performUpdate();
		}
		if (provider.hasBeenOpened() && LootrAPI.shouldStartRefreshWhileTicking() && !LootrAPI.isRefreshed(provider)) {
		int refreshValue = LootrAPI.getRemainingRefreshValue(provider);
		if (refreshValue == -1) {
			if (LootrAPI.isRefreshing(provider)) {
			LootrAPI.setRefreshing(provider);
			}
		}
		}
	}

	@Override
	public Set<UUID> getPlayerIds() {
		MinecraftServer server = getServer();
		if (server == null) {
			return Set.of();
		}

		Set<UUID> result = new HashSet<>();
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (isFakePlayer(player)) {
				continue;
			}
			UUID thisUuid = player.getUUID();
			if (thisUuid != null) {
				result.add(thisUuid);
			}
		}
		return result;
	}

	@Override
	public Player getPlayer() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
		return ClientHandlers.getPlayer();
		} else {
		return null;
		}
	}

	@Override
	public MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}

	@Override
	public boolean isFakePlayer(Player player) {
		if (player instanceof ServerPlayer sPlayer) {
		//noinspection ConstantValue
		if (sPlayer.connection == null) {
			return true;
		}
		}
		return player instanceof FakePlayer;
	}

	@Override
	public boolean clearPlayerLoot(UUID id) {
		return DataStorage.clearInventories(id);
	}

	@Override
	public ILootrInventory getInventory(ILootrInfoProvider provider, ServerPlayer player, LootFiller filler) {
		return DataStorage.getInventory(provider, player, filler);
	}

	@Override
	public ILootrInventory getInventory(ILootrInfoProvider provider, ServerPlayer player, LootFiller filler, MenuBuilder menuBuilder) {
		ILootrInventory inventory = DataStorage.getInventory(provider, player, filler);
		if (inventory != null) {
		inventory.setMenuBuilder(menuBuilder);
		}
		return inventory;
	}

	@Override
	public @Nullable ILootrSavedData getData(ILootrInfoProvider provider) {
		return DataStorage.getData(provider);
	}

	@Override
	public long getLootSeed(long seed) {
			return ThreadLocalRandom.current().nextLong();
	}

	@Override
	public boolean shouldDiscard() {
		return LootrAPI.shouldDiscardIdAndOpeners;
	}

	@Override
	public float getExplosionResistance(Block block, float defaultResistance) {
		if (ConfigManager.BLAST_RESISTANT) {
		return 16.0f;
		} else if (ConfigManager.BLAST_IMMUNE) {
		return Float.MAX_VALUE;
		} else {
		return defaultResistance;
		}
	}

	@Override
	public boolean isBlastResistant () {
		return ConfigManager.BLAST_RESISTANT;
	}

	@Override
	public boolean isBlastImmune () {
		return ConfigManager.BLAST_IMMUNE;
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos position, float defaultProgress) {
		if (ConfigManager.DISABLE_BREAK) {
		return 0f;
		}
		return defaultProgress;
	}

	@Override
	public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos, int defaultSignal) {
		if (ConfigManager.POWER_COMPARATORS) {
		return 1;
		}
		return defaultSignal;
	}

	@Override
	public boolean shouldPowerComparators () {
		return ConfigManager.POWER_COMPARATORS;
	}

	@Override
	public boolean shouldNotify(int remaining) {
		return ConfigManager.shouldNotify(remaining);
	}

	@Override
	public int getNotificationDelay () {
		return ConfigManager.NOTIFICATION_DELAY;
	}

	@Override
	public boolean isNotificationsEnabled () {
		return !ConfigManager.DISABLE_NOTIFICATIONS;
	}

	@Override
	public boolean isMessageStylesEnabled() {
		return !ConfigManager.DISABLE_MESSAGE_STYLES;
	}

	@Override
	public ClientTextureType getTextureType() {
		if (ConfigManager.isOldTextures()) {
		return ClientTextureType.OLD;
		} else if (ConfigManager.isVanillaTextures()) {
		return ClientTextureType.VANILLA;
		} else {
		return ClientTextureType.DEFAULT;
		}
	}

	@Override
	public boolean isDisabled() {
		return ConfigManager.DISABLE;
	}

	@Override
	public boolean isLootTableBlacklisted(ResourceKey<LootTable> table) {
		return ConfigManager.isLootTableBlacklisted(table);
	}

	@Override
	public boolean isDimensionBlocked(ResourceKey<Level> dimension) {
		return ConfigManager.isDimensionBlocked(dimension);
	}

	@Override
	public boolean isDimensionDecaying(ResourceKey<Level> dimension) {
		return ConfigManager.isDimensionDecaying(dimension);
	}

	@Override
	public boolean isDimensionRefreshing(ResourceKey<Level> dimension) {
		return ConfigManager.isDimensionRefreshing(dimension);
	}

	@Override
	public Set<ResourceKey<Level>> getDimensionBlacklist() {
		return ConfigManager.getDimensionBlacklist();
	}

	@Override
	public Set<ResourceKey<Level>> getDimensionWhitelist () {
		return ConfigManager.getDimensionWhitelist();
	}

	@Override
	public Set<ResourceKey<LootTable>> getLootTableBlacklist () {
		return ConfigManager.getLootBlacklist();
	}

	@Override
	public Set<String> getLootModidBlacklist () {
		return ConfigManager.getLootModids();
	}

	@Override
	public Set<String> getModidDimensionWhitelist() {
		return ConfigManager.getDimensionModidWhitelist();
	}

	@Override
	public Set<String> getModidDimensionBlacklist() {
		return ConfigManager.getDimensionModidBlacklist();
	}

	@Override
	public boolean isDecaying(ILootrInfoProvider provider) {
		return ConfigManager.isDecaying(provider);
	}

	@Override
	public boolean isRefreshing(ILootrInfoProvider provider) {
		return ConfigManager.isRefreshing(provider);
	}

	@Override
	public Set<String> getModidDecayWhitelist() {
		return ConfigManager.getDecayMods();
	}

	@Override
	public Set<ResourceKey<LootTable>> getDecayWhitelist() {
		return ConfigManager.getDecayingTables();
	}

	@Override
	public Set<ResourceKey<Level>> getDecayDimensions() {
		return ConfigManager.getDecayDimensions();
	}

	@Override
	public Set<String> getRefreshModids() {
		return ConfigManager.getRefreshMods();
	}

	@Override
	public Set<ResourceKey<LootTable>> getRefreshWhitelist() {
		return ConfigManager.getRefreshingTables();
	}

	@Override
	public Set<ResourceKey<Level>> getRefreshDimensions() {
		return ConfigManager.getRefreshDimensions();
	}

	@Override
	public boolean reportUnresolvedTables() {
		return ConfigManager.REPORT_UNRESOLVED_TABLES;
	}

	@Override
	public boolean isCustomTrapped() {
		return ConfigManager.TRAPPED_CUSTOM;
	}

	@Override
	public boolean isWorldBorderSafe(Level level, BlockPos pos) {
		if (!ConfigManager.CHECK_WORLD_BORDER) {
		return true;
		}
		return level.getWorldBorder().isWithinBounds(pos);
	}

	@Override
	public boolean isWorldBorderSafe(Level level, ChunkPos pos) {
		if (!ConfigManager.CHECK_WORLD_BORDER) {
		return true;
		}
		return level.getWorldBorder().isWithinBounds(pos);
	}

	@Override
	public boolean shouldCheckWorldBorder () {
		return ConfigManager.CHECK_WORLD_BORDER;
	}

	@Override
	public int getMaximumAge() {
		return ConfigManager.MAXIMUM_AGE;
	}

	@Override
	public boolean hasExpired(long time) {
		return time > ConfigManager.MAXIMUM_AGE;
	}

	@Override
	public boolean shouldConvertMineshafts() {
		return ConfigManager.CONVERT_MINESHAFTS;
	}

	@Override
	public boolean shouldConvertElytras() {
		return ConfigManager.CONVERT_ELYTRAS;
	}

	@Override
	public int getDecayValue() {
		return ConfigManager.DECAY_VALUE;
	}

	@Override
	public boolean shouldDecayAll () {
		return ConfigManager.DECAY_ALL;
	}

	@Override
	public int getRefreshValue() {
		return ConfigManager.REFRESH_VALUE;
	}

	@Override
	public boolean shouldRefreshAll () {
		return ConfigManager.REFRESH_ALL;
	}

	@Override
	public Style getInvalidStyle() {
		return ConfigManager.DISABLE_MESSAGE_STYLES ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)).withBold(true);
	}

	@Override
	public Style getDecayStyle() {
		return ConfigManager.DISABLE_MESSAGE_STYLES ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)).withBold(true);
	}

	@Override
	public Style getRefreshStyle() {
		return ConfigManager.DISABLE_MESSAGE_STYLES ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true);
	}

	@Override
	public Style getChatStyle() {
		return ConfigManager.DISABLE_MESSAGE_STYLES ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA));
	}

	@Override
	public boolean canDestroyOrBreak(Player player) {
		return (isFakePlayer(player) && ConfigManager.ENABLE_FAKE_PLAYER_BREAK) || ConfigManager.ENABLE_BREAK;
	}

	@Override
	public boolean isBreakDisabled() {
		return ConfigManager.DISABLE_BREAK;
	}

	@Override
	public boolean isBreakEnabled() {
		return ConfigManager.ENABLE_BREAK;
	}

	@Override
	public boolean isFakePlayerBreakEnabled () {
		return ConfigManager.ENABLE_FAKE_PLAYER_BREAK;
	}

	@Override
	public boolean shouldPerformDecayWhileTicking () {
		return ConfigManager.PERFORM_DECAY_WHILE_TICKING;
	}

	@Override
	public boolean shouldPerformRefreshWhileTicking () {
		return ConfigManager.PERFORM_REFRESH_WHILE_TICKING;
	}

	@Override
	public boolean shouldStartDecayWhileTicking () {
		return ConfigManager.START_DECAY_WHILE_TICKING;
	}

	@Override
	public boolean shouldStartRefreshWhileTicking () {
		return ConfigManager.START_REFRESH_WHILE_TICKING;
	}

	@Override
	public boolean isAwarded(UUID uuid, ServerPlayer player) {
		return DataStorage.isAwarded(uuid, player);
	}

	@Override
	public void award(UUID id, ServerPlayer player) {
		DataStorage.award(id, player);
	}

	@Override
	public int getRemainingDecayValue(ILootrInfoProvider provider) {
		return DataStorage.getDecayValue(provider);
	}

	@Override
	public boolean isDecayed(ILootrInfoProvider provider) {
		return DataStorage.isDecayed(provider);
	}

	@Override
	public void setDecaying(ILootrInfoProvider provider) {
		DataStorage.setDecaying(provider);
	}

	@Override
	public void removeDecayed(ILootrInfoProvider provider) {
		DataStorage.removeDecayed(provider);
	}

	@Override
	public int getRemainingRefreshValue(ILootrInfoProvider provider) {
		return DataStorage.getRefreshValue(provider);
	}

	@Override
	public boolean isRefreshed(ILootrInfoProvider provider) {
		return DataStorage.isRefreshed(provider);
	}

	@Override
	public void setRefreshing(ILootrInfoProvider provider) {
		DataStorage.setRefreshing(provider);
	}

	@Override
	public void removeRefreshed(ILootrInfoProvider provider) {
		DataStorage.removeRefreshed(provider);
	}

	@Override
	@Nullable
	public BlockState replacementBlockState(BlockState original) {
		return ConfigManager.replacement(original);
	}

	@Override
	public Component getInvalidTableComponent(ResourceKey<LootTable> lootTable) {
		return Component.translatable("dainty.message.invalid_table", lootTable.location().getNamespace(), lootTable.toString()).setStyle(ConfigManager.DISABLE_MESSAGE_STYLES ? Style.EMPTY : Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)).withBold(true));
	}

	@Override
	public boolean anyUnloadedChunks (ResourceKey<Level> dimension, Set<ChunkPos> chunks) {
		synchronized (HandleChunk.LOADED_CHUNKS) {
		Set<ChunkPos> syncedChunks = HandleChunk.LOADED_CHUNKS.get(dimension);
		if (syncedChunks == null || syncedChunks.isEmpty()) {
			return true;
		}

		for (ChunkPos myPos : chunks) {
			if (!syncedChunks.contains(myPos)) {
			return true;
			}
		}
		}

		return false;
	}
}
