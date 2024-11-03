package violet.dainty.features.blocktooltips.addon.debug;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.level.material.FluidState;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.IToggleableProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.TooltipPosition;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.util.CommonProxy;

public abstract class RegistryNameProvider implements IToggleableProvider {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends RegistryNameProvider implements IBlockComponentProvider {
		private static final ForBlock INSTANCE = new ForBlock();

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			if (append(tooltip, CommonProxy.getId(accessor.getBlock()).toString(), config) &&
					config.get(JadeIds.DEBUG_SPECIAL_REGISTRY_NAME)) {
				if (accessor.getBlockEntity() != null) {
					ResourceLocation id = CommonProxy.getId(accessor.getBlockEntity().getType());
					String s = I18n.get("config.dainty.plugin_dainty.registry_name.special.block_entity_type", id);
					tooltip.add(IWailaConfig.get().getFormatting().registryName(s), JadeIds.DEBUG_SPECIAL_REGISTRY_NAME);
				}
				FluidState fluidState = accessor.getBlockState().getFluidState();
				if (!fluidState.isEmpty()) {
					ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluidState.getType());
					String s = I18n.get("config.dainty.plugin_dainty.registry_name.special.fluid", id);
					tooltip.add(IWailaConfig.get().getFormatting().registryName(s), JadeIds.DEBUG_SPECIAL_REGISTRY_NAME);
				}
				Optional<Holder<PoiType>> poiTypeHolder = PoiTypes.forState(accessor.getBlockState());
				if (poiTypeHolder.isPresent()) {
					ResourceLocation id = poiTypeHolder.get().unwrapKey().orElseThrow().location();
					String s = I18n.get("config.dainty.plugin_dainty.registry_name.special.poi", id);
					tooltip.add(IWailaConfig.get().getFormatting().registryName(s), JadeIds.DEBUG_SPECIAL_REGISTRY_NAME);
				}
			}
		}
	}

	public static class ForEntity extends RegistryNameProvider implements IEntityComponentProvider {
		private static final ForEntity INSTANCE = new ForEntity();

		@Override
		public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
			if (append(tooltip, CommonProxy.getId(accessor.getEntity().getType()).toString(), config) &&
					config.get(JadeIds.DEBUG_SPECIAL_REGISTRY_NAME)) {
				if (accessor.getEntity() instanceof Painting painting) {
					ResourceLocation id = painting.getVariant().unwrapKey().orElseThrow().location();
					String s = I18n.get("config.dainty.plugin_dainty.registry_name.special.painting", id);
					tooltip.add(IWailaConfig.get().getFormatting().registryName(s), JadeIds.DEBUG_SPECIAL_REGISTRY_NAME);
				}
			}
		}
	}

	public boolean append(ITooltip tooltip, String id, IPluginConfig config) {
		Mode mode = config.getEnum(JadeIds.DEBUG_REGISTRY_NAME);
		if (mode == Mode.OFF) {
			return false;
		}
		if (mode == Mode.ADVANCED_TOOLTIPS && !Minecraft.getInstance().options.advancedItemTooltips) {
			return false;
		}
		tooltip.add(IWailaConfig.get().getFormatting().registryName(id));
		return true;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.DEBUG_REGISTRY_NAME;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.HEAD + 100;
	}

	public enum Mode {
		ON, OFF, ADVANCED_TOOLTIPS
	}

}
