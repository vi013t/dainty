package violet.dainty.features.blocktooltips.addon.universal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import violet.dainty.features.blocktooltips.api.ui.BoxStyle;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.api.ui.ProgressStyle;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.EnergyView;
import violet.dainty.features.blocktooltips.api.view.IClientExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.util.CommonProxy;
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public abstract class EnergyStorageProvider<T extends Accessor<?>> implements IComponentProvider<T>, IServerDataProvider<T> {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends EnergyStorageProvider<BlockAccessor> {
		private static final ForBlock INSTANCE = new ForBlock();
	}

	public static class ForEntity extends EnergyStorageProvider<EntityAccessor> {
		private static final ForEntity INSTANCE = new ForEntity();
	}

	public static void append(ITooltip tooltip, Accessor<?> accessor, IPluginConfig config) {
		if ((!accessor.showDetails() && config.get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED))) {
			return;
		}

		if (!accessor.getServerData().contains("JadeEnergyStorage")) {
			return;
		}

		var provider = Optional.ofNullable(ResourceLocation.tryParse(accessor.getServerData().getString("JadeEnergyStorageUid"))).map(
				WailaClientRegistration.instance().energyStorageProviders::get).orElse(null);
		if (provider == null) {
			return;
		}

		List<ClientViewGroup<EnergyView>> groups;
		try {
			groups = provider.getClientGroups(
					accessor,
					ViewGroup.readList(accessor.getServerData(), "JadeEnergyStorage", Function.identity()));
		} catch (Exception e) {
			WailaExceptionHandler.handleErr(e, provider, tooltip::add);
			return;
		}

		if (groups.isEmpty()) {
			return;
		}

		IElementHelper helper = IElementHelper.get();
		boolean renderGroup = groups.size() > 1 || groups.getFirst().shouldRenderGroup();
		ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
			if (renderGroup) {
				group.renderHeader(theTooltip);
			}
			for (var view : group.views) {
				Component text;
				if (view.overrideText != null) {
					text = view.overrideText;
				} else {
					text = Component.translatable("dainty.fe", ChatFormatting.WHITE + view.current, view.max)
							.withStyle(ChatFormatting.GRAY);
				}
				ProgressStyle progressStyle = helper.progressStyle().color(0xFFAA0000, 0xFF660000);
				theTooltip.add(helper.progress(view.ratio, text, progressStyle, BoxStyle.getNestedBox(), true));
			}
		});
	}

	public static void putData(Accessor<?> accessor) {
		CompoundTag tag = accessor.getServerData();
		for (var provider : WailaCommonRegistration.instance().energyStorageProviders.get(accessor)) {
			List<ViewGroup<CompoundTag>> groups;
			try {
				groups = provider.getGroups(accessor);
			} catch (Exception e) {
				WailaExceptionHandler.handleErr(e, provider, null);
				continue;
			}
			if (groups != null) {
				if (ViewGroup.saveList(tag, "JadeEnergyStorage", groups, Function.identity())) {
					tag.putString("JadeEnergyStorageUid", provider.getUid().toString());
				}
				return;
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.UNIVERSAL_ENERGY_STORAGE;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.BODY + 1000;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
		append(tooltip, accessor, config);
	}

	@Override
	public void appendServerData(CompoundTag data, T accessor) {
		putData(accessor);
	}

	@Override
	public boolean shouldRequestData(T accessor) {
		if (!accessor.showDetails() && IWailaConfig.get().getPlugin().get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED)) {
			return false;
		}
		for (var provider : WailaCommonRegistration.instance().energyStorageProviders.get(accessor)) {
			if (provider.shouldRequestData(accessor)) {
				return true;
			}
		}
		return false;
	}

	public enum Extension implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {
		INSTANCE;

		@Override
		public ResourceLocation getUid() {
			return JadeIds.UNIVERSAL_ENERGY_STORAGE_DEFAULT;
		}

		@Override
		public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
			return groups.stream().map($ -> {
				String unit = $.getExtraData().getString("Unit");
				return new ClientViewGroup<>($.views.stream().map(tag -> EnergyView.read(tag, unit)).filter(Objects::nonNull).toList());
			}).toList();
		}

		@Nullable
		@Override
		public List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
			return CommonProxy.wrapEnergyStorage(accessor);
		}

		@Override
		public boolean shouldRequestData(Accessor<?> accessor) {
			return CommonProxy.hasDefaultEnergyStorage(accessor);
		}

		@Override
		public int getDefaultPriority() {
			return 9999;
		}
	}

}
