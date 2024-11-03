package violet.dainty.features.blocktooltips.addon.core;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.IToggleableProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.impl.theme.ThemeHelper;

public abstract class DistanceProvider implements IToggleableProvider {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends DistanceProvider implements IBlockComponentProvider {
		private static final ForBlock INSTANCE = new ForBlock();

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			append(tooltip, accessor, accessor.getPosition(), config);
		}
	}

	public static class ForEntity extends DistanceProvider implements IEntityComponentProvider {
		private static final ForEntity INSTANCE = new ForEntity();

		@Override
		public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
			append(tooltip, accessor, accessor.getEntity().blockPosition(), config);
		}
	}

	public static final DecimalFormat fmt = new DecimalFormat("#.#");
	private static final int[] colors = {0xef9a9a, 0xa5d6a7, 0x90caf9, 0xb02a37, 0x198754, 0x0a58ca};

	public static String distance(Accessor<?> accessor) {
		float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
		return fmt.format(accessor.getPlayer().getEyePosition(partialTick).distanceTo(accessor.getHitResult().getLocation()));
	}

	public static void xyz(ITooltip tooltip, Vec3i pos) {
		Component display = Component.translatable("dainty.blockpos", display(pos.getX(), 0), display(pos.getY(), 1), display(pos.getZ(), 2));
		String narrate = I18n.get("narration.dainty.blockpos", narrate(pos.getX()), narrate(pos.getY()), narrate(pos.getZ()));
		tooltip.add(IElementHelper.get().text(display).message(narrate));
	}

	public static Component display(int i, int colorIndex) {
		if (IThemeHelper.get().isLightColorScheme()) {
			colorIndex += 3;
		}
		return Component.literal(Integer.toString(i)).withStyle(ThemeHelper.colorStyle(colors[colorIndex]));
	}

	public static String narrate(int i) {
		return i >= 0 ? Integer.toString(i) : I18n.get("narration.dainty.negative", -i);
	}

	public void append(ITooltip tooltip, Accessor<?> accessor, BlockPos pos, IPluginConfig config) {
		boolean distance = config.get(JadeIds.CORE_DISTANCE);
		String distanceVal = distance ? distance(accessor) : null;
		String distanceMsg = distance ? I18n.get("narration.dainty.distance", distanceVal) : null;
		if (config.get(JadeIds.CORE_COORDINATES)) {
			if (config.get(JadeIds.CORE_REL_COORDINATES) && Screen.hasControlDown()) {
				xyz(tooltip, pos.subtract(BlockPos.containing(accessor.getPlayer().getEyePosition())));
			} else {
				xyz(tooltip, pos);
			}
			if (distance) {
				tooltip.append(IElementHelper.get().text(Component.translatable("dainty.distance1", distanceVal)).message(distanceMsg));
			}
		} else if (distance) {
			tooltip.add(IElementHelper.get().text(Component.translatable("dainty.distance2", distanceVal)).message(distanceMsg));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.CORE_DISTANCE;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public int getDefaultPriority() {
		return -4600;
	}

}
