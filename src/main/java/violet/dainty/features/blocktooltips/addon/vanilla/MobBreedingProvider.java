package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.allay.Allay;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.StreamServerDataProvider;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum MobBreedingProvider implements IEntityComponentProvider, StreamServerDataProvider<EntityAccessor, Integer> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		int time = decodeFromData(accessor).orElse(0);
		if (time > 0) {
			tooltip.add(Component.translatable(
					accessor.getEntity() instanceof Allay ? "dainty.mobduplication.time" : "dainty.mobbreeding.time",
					IThemeHelper.get().seconds(time, accessor.tickRate())));
		}
	}

	@Override
	public @Nullable Integer streamData(EntityAccessor accessor) {
		int time = 0;
		Entity entity = accessor.getEntity();
		if (entity instanceof Allay allay) {
			if (allay.duplicationCooldown > 0 && allay.duplicationCooldown < Integer.MAX_VALUE) {
				time = (int) allay.duplicationCooldown;
			}
		} else {
			time = ((Animal) entity).getAge();
		}
		return time > 0 ? time : null;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, Integer> streamCodec() {
		return ByteBufCodecs.VAR_INT.cast();
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_MOB_BREEDING;
	}
}
