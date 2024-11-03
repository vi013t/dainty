package violet.dainty.features.blocktooltips.impl;

import java.util.List;
import java.util.function.Function;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.blocktooltips.api.AccessorClientHandler;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
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
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public class BlockAccessorClientHandler implements AccessorClientHandler<BlockAccessor> {

	@Override
	public boolean shouldDisplay(BlockAccessor accessor) {
		return IWailaConfig.get().getGeneral().getDisplayBlocks();
	}

	@Override
	public List<IServerDataProvider<BlockAccessor>> shouldRequestData(BlockAccessor accessor) {
		List<IServerDataProvider<BlockAccessor>> providers = WailaCommonRegistration.instance().getBlockNBTProviders(
				accessor.getBlock(),
				accessor.getBlockEntity());
		if (providers.isEmpty()) {
			return List.of();
		}
		return providers.stream().filter(provider -> provider.shouldRequestData(accessor)).toList();
	}

	@Override
	public void requestData(BlockAccessor accessor, List<IServerDataProvider<BlockAccessor>> providers) {
		ClientProxy.requestBlockData(accessor, providers);
	}

	@Override
	public IElement getIcon(BlockAccessor accessor) {
		BlockState blockState = accessor.getBlockState();
		Block block = blockState.getBlock();
		if (blockState.isAir()) {
			return null;
		}
		IElement icon = null;

		if (accessor.isFakeBlock()) {
			icon = ItemStackElement.of(accessor.getFakeBlock());
		} else {
			ItemStack pick = accessor.getPickedResult();
			if (!pick.isEmpty()) {
				icon = ItemStackElement.of(pick);
			}
		}

		if (RayTracing.isEmptyElement(icon) && block.asItem() != Items.AIR) {
			icon = ItemStackElement.of(new ItemStack(block));
		}

		if (RayTracing.isEmptyElement(icon) && block instanceof LiquidBlock) {
			icon = ClientProxy.elementFromLiquid(blockState);
		}

		for (var provider : WailaClientRegistration.instance().getBlockIconProviders(block, this::isEnabled)) {
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
	public void gatherComponents(BlockAccessor accessor, Function<IJadeProvider, ITooltip> tooltipProvider) {
		for (var provider : WailaClientRegistration.instance().getBlockProviders(accessor.getBlock(), this::isEnabled)) {
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
