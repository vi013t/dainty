package violet.dainty.features.structurecompass;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import violet.dainty.Dainty;
import violet.dainty.features.structurecompass.items.NaturesCompassItem;
import violet.dainty.features.structurecompass.network.SearchPacket;
import violet.dainty.features.structurecompass.network.SyncPacket;
import violet.dainty.features.structurecompass.network.TeleportPacket;
import violet.dainty.item.DaintyItems;

public class StructureCompass {

	public static final PermissionNode<Boolean> TELEPORT_PERMISSION = new PermissionNode<>(Dainty.MODID, "dainty.teleportstructure", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> false);

	public static NaturesCompassItem naturesCompass;
	
	public static final DataComponentType<String> BIOME_ID = DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build();
	public static final DataComponentType<Integer> COMPASS_STATE = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> FOUND_X = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> FOUND_Z = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> SEARCH_RADIUS = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Integer> SAMPLES = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();
	public static final DataComponentType<Boolean> DISPLAY_COORDS = DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();
	
	public static boolean canTeleport;
	public static List<ResourceLocation> allowedBiomes;
	public static ListMultimap<ResourceLocation, ResourceLocation> dimensionKeysForAllowedBiomeKeys;

	public static StructureCompass instance;

	public StructureCompass(ModContainer modContainer) {
		instance = this;

		modContainer.getEventBus().addListener(this::preInit);
		modContainer.getEventBus().addListener(this::buildCreativeTabContents);
		modContainer.getEventBus().addListener(this::registerPayloads);

		NeoForge.EVENT_BUS.register(this);
	}

	private void preInit(FMLCommonSetupEvent event) {
		allowedBiomes = new ArrayList<ResourceLocation>();
		dimensionKeysForAllowedBiomeKeys = ArrayListMultimap.create();
	}

	private void registerPayloads(RegisterPayloadHandlersEvent event) {
	    final PayloadRegistrar registrar = event.registrar(Dainty.MODID);
	    registrar.playToServer(SearchPacket.TYPE, SearchPacket.CODEC, SearchPacket::handle);
	    registrar.playToServer(TeleportPacket.TYPE, TeleportPacket.CODEC, TeleportPacket::handle);
	    registrar.playToClient(SyncPacket.TYPE, SyncPacket.CODEC, SyncPacket::handle);
	}

	private void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == DaintyItems.CREATIVE_TAB.get()) {
			event.accept(new ItemStack(naturesCompass));
		}
	}

	@SubscribeEvent
	public void registerNodes(PermissionGatherEvent.Nodes event) {
		event.addNodes(TELEPORT_PERMISSION);
	}

}
