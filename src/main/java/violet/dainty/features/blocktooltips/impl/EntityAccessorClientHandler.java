package violet.dainty.features.blocktooltips.impl;

import java.util.List;
import java.util.function.Function;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import violet.dainty.features.blocktooltips.api.AccessorClientHandler;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IJadeProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.impl.config.PluginConfig;
import violet.dainty.features.blocktooltips.impl.ui.ElementHelper;
import violet.dainty.features.blocktooltips.impl.ui.ItemStackElement;
import violet.dainty.features.blocktooltips.overlay.RayTracing;
import violet.dainty.features.blocktooltips.util.ClientProxy;
import violet.dainty.features.blocktooltips.util.CommonProxy;
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public class EntityAccessorClientHandler implements AccessorClientHandler<EntityAccessor> {

	@Override
	public boolean shouldDisplay(EntityAccessor accessor) {
		IWailaConfig.IConfigGeneral general = IWailaConfig.get().getGeneral();
		if (!general.getDisplayEntities()) {
			return false;
		}
		if (!general.getDisplayBosses() && CommonProxy.isBoss(accessor.getEntity())) {
			return false;
		}
		return true;
	}

	@Override
	public List<IServerDataProvider<EntityAccessor>> shouldRequestData(EntityAccessor accessor) {
		List<IServerDataProvider<EntityAccessor>> providers = WailaCommonRegistration.instance()
				.getEntityNBTProviders(accessor.getEntity());
		if (providers.isEmpty()) {
			return List.of();
		}
		return providers.stream().filter(provider -> provider.shouldRequestData(accessor)).toList();
	}

	@Override
	public void requestData(EntityAccessor accessor, List<IServerDataProvider<EntityAccessor>> providers) {
		ClientProxy.requestEntityData(accessor, providers);
	}

	@Override
	public IElement getIcon(EntityAccessor accessor) {
		IElement icon = null;
		Entity entity = accessor.getEntity();
		if (entity instanceof ItemEntity) {
			icon = ItemStackElement.of(((ItemEntity) entity).getItem());
		} else {
			ItemStack stack = accessor.getPickedResult();
			if ((!(stack.getItem() instanceof SpawnEggItem) || !(entity instanceof LivingEntity))) {
				icon = ItemStackElement.of(stack);
			}
		}

		for (var provider : WailaClientRegistration.instance().getEntityIconProviders(entity, this::isEnabled)) {
			try {
				IElement element = provider.getIcon(accessor, PluginConfig.INSTANCE, icon);
				if (!RayTracing.isEmptyElement(element)) {
					icon = element;
				}
			} catch (Throwable e) {
				WailaExceptionHandler.handleErr(e, provider, null);
			}
		}
		return icon;
	}

	@Override
	public void gatherComponents(EntityAccessor accessor, Function<IJadeProvider, ITooltip> tooltipProvider) {
		for (var provider : WailaClientRegistration.instance().getEntityProviders(accessor.getEntity(), this::isEnabled)) {
			ITooltip tooltip = tooltipProvider.apply(provider);
			try {
				ElementHelper.INSTANCE.setCurrentUid(provider.getUid());
				provider.appendTooltip(tooltip, accessor, PluginConfig.INSTANCE);
			} catch (Throwable e) {
				WailaExceptionHandler.handleErr(e, provider, tooltip::add);
			} finally {
				ElementHelper.INSTANCE.setCurrentUid(null);
			}
		}
	}

}
