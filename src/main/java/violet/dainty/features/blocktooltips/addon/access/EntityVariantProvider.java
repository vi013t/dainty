package violet.dainty.features.blocktooltips.addon.access;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;

public class EntityVariantProvider implements IEntityComponentProvider {
	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		Entity entity = accessor.getEntity();
		String variantKey = null;
		variantChecks:
		if (entity instanceof VariantHolder<?> variantHolder) {
			if (entity instanceof VillagerDataHolder) {
				break variantChecks;
			}
			Object variant = variantHolder.getVariant();
			if (variant instanceof StringRepresentable stringRepresentable) {
				variantKey = stringRepresentable.getSerializedName();
			} else if (variant instanceof Holder<?> holder) {
				variantKey = holder.unwrapKey().map(ResourceKey::location).map(ResourceLocation::toShortLanguageKey).orElse(null);
			}
		}
		if (variantKey != null) {
			String type = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toShortLanguageKey();
			String key = "dainty.access.entity.%s.%s".formatted(type, variantKey);
			if (I18n.exists(key) || (config.get(JadeIds.DEBUG_SPECIAL_REGISTRY_NAME) && !accessor.showDetails())) {
				variantKey = I18n.get(key);
			} else {
				variantKey = variantKey.replace('.', ' ').replace('_', ' ');
			}
			tooltip.add(Component.translatable("dainty.access.entity.variant", variantKey));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.ACCESS_ENTITY_VARIANT;
	}
}
