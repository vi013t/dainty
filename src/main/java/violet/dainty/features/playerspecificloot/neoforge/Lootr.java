package violet.dainty.features.playerspecificloot.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.command.CommandLootr;
import violet.dainty.features.playerspecificloot.neoforge.impl.LootrAPIImpl;
import violet.dainty.features.playerspecificloot.neoforge.impl.LootrRegistryImpl;
import violet.dainty.features.playerspecificloot.neoforge.init.ModAdvancements;
import violet.dainty.features.playerspecificloot.neoforge.init.ModBlockEntities;
import violet.dainty.features.playerspecificloot.neoforge.init.ModBlocks;
import violet.dainty.features.playerspecificloot.neoforge.init.ModEntities;
import violet.dainty.features.playerspecificloot.neoforge.init.ModItems;
import violet.dainty.features.playerspecificloot.neoforge.init.ModLoot;
import violet.dainty.features.playerspecificloot.neoforge.init.ModStats;
import violet.dainty.features.playerspecificloot.neoforge.network.PacketHandler;

public class Lootr {
	public static Lootr instance;
	private final PacketHandler packetHandler;

	public CommandLootr COMMAND_LOOTR;

	public Lootr(ModContainer modContainer, IEventBus modBus) {
		instance = this;
		LootrAPI.INSTANCE = new LootrAPIImpl();
		LootrRegistry.INSTANCE = new LootrRegistryImpl();
		NeoForge.EVENT_BUS.addListener(this::onCommands);
		ModBlockEntities.register(modBus);
		ModBlocks.register(modBus);
		ModEntities.register(modBus);
		ModItems.register(modBus);
		ModLoot.register(modBus);
		ModStats.register(modBus);
		ModAdvancements.register(modBus);
		this.packetHandler = new PacketHandler(modBus);
	}

	public static ResourceLocation rl(String path) {
		return LootrAPI.rl(path);
	}

	public static PacketHandler getPacketHandler() {
		return instance.packetHandler;
	}

	public void onCommands(RegisterCommandsEvent event) {
		COMMAND_LOOTR = new CommandLootr(event.getDispatcher());
		COMMAND_LOOTR.register();
	}
}
