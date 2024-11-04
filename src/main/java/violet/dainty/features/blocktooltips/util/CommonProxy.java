package violet.dainty.features.blocktooltips.util;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.mojang.authlib.GameProfile;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.UsernameCache;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforgespi.language.ModFileScanData;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.Jade;
import violet.dainty.features.blocktooltips.addon.harvest.HarvestToolProvider;
import violet.dainty.features.blocktooltips.addon.universal.ItemCollector;
import violet.dainty.features.blocktooltips.addon.universal.ItemIterator;
import violet.dainty.features.blocktooltips.addon.universal.ItemStorageProvider;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IWailaPlugin;
import violet.dainty.features.blocktooltips.api.WailaPlugin;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.view.EnergyView;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;
import violet.dainty.features.blocktooltips.command.JadeServerCommand;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.impl.config.PluginConfig;
import violet.dainty.features.blocktooltips.impl.lookup.WrappedHierarchyLookup;
import violet.dainty.features.blocktooltips.network.ReceiveDataPacket;
import violet.dainty.features.blocktooltips.network.RequestBlockPacket;
import violet.dainty.features.blocktooltips.network.RequestEntityPacket;
import violet.dainty.features.blocktooltips.network.ServerPingPacket;
import violet.dainty.features.blocktooltips.network.ShowOverlayPacket;
import violet.dainty.mixins.blocktooltips.CanItemPerformAbilityAccess;

@Mod(Dainty.MODID)
public final class CommonProxy {
	public CommonProxy(IEventBus modBus) {
		modBus.addListener(this::loadComplete);
		modBus.addListener(this::registerPayloadHandlers);
		NeoForge.EVENT_BUS.addListener(CommonProxy::playerJoin);
		NeoForge.EVENT_BUS.addListener(CommonProxy::registerServerCommand);
		if (isPhysicallyClient()) {
			ClientProxy.init(modBus);
		}
	}

	@SuppressWarnings("null")
	public static <T> T getDefaultStorage(
			Accessor<?> accessor,
			BlockCapability<T, ?> blockCapability,
			EntityCapability<T, ?> entityCapability) {
		if (accessor instanceof BlockAccessor blockAccessor) {
			return accessor.getLevel().getCapability(
					blockCapability,
					blockAccessor.getPosition(),
					blockAccessor.getBlockState(),
					blockAccessor.getBlockEntity(),
					null);
		} else if (accessor instanceof EntityAccessor entityAccessor) {
			return entityAccessor.getEntity().getCapability(entityCapability, null);
		}
		return null;
	}

	public static <T> boolean hasDefaultStorage(
			Accessor<?> accessor,
			BlockCapability<T, ?> blockCapability,
			EntityCapability<T, ?> entityCapability) {
		if (accessor instanceof BlockAccessor || accessor instanceof EntityAccessor) {
			return getDefaultStorage(accessor, blockCapability, entityCapability) != null;
		}
		return true;
	}

	public static boolean hasDefaultItemStorage(Accessor<?> accessor) {
		if (accessor.getTarget() == null && accessor instanceof BlockAccessor blockAccessor &&
				blockAccessor.getBlock() instanceof WorldlyContainerHolder) {
			return true;
		}
		return hasDefaultStorage(accessor, Capabilities.ItemHandler.BLOCK, Capabilities.ItemHandler.ENTITY);
	}

	public static boolean hasDefaultFluidStorage(Accessor<?> accessor) {
		return hasDefaultStorage(accessor, Capabilities.FluidHandler.BLOCK, Capabilities.FluidHandler.ENTITY);
	}

	public static boolean hasDefaultEnergyStorage(Accessor<?> accessor) {
		return hasDefaultStorage(accessor, Capabilities.EnergyStorage.BLOCK, Capabilities.EnergyStorage.ENTITY);
	}

	public static long bucketVolume() {
		return FluidType.BUCKET_VOLUME;
	}

	public static long blockVolume() {
		return FluidType.BUCKET_VOLUME;
	}

	public static boolean isCorrectConditions(List<LootItemCondition> conditions, ItemStack toolItem) {
		if (conditions.size() != 1) {
			return false;
		}
		LootItemCondition condition = conditions.getFirst();
		if (condition instanceof MatchTool matchTool) {
			ItemPredicate itemPredicate = matchTool.predicate().orElse(null);
			return itemPredicate != null && itemPredicate.test(toolItem);
		} else if (condition instanceof AnyOfCondition anyOfCondition) {
			for (LootItemCondition child : anyOfCondition.terms) {
				if (isCorrectConditions(List.of(child), toolItem)) {
					return true;
				}
			}
		} else if (condition instanceof CanItemPerformAbilityAccess canItemPerformAbility) {
			return canItemPerformAbility.getAbility() == ItemAbilities.SHEARS_DIG;
		}
		return false;
	}

	private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		event.registrar(Dainty.MODID)
				.versioned("5")
				.optional()
				.playToClient(
						ReceiveDataPacket.TYPE,
						ReceiveDataPacket.CODEC,
						(payload, context) -> ReceiveDataPacket.handle(payload, context::enqueueWork))
				.playToClient(
						ServerPingPacket.TYPE,
						ServerPingPacket.CODEC,
						(payload, context) -> ServerPingPacket.handle(payload, context::enqueueWork))
				.playToServer(
						RequestEntityPacket.TYPE,
						RequestEntityPacket.CODEC,
						(payload, context) -> RequestEntityPacket.handle(payload, () -> (ServerPlayer) context.player()))
				.playToServer(
						RequestBlockPacket.TYPE,
						RequestBlockPacket.CODEC,
						(payload, context) -> RequestBlockPacket.handle(payload, () -> (ServerPlayer) context.player()))
				.playToClient(
						ShowOverlayPacket.TYPE,
						ShowOverlayPacket.CODEC,
						(payload, context) -> ShowOverlayPacket.handle(payload, context::enqueueWork));
	}

	public static int showOrHideFromServer(Collection<ServerPlayer> players, boolean show) {
		ShowOverlayPacket msg = new ShowOverlayPacket(show);
		for (ServerPlayer player : players) {
			player.connection.send(msg);
		}
		return players.size();
	}

	private static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer player = (ServerPlayer) event.getEntity();
		String configs = PluginConfig.INSTANCE.getServerConfigs();
		List<Block> shearableBlocks = HarvestToolProvider.INSTANCE.getShearableBlocks();
		if (!configs.isEmpty()) {
			Dainty.LOGGER.debug(
					"Syncing config to {} ({})",
					event.getEntity().getGameProfile().getName(),
					event.getEntity().getGameProfile().getId());
		}
		List<ResourceLocation> blockProviderIds = WailaCommonRegistration.instance().blockDataProviders.mappedIds();
		List<ResourceLocation> entityProviderIds = WailaCommonRegistration.instance().entityDataProviders.mappedIds();
		player.connection.send(new ServerPingPacket(configs, shearableBlocks, blockProviderIds, entityProviderIds));
	}

	@Nullable
	public static String getLastKnownUsername(@Nullable UUID uuid) {
		if (uuid == null) {
			return null;
		}
		Optional<GameProfile> optional = SkullBlockEntity.fetchGameProfile(uuid).getNow(Optional.empty());
		if (optional.isPresent()) {
			return optional.get().getName();
		}
		return UsernameCache.getLastKnownUsername(uuid);
	}

	public static File getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get().toFile();
	}

	public static boolean isCorrectToolForDrops(BlockState state, Player player, Level level, BlockPos pos) {
		return EventHooks.doPlayerHarvestCheck(player, state, level, pos);
	}

	public static String getModIdFromItem(ItemStack stack) {
		if (isPhysicallyClient()) {
			CustomModelData modelData = stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT);
			if (!CustomModelData.DEFAULT.equals(modelData)) {
				String key = "dainty.customModelData.%s.namespace".formatted(modelData.value());
				if (I18n.exists(key)) {
					return I18n.get(key);
				}
			}
		}
		return stack.getItem().getCreatorModId(stack);
	}

	public static boolean isPhysicallyClient() {
		return FMLEnvironment.dist.isClient();
	}

	private static void registerServerCommand(RegisterCommandsEvent event) {
		JadeServerCommand.register(event.getDispatcher());
	}

	public static ItemCollector<?> createItemCollector(Accessor<?> accessor, Cache<Object, ItemCollector<?>> containerCache) {
		final Object target = accessor.getTarget();
		if (!(target instanceof Entity) || target instanceof AbstractChestedHorse) {
			try {
				IItemHandler itemHandler = findItemHandler(accessor);
				if (itemHandler != null) {
					return containerCache.get(
							itemHandler,
							() -> new ItemCollector<>(JadeForgeUtils.fromItemHandler(
									itemHandler,
									target instanceof AbstractChestedHorse ? 2 : 0)));
				}
			} catch (Throwable e) {
				WailaExceptionHandler.handleErr(e, null, null);
			}
		}
		final Container container = findContainer(accessor);
		if (container != null) {
			if (container instanceof ChestBlockEntity) {
				return new ItemCollector<>(new ItemIterator.ContainerItemIterator(a -> {
					if (a.getTarget() instanceof ChestBlockEntity be) {
						if (be.getBlockState().getBlock() instanceof ChestBlock chestBlock) {
							Container compound = ChestBlock.getContainer(
									chestBlock,
									be.getBlockState(),
									Objects.requireNonNull(be.getLevel()),
									be.getBlockPos(),
									false);
							if (compound != null) {
								return compound;
							}
						}
						return be;
					}
					return null;
				}, 0));
			}
			return new ItemCollector<>(new ItemIterator.ContainerItemIterator(0));
		}
		return ItemCollector.EMPTY;
	}

	@Nullable
	public static List<ViewGroup<ItemStack>> containerGroup(Container container, Accessor<?> accessor) {
		return containerGroup(container, accessor, CommonProxy::findContainer);
	}

	@Nullable
	public static List<ViewGroup<ItemStack>> containerGroup(
			Container container,
			Accessor<?> accessor,
			Function<Accessor<?>, Container> containerFinder) {
		try {
			return ItemStorageProvider.containerCache.get(
							container,
							() -> new ItemCollector<>(new ItemIterator.ContainerItemIterator(containerFinder, 0)))
					.update(accessor);
		} catch (Exception e) {
			return null;
		}
	}

	@Nullable
	public static List<ViewGroup<ItemStack>> storageGroup(Object storage, Accessor<?> accessor) {
		return storageGroup(storage, accessor, CommonProxy::findItemHandler);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static List<ViewGroup<ItemStack>> storageGroup(
			Object storage,
			Accessor<?> accessor,
			Function<Accessor<?>, Object> storageFinder) {
		try {
			//noinspection unchecked
			return ItemStorageProvider.containerCache.get(
					storage,
					() -> new ItemCollector<>(JadeForgeUtils.fromItemHandler(
							(IItemHandler) storage,
							0,
							(Function<Accessor<?>, @Nullable IItemHandler>) (Object) storageFinder))).update(
					accessor
			);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("null")
	@Nullable
	public static IItemHandler findItemHandler(Accessor<?> accessor) {
		if (accessor instanceof BlockAccessor blockAccessor) {
			return accessor.getLevel().getCapability(
					Capabilities.ItemHandler.BLOCK,
					blockAccessor.getPosition(),
					blockAccessor.getBlockState(),
					blockAccessor.getBlockEntity(),
					null);
		} else if (accessor instanceof EntityAccessor entityAccessor) {
			return entityAccessor.getEntity().getCapability(Capabilities.ItemHandler.ENTITY);
		}
		return null;
	}

	@Nullable
	public static Container findContainer(Accessor<?> accessor) {
		Object target = accessor.getTarget();
		if (target == null && accessor instanceof BlockAccessor blockAccessor &&
				blockAccessor.getBlock() instanceof WorldlyContainerHolder holder) {
			return holder.getContainer(blockAccessor.getBlockState(), accessor.getLevel(), blockAccessor.getPosition());
		} else if (target instanceof Container container) {
			return container;
		}
		return null;
	}

	@Nullable
	public static List<ViewGroup<CompoundTag>> wrapFluidStorage(Accessor<?> accessor) {
		IFluidHandler fluidHandler = getDefaultStorage(accessor, Capabilities.FluidHandler.BLOCK, Capabilities.FluidHandler.ENTITY);
		if (fluidHandler != null) {
			return JadeForgeUtils.fromFluidHandler(fluidHandler);
		}
		return null;
	}

	@Nullable
	public static List<ViewGroup<CompoundTag>> wrapEnergyStorage(Accessor<?> accessor) {
		IEnergyStorage energyStorage = getDefaultStorage(accessor, Capabilities.EnergyStorage.BLOCK, Capabilities.EnergyStorage.ENTITY);
		if (energyStorage != null) {
			var group = new ViewGroup<>(List.of(EnergyView.of(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored())));
			group.getExtraData().putString("Unit", "FE");
			return List.of(group);
		}
		return null;
	}

	public static boolean isDevEnv() {
		return !FMLEnvironment.production;
	}

	public static float getEnchantPowerBonus(BlockState state, Level world, BlockPos pos) {
		if (WailaClientRegistration.instance().customEnchantPowers.containsKey(state.getBlock())) {
			return WailaClientRegistration.instance().customEnchantPowers.get(state.getBlock()).getEnchantPowerBonus(state, world, pos);
		}
		return state.getEnchantPowerBonus(world, pos);
	}

	public static ResourceLocation getId(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public static ResourceLocation getId(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

	public static ResourceLocation getId(BlockEntityType<?> blockEntityType) {
		return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
	}

	public static String getPlatformIdentifier() {
		return "neoforge";
	}

	public static MutableComponent getProfessionName(VillagerProfession profession) {
		ResourceLocation profName = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession);
		return Component.translatable(EntityType.VILLAGER.getDescriptionId() + '.' +
				(!ResourceLocation.DEFAULT_NAMESPACE.equals(profName.getNamespace()) ? profName.getNamespace() + '.' : "") +
				profName.getPath());
	}

	public static boolean isBoss(Entity entity) {
		EntityType<?> entityType = entity.getType();
		return entityType.is(Tags.EntityTypes.BOSSES) || entityType == EntityType.ENDER_DRAGON || entityType == EntityType.WITHER;
	}

	public static boolean isModLoaded(String modid) {
		try {
			return ModList.get().isLoaded(modid);
		} catch (Throwable e) {
			return false;
		}
	}

	public static ItemStack getBlockPickedResult(BlockState state, Player player, BlockHitResult hitResult) {
		return state.getCloneItemStack(hitResult, player.level(), hitResult.getBlockPos(), player);
	}

	public static ItemStack getEntityPickedResult(Entity entity, Player player, EntityHitResult hitResult) {
		return MoreObjects.firstNonNull(entity.getPickedResult(hitResult), ItemStack.EMPTY);
	}

	public static Component getFluidName(JadeFluidObject fluid) {
		return toFluidStack(fluid).getHoverName();
	}

	public static FluidStack toFluidStack(JadeFluidObject fluid) {
		int id = BuiltInRegistries.FLUID.getId(fluid.getType());
		Optional<Holder.Reference<Fluid>> holder = BuiltInRegistries.FLUID.getHolder(id);
		return holder.isEmpty() ? FluidStack.EMPTY : new FluidStack(holder.get(), (int) fluid.getAmount(), fluid.getComponents());
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		/* off */
		List<String> classNames = ModList.get().getAllScanData()
				.stream()
				.flatMap($ -> $.getAnnotations().stream())
				.filter($ -> {
					if ($.annotationType().getClassName().equals(WailaPlugin.class.getName())) {
						String required = (String) $.annotationData().getOrDefault("value", "");
						return required.isEmpty() || ModList.get().isLoaded(required);
					}
					return false;
				})
				.map(ModFileScanData.AnnotationData::memberName)
				.toList();
		/* on */

		for (String className : classNames) {
			Dainty.LOGGER.info("Start loading plugin from %s".formatted(className));
			try {
				Class<?> clazz = Class.forName(className);
				if (IWailaPlugin.class.isAssignableFrom(clazz)) {
					IWailaPlugin plugin = (IWailaPlugin) clazz.getDeclaredConstructor().newInstance();
					Stopwatch stopwatch = null;
					if (CommonProxy.isDevEnv()) {
						stopwatch = Stopwatch.createStarted();
					}
					WailaCommonRegistration common = WailaCommonRegistration.instance();
					common.startSession();
					plugin.register(common);
					if (isPhysicallyClient()) {
						WailaClientRegistration client = WailaClientRegistration.instance();
						client.startSession();
						plugin.registerClient(client);
						if (stopwatch != null) {
							Dainty.LOGGER.info("Bootstrapped plugin from %s in %s".formatted(className, stopwatch));
						}
						client.endSession();
					}
					common.endSession();
					if (stopwatch != null) {
						Dainty.LOGGER.info("Loaded plugin from %s in %s".formatted(className, stopwatch.stop()));
					}
				}
			} catch (Throwable e) {
				Dainty.LOGGER.error("Error loading plugin at %s".formatted(className), e);
				Throwables.throwIfInstanceOf(e, IllegalStateException.class);
				if (className.startsWith("violet.dainty.features.blocktooltips.")) {
					ExceptionUtils.wrapAndThrow(e);
				}
			}
		}
		Jade.loadComplete();
	}

	public static boolean isMultipartEntity(Entity target) {
		return target.isMultipartEntity();
	}

	public static Entity wrapPartEntityParent(Entity target) {
		if (target instanceof PartEntity<?> part) {
			return part.getParent();
		}
		return target;
	}

	public static int getPartEntityIndex(Entity entity) {
		if (!(entity instanceof PartEntity<?> part)) {
			return -1;
		}
		Entity parent = wrapPartEntityParent(entity);
		PartEntity<?>[] parts = parent.getParts();
		if (parts == null) {
			return -1;
		}
		return List.of(parts).indexOf(part);
	}

	public static Entity getPartEntity(Entity parent, int index) {
		if (parent == null) {
			return null;
		}
		if (index < 0) {
			return parent;
		}
		PartEntity<?>[] parts = parent.getParts();
		if (parts == null || index >= parts.length) {
			return parent;
		}
		return parts[index];
	}

	public static void registerTagsUpdatedListener(BiConsumer<RegistryAccess, Boolean> listener) {
		NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> listener.accept(
				event.getRegistryAccess(),
				event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));
	}

	public static <T> Map.Entry<ResourceLocation, List<ViewGroup<T>>> getServerExtensionData(
			Accessor<?> accessor,
			WrappedHierarchyLookup<IServerExtensionProvider<T>> lookup) {
		for (var provider : lookup.wrappedGet(accessor)) {
			List<ViewGroup<T>> groups;
			try {
				groups = provider.getGroups(accessor);
			} catch (Exception e) {
				WailaExceptionHandler.handleErr(e, provider, null);
				continue;
			}
			if (groups != null) {
				return Map.entry(provider.getUid(), groups);
			}
		}
		return null;
	}
}
