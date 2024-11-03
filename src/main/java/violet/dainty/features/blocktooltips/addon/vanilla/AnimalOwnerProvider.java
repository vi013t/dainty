package violet.dainty.features.blocktooltips.addon.vanilla;

import java.util.UUID;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.StreamServerDataProvider;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.util.CommonProxy;

public enum AnimalOwnerProvider implements IEntityComponentProvider, StreamServerDataProvider<EntityAccessor, String> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		String name = decodeFromData(accessor).orElse("");
		if (name.isEmpty()) {
			UUID ownerUUID = getOwnerUUID(accessor.getEntity());
			if (ownerUUID == null) {
				return;
			}
			name = CommonProxy.getLastKnownUsername(ownerUUID);
			if (name == null) {
				name = "???";
			}
		}
		tooltip.add(Component.translatable("dainty.owner", name));
	}

	@Override
	public String streamData(EntityAccessor accessor) {
		return CommonProxy.getLastKnownUsername(getOwnerUUID(accessor.getEntity()));
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, String> streamCodec() {
		return ByteBufCodecs.STRING_UTF8.cast();
	}

	public static UUID getOwnerUUID(Entity entity) {
		if (entity instanceof OwnableEntity ownableEntity) {
			return ownableEntity.getOwnerUUID();
		}
		return null;
	}

	@Override
	public boolean shouldRequestData(EntityAccessor accessor) {
		Entity entity = accessor.getEntity();
		return entity instanceof OwnableEntity && getOwnerUUID(entity) == null;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ANIMAL_OWNER;
	}

}
