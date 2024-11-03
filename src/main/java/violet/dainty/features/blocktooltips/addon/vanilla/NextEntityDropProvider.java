package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum NextEntityDropProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		appendSeconds(tooltip, accessor, "NextEggIn", "dainty.nextEgg");
		appendSeconds(tooltip, accessor, "NextScuteIn", "dainty.nextScute");
	}

	public static void appendSeconds(ITooltip tooltip, Accessor<?> accessor, String tagKey, String translationKey) {
		if (accessor.getServerData().contains(tagKey)) {
			tooltip.add(Component.translatable(
					translationKey,
					IThemeHelper.get().seconds(accessor.getServerData().getInt(tagKey), accessor.tickRate())));
		}
	}

	@Override
	public void appendServerData(CompoundTag tag, EntityAccessor accessor) {
		int max = 24000 * 2;
		if (accessor.getEntity() instanceof Chicken chicken) {
			if (!chicken.isBaby() && chicken.eggTime < max) {
				tag.putInt("NextEggIn", chicken.eggTime);
			}
		} else if (accessor.getEntity() instanceof Armadillo armadillo) {
			if (!armadillo.isBaby() && armadillo.scuteTime < max) {
				tag.putInt("NextScuteIn", armadillo.scuteTime);
			}
		}
	}

	@Override
	public boolean shouldRequestData(EntityAccessor accessor) {
		if (accessor.getEntity() instanceof LivingEntity living) {
			return !living.isBaby();
		}
		return true;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_NEXT_ENTITY_DROP;
	}

}
