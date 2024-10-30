package violet.dainty.features.carryon;

import org.apache.logging.log4j.util.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import violet.dainty.features.carryon.CarryOnData.CarryType;

public class CarryOnCommon {
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();

	public static HolderLookup.Provider createLookup() {
		RegistryAccess.Frozen registryaccess$frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		HolderLookup.Provider holderlookup$provider = BUILDER.build(registryaccess$frozen);
		return holderlookup$provider;
	}

	@SuppressWarnings("unchecked")
    public static <T extends PacketBase, B extends FriendlyByteBuf> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<B, T> codec, BiConsumer<T, Player> handler, Object... args) {
        PayloadRegistrar registrar = (PayloadRegistrar) args[0];

        IPayloadHandler<T> serverHandler = (packet, ctx) -> {
            ctx.enqueueWork(() -> {
                handler.accept(packet, ctx.player());
            });
        };

        registrar.playToServer(type, (StreamCodec<RegistryFriendlyByteBuf, T>) codec, serverHandler);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketBase, B extends FriendlyByteBuf> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<B, T> codec, BiConsumer<T, Player> handler, Object... args) {
        PayloadRegistrar registrar = (PayloadRegistrar) args[0];
        IPayloadHandler<T> clientHandler = (packet, ctx) -> {
            ctx.enqueueWork(() -> {
                handler.accept(packet, CarryOnCommonClient.getPlayer());
            });
        };

        registrar.playToClient(type, (StreamCodec<RegistryFriendlyByteBuf, T>) codec, clientHandler);
    }

	public static void registerServerPackets(Object... args) {
		registerServerboundPacket(
			ServerboundCarryKeyPressedPacket.TYPE,
			ServerboundCarryKeyPressedPacket.class,
			ServerboundCarryKeyPressedPacket.CODEC,
			ServerboundCarryKeyPressedPacket::handle,
			args
		);
	}

	public static void registerClientPackets(Object... args) {
		registerClientboundPacket(
			ClientboundStartRidingPacket.TYPE,
			ClientboundStartRidingPacket.class,
			ClientboundStartRidingPacket.CODEC,
			ClientboundStartRidingPacket::handle,
			args
		);

		registerClientboundPacket(
			ClientboundSyncScriptsPacket.TYPE,
			ClientboundSyncScriptsPacket.class,
			ClientboundSyncScriptsPacket.CODEC,
			ClientboundSyncScriptsPacket::handle,
			args
		);
	}

	@SuppressWarnings("null")
	public static void onCarryTick(ServerPlayer player) {
	    CarryOnData carry = CarryOnDataManager.getCarryData(player);
	    if(carry.isCarrying()) {
	        if(carry.getActiveScript().isPresent()) {
	            String cmd = carry.getActiveScript().get().scriptEffects().commandLoop();
	            if (!cmd.isEmpty()) player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack(), "/execute as " + player.getGameProfile().getName() + " run " + cmd);
	        }

		    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, potionLevel(carry, player.level()), false, false));

		    Inventory inv = player.getInventory();
			inv.selected = carry.getSelected();
	    }
	}

	/**
	 * Returns true if the block can be broken.
	 */
	public static boolean onTryBreakBlock(Player player) {
		if (player != null) {
			CarryOnData carry = CarryOnDataManager.getCarryData(player);
			if (carry.isCarrying()) return false;
		}
		return true;
	}

	/**
	 * Returns true of the entity can be attacked
	 */
	public static boolean onAttackedByPlayer(Player player) {
		if (player != null) {
			CarryOnData carry = CarryOnDataManager.getCarryData(player);
			if(carry.isCarrying())
				return false;
		}
		return true;
	}

	public static void onPlayerAttacked(Player player) {}

	private static int potionLevel(CarryOnData carry, Level level) {
		if (carry.isCarrying(CarryType.PLAYER)) return 1;
		if (carry.isCarrying(CarryType.ENTITY)) {
			Entity entity = carry.getEntity(level);
			int i = (int) (entity.getBbHeight() * entity.getBbWidth());
			if (i > 4) i = 4;
			return (int) i;
		}
		if (carry.isCarrying(CarryType.BLOCK)) {
			String nbt = carry.getNbt().toString();
			int i = nbt.length() / 500;

			if (i > 4) i = 4;
			return (int) i;
		}
		return 0;
	}
}