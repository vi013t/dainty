package violet.dainty.features.blocktooltips.addon.universal;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.TooltipPosition;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.BoxStyle;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.api.ui.ScreenDirection;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.ProgressView;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public abstract class ProgressProvider<T extends Accessor<?>> implements IComponentProvider<T>, IServerDataProvider<T> {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends ProgressProvider<BlockAccessor> {
		private static final ForBlock INSTANCE = new ForBlock();
	}

	public static class ForEntity extends ProgressProvider<EntityAccessor> {
		private static final ForEntity INSTANCE = new ForEntity();
	}

	public static void append(ITooltip tooltip, Accessor<?> accessor, IPluginConfig config) {
		if (!accessor.getServerData().contains("JadeProgress")) {
			return;
		}

		var provider = Optional.ofNullable(ResourceLocation.tryParse(accessor.getServerData().getString("JadeProgressUid"))).map(
				WailaClientRegistration.instance().progressProviders::get).orElse(null);
		if (provider == null) {
			return;
		}

		List<ClientViewGroup<ProgressView>> groups;
		try {
			groups = provider.getClientGroups(
					accessor,
					ViewGroup.readList(accessor.getServerData(), "JadeProgress", Function.identity()));
		} catch (Exception e) {
			WailaExceptionHandler.handleErr(e, provider, tooltip::add);
			return;
		}

		if (groups.isEmpty()) {
			return;
		}

		IElementHelper helper = IElementHelper.get();
		boolean renderGroup = groups.size() > 1 || groups.getFirst().shouldRenderGroup();
		BoxStyle.GradientBorder boxStyle = BoxStyle.getTransparent().clone();
		boxStyle.bgColor = 0x44FFFFFF;
		ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
			if (renderGroup) {
				group.renderHeader(theTooltip);
			}
			for (var view : group.views) {
				if (view.text != null) {
					theTooltip.add(helper.text(view.text).scale(0.75F));
					theTooltip.setLineMargin(-1, ScreenDirection.DOWN, 0);
				}
				theTooltip.add(helper.progress(view.progress, null, view.style, boxStyle, false).size(new Vec2(10, 2)));
			}
		});
	}

	public static void putData(Accessor<?> accessor) {
		CompoundTag tag = accessor.getServerData();
		for (var provider : WailaCommonRegistration.instance().progressProviders.get(accessor)) {
			List<ViewGroup<CompoundTag>> groups;
			try {
				groups = provider.getGroups(accessor);
			} catch (Exception e) {
				WailaExceptionHandler.handleErr(e, provider, null);
				continue;
			}
			if (groups != null) {
				if (ViewGroup.saveList(tag, "JadeProgress", groups, Function.identity())) {
					tag.putString("JadeProgressUid", provider.getUid().toString());
				}
				return;
			}
		}
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
		for (var provider : WailaCommonRegistration.instance().progressProviders.get(accessor)) {
			if (provider.shouldRequestData(accessor)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.UNIVERSAL_PROGRESS;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.BODY + 1000;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
