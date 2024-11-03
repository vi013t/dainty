package violet.dainty.features.blocktooltips.addon.universal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.TooltipPosition;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.api.ui.ScreenDirection;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.IClientExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ItemView;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.impl.ui.HorizontalLineElement;
import violet.dainty.features.blocktooltips.util.ClientProxy;
import violet.dainty.features.blocktooltips.util.CommonProxy;
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public abstract class ItemStorageProvider<T extends Accessor<?>> implements IComponentProvider<T>, IServerDataProvider<T> {

	public static final Cache<Object, ItemCollector<?>> targetCache = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(
			60,
			TimeUnit.SECONDS).build();
	public static final Cache<Object, ItemCollector<?>> containerCache = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(
			120,
			TimeUnit.SECONDS).build();
	private static final StreamCodec<RegistryFriendlyByteBuf, Map.Entry<ResourceLocation, List<ViewGroup<ItemStack>>>> STREAM_CODEC = ViewGroup.listCodec(
			ItemStack.OPTIONAL_STREAM_CODEC);

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends ItemStorageProvider<BlockAccessor> {
		private static final ForBlock INSTANCE = new ForBlock();
	}

	public static class ForEntity extends ItemStorageProvider<EntityAccessor> {
		private static final ForEntity INSTANCE = new ForEntity();
	}

	public static void append(ITooltip tooltip, Accessor<?> accessor, IPluginConfig config) {
		if (!accessor.getServerData().contains(JadeIds.UNIVERSAL_ITEM_STORAGE.toString())) {
			if (accessor.getServerData().getBoolean("Loot")) {
				tooltip.add(Component.translatable("dainty.loot_not_generated"));
			} else if (accessor.getServerData().getBoolean("Locked")) {
				tooltip.add(Component.translatable("dainty.locked"));
			}
			return;
		}

		List<ClientViewGroup<ItemView>> groups = ClientProxy.mapToClientGroups(
				accessor,
				JadeIds.UNIVERSAL_ITEM_STORAGE,
				STREAM_CODEC,
				WailaClientRegistration.instance().itemStorageProviders::get,
				tooltip);
		if (groups == null || groups.isEmpty()) {
			return;
		}

		MutableBoolean showName = new MutableBoolean(true);
		MutableInt amountWidth = new MutableInt();
		{
			int showNameAmount = config.getInt(JadeIds.UNIVERSAL_ITEM_STORAGE_SHOW_NAME_AMOUNT);
			int totalSize = 0;
			for (var group : groups) {
				for (var view : group.views) {
					if (view.amountText != null) {
						showName.setFalse();
					}
					if (!view.item.isEmpty()) {
						++totalSize;
						if (totalSize == showNameAmount) {
							showName.setFalse();
						}
					}
					if (showName.isTrue()) {
						String s = IDisplayHelper.get().humanReadableNumber(view.item.getCount(), "", false, null);
						amountWidth.setValue(Math.max(amountWidth.intValue(), Minecraft.getInstance().font.width(s)));
					}
				}
			}
		}

		IElementHelper helper = IElementHelper.get();
		boolean renderGroup = groups.size() > 1 || groups.getFirst().shouldRenderGroup();
		ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
			if (renderGroup) {
				theTooltip.add(new HorizontalLineElement());
				if (group.title != null) {
					theTooltip.append(helper.text(group.title).scale(0.5F));
					theTooltip.append(new HorizontalLineElement());
				}
			}
			if (group.views.isEmpty()) {
				CompoundTag data = group.extraData;
				if (data != null && data.contains("Collecting", Tag.TAG_ANY_NUMERIC)) {
					float progress = data.getFloat("Collecting");
					if (progress < 1) {
						MutableComponent component = Component.translatable("dainty.collectingItems");
						if (progress > 0) {
							component.append(" %s%%".formatted((int) (progress * 100)));
						}
						theTooltip.add(component);
					}
				}
			}
			int drawnCount = 0;
			int realSize = config.getInt(accessor.showDetails() ?
					JadeIds.UNIVERSAL_ITEM_STORAGE_DETAILED_AMOUNT :
					JadeIds.UNIVERSAL_ITEM_STORAGE_NORMAL_AMOUNT);
			realSize = Math.min(group.views.size(), realSize);
			List<IElement> elements = Lists.newArrayList();
			for (int i = 0; i < realSize; i++) {
				ItemView itemView = group.views.get(i);
				ItemStack stack = itemView.item;
				if (stack.isEmpty()) {
					continue;
				}
				if (i > 0 && (
						showName.isTrue() ||
								drawnCount >= config.getInt(JadeIds.UNIVERSAL_ITEM_STORAGE_ITEMS_PER_LINE))) {
					theTooltip.add(elements);
					theTooltip.setLineMargin(-1, ScreenDirection.DOWN, 0);
					elements.clear();
					drawnCount = 0;
				}

				if (showName.isTrue()) {
					if (itemView.description != null) {
						elements.add(helper.smallItem(stack));
						elements.addAll(itemView.description);
					} else {
						elements.add(helper.smallItem(stack).clearCachedMessage());
						String s = IDisplayHelper.get().humanReadableNumber(stack.getCount(), "", false, null);
						int width = Minecraft.getInstance().font.width(s);
						if (width < amountWidth.intValue()) {
							elements.add(helper.spacer(amountWidth.intValue() - width, 0));
						}
						elements.add(helper.text(Component.literal(s)
								.append("× ")
								.append(IDisplayHelper.get().stripColor(stack.getHoverName()))).message(null));
					}
				} else if (itemView.amountText != null) {
					elements.add(helper.item(stack, 1, itemView.amountText));
				} else {
					elements.add(helper.item(stack));
				}
				drawnCount += 1;
			}

			if (!elements.isEmpty()) {
				theTooltip.add(elements);
			}
		});
	}

	public static void putData(Accessor<?> accessor) {
		CompoundTag tag = accessor.getServerData();
		Object target = accessor.getTarget();
		Player player = accessor.getPlayer();
		Map.Entry<ResourceLocation, List<ViewGroup<ItemStack>>> entry = CommonProxy.getServerExtensionData(
				accessor,
				WailaCommonRegistration.instance().itemStorageProviders);
		if (entry != null) {
			List<ViewGroup<ItemStack>> groups = entry.getValue();
			for (ViewGroup<ItemStack> group : groups) {
				if (group.views.size() > ItemCollector.MAX_SIZE) {
					group.views = group.views.subList(0, ItemCollector.MAX_SIZE);
				}
			}
			tag.put(JadeIds.UNIVERSAL_ITEM_STORAGE.toString(), accessor.encodeAsNbt(STREAM_CODEC, entry));
			return;
		}
		if (target instanceof RandomizableContainer containerEntity && containerEntity.getLootTable() != null) {
			tag.putBoolean("Loot", true);
		} else if (!player.isCreative() && !player.isSpectator() && target instanceof BaseContainerBlockEntity te) {
			if (te.lockKey != LockCode.NO_LOCK) {
				tag.putBoolean("Locked", true);
			}
		}
	}

	@Override
	public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
		if (accessor.getTarget() instanceof AbstractFurnaceBlockEntity) {
			return;
		}
		append(tooltip, accessor, config);
	}

	@Override
	public void appendServerData(CompoundTag tag, T accessor) {
		if (accessor.getTarget() instanceof AbstractFurnaceBlockEntity) {
			return;
		}
		putData(accessor);
	}

	@Override
	public boolean shouldRequestData(T accessor) {
		if (accessor.getTarget() instanceof AbstractFurnaceBlockEntity) {
			return false;
		}
		int amount;
		if (accessor.showDetails()) {
			amount = IWailaConfig.get().getPlugin().getInt(JadeIds.UNIVERSAL_ITEM_STORAGE_DETAILED_AMOUNT);
		} else {
			amount = IWailaConfig.get().getPlugin().getInt(JadeIds.UNIVERSAL_ITEM_STORAGE_NORMAL_AMOUNT);
		}
		if (amount == 0) {
			return false;
		}
		return WailaCommonRegistration.instance().itemStorageProviders.hitsAny(accessor, IServerExtensionProvider::shouldRequestData);
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.UNIVERSAL_ITEM_STORAGE;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.BODY + 1000;
	}

	public enum Extension implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
		INSTANCE;

		@Override
		public ResourceLocation getUid() {
			return JadeIds.UNIVERSAL_ITEM_STORAGE_DEFAULT;
		}

		@Nullable
		@Override
		public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
			Object target = accessor.getTarget();
			if (target == null) {
				return CommonProxy.createItemCollector(accessor, containerCache).update(accessor);
			}
			if (target instanceof RandomizableContainer te && te.getLootTable() != null) {
				return null;
			}
			if (target instanceof ContainerEntity containerEntity && containerEntity.getLootTable() != null) {
				return null;
			}
			Player player = accessor.getPlayer();
			if (!player.isCreative() && !player.isSpectator() && target instanceof BaseContainerBlockEntity te) {
				if (te.lockKey != LockCode.NO_LOCK) {
					return null;
				}
			}
			if (target instanceof EnderChestBlockEntity) {
				PlayerEnderChestContainer inventory = player.getEnderChestInventory();
				return new ItemCollector<>(new ItemIterator.ContainerItemIterator($ -> inventory, 0)).update(
						accessor
				);
			}
			ItemCollector<?> itemCollector;
			try {
				itemCollector = targetCache.get(target, () -> CommonProxy.createItemCollector(accessor, containerCache));
			} catch (ExecutionException e) {
				WailaExceptionHandler.handleErr(e, this, null);
				return null;
			}
			return itemCollector.update(accessor);
		}

		@Override
		public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
			return ClientViewGroup.map(groups, ItemView::new, null);
		}

		@Override
		public boolean shouldRequestData(Accessor<?> accessor) {
			Object target = accessor.getTarget();
			if (target instanceof EnderChestBlockEntity || target instanceof Container) {
				return true;
			}
			return CommonProxy.hasDefaultItemStorage(accessor);
		}

		@Override
		public int getDefaultPriority() {
			return 9999;
		}
	}

}
