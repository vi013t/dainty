package violet.dainty.features.blocktooltips.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IJadeProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.IWailaCommonRegistration;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.impl.config.PluginConfig;
import violet.dainty.features.blocktooltips.impl.lookup.HierarchyLookup;
import violet.dainty.features.blocktooltips.impl.lookup.PairHierarchyLookup;
import violet.dainty.features.blocktooltips.impl.lookup.WrappedHierarchyLookup;

public class WailaCommonRegistration implements IWailaCommonRegistration {

	private static final WailaCommonRegistration INSTANCE = new WailaCommonRegistration();

	public final PairHierarchyLookup<IServerDataProvider<BlockAccessor>> blockDataProviders;
	public final HierarchyLookup<IServerDataProvider<EntityAccessor>> entityDataProviders;
	public final PriorityStore<ResourceLocation, IJadeProvider> priorities;

	public final WrappedHierarchyLookup<IServerExtensionProvider<ItemStack>> itemStorageProviders;
	public final WrappedHierarchyLookup<IServerExtensionProvider<CompoundTag>> fluidStorageProviders;
	public final WrappedHierarchyLookup<IServerExtensionProvider<CompoundTag>> energyStorageProviders;
	public final WrappedHierarchyLookup<IServerExtensionProvider<CompoundTag>> progressProviders;
	private CommonRegistrationSession session;

	WailaCommonRegistration() {
		blockDataProviders = new PairHierarchyLookup<>(new HierarchyLookup<>(Block.class), new HierarchyLookup<>(BlockEntity.class));
		blockDataProviders.idMapped();
		entityDataProviders = new HierarchyLookup<>(Entity.class);
		entityDataProviders.idMapped();
		priorities = new PriorityStore<>(IJadeProvider::getDefaultPriority, IJadeProvider::getUid);
		priorities.setSortingFunction((store, allKeys) -> {
			List<ResourceLocation> keys = allKeys.stream()
					.filter(PluginConfig::isPrimaryKey)
					.sorted(Comparator.comparingInt(store::byKey))
					.collect(Collectors.toCollection(ArrayList::new));
			allKeys.stream().filter(Predicate.not(PluginConfig::isPrimaryKey)).forEach($ -> {
				int index = keys.indexOf(PluginConfig.getPrimaryKey($));
				keys.add(index + 1, $);
			});
			return keys;
		});
		priorities.configurable(Dainty.MODID + "/sort-order", ResourceLocation.CODEC);

		itemStorageProviders = new WrappedHierarchyLookup<>();
		fluidStorageProviders = new WrappedHierarchyLookup<>();
		energyStorageProviders = new WrappedHierarchyLookup<>();
		progressProviders = new WrappedHierarchyLookup<>();
	}

	public static WailaCommonRegistration instance() {
		return INSTANCE;
	}

	@Override
	public void registerBlockDataProvider(IServerDataProvider<BlockAccessor> dataProvider, Class<?> blockOrBlobkEntityClass) {
		if (isSessionActive()) {
			session.registerBlockDataProvider(dataProvider, blockOrBlobkEntityClass);
		} else {
			blockDataProviders.register(blockOrBlobkEntityClass, dataProvider);
		}
	}

	@Override
	public void registerEntityDataProvider(IServerDataProvider<EntityAccessor> dataProvider, Class<? extends Entity> entityClass) {
		if (isSessionActive()) {
			session.registerEntityDataProvider(dataProvider, entityClass);
		} else {
			entityDataProviders.register(entityClass, dataProvider);
		}
	}

	/* PROVIDER GETTERS */

	public List<IServerDataProvider<BlockAccessor>> getBlockNBTProviders(Block block, @Nullable BlockEntity blockEntity) {
		if (blockEntity == null) {
			return blockDataProviders.first.get(block);
		}
		return blockDataProviders.getMerged(block, blockEntity);
	}

	public List<IServerDataProvider<EntityAccessor>> getEntityNBTProviders(Entity entity) {
		return entityDataProviders.get(entity);
	}

	public void loadComplete() {
		blockDataProviders.loadComplete(priorities);
		entityDataProviders.loadComplete(priorities);
		itemStorageProviders.loadComplete(priorities);
		fluidStorageProviders.loadComplete(priorities);
		energyStorageProviders.loadComplete(priorities);
		progressProviders.loadComplete(priorities);
		session = null;
	}

	@Override
	public <T> void registerItemStorage(IServerExtensionProvider<ItemStack> provider, Class<? extends T> clazz) {
		if (isSessionActive()) {
			session.registerItemStorage(provider, clazz);
		} else {
			itemStorageProviders.register(clazz, provider);
		}
	}

	@Override
	public <T> void registerFluidStorage(IServerExtensionProvider<CompoundTag> provider, Class<? extends T> clazz) {
		if (isSessionActive()) {
			session.registerFluidStorage(provider, clazz);
		} else {
			fluidStorageProviders.register(clazz, provider);
		}
	}

	@Override
	public <T> void registerEnergyStorage(IServerExtensionProvider<CompoundTag> provider, Class<? extends T> clazz) {
		if (isSessionActive()) {
			session.registerEnergyStorage(provider, clazz);
		} else {
			energyStorageProviders.register(clazz, provider);
		}
	}

	@Override
	public <T> void registerProgress(IServerExtensionProvider<CompoundTag> provider, Class<? extends T> clazz) {
		if (isSessionActive()) {
			session.registerProgress(provider, clazz);
		} else {
			progressProviders.register(clazz, provider);
		}
	}

	public void startSession() {
		if (session == null) {
			session = new CommonRegistrationSession(this);
		}
		session.reset();
	}

	public void endSession() {
		Preconditions.checkState(session != null, "Session not started");
		session.end();
	}

	public boolean isSessionActive() {
		return session != null && session.isActive();
	}
}
