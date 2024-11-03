package violet.dainty.features.carryon.client;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.carryon.ServerboundCarryKeyPressedPacket;

public class CarryOnKeybinds {
	public static KeyMapping carryKey;

	public static void registerKeybinds(Consumer<KeyMapping> registrar) {
		carryKey = new ConflictFreeKeyMapping("key.carry.desc", InputConstants.KEY_LSHIFT, "key.dainty.category");
		registrar.accept(carryKey);
	}

	public static void onCarryKey(boolean pressed) {
		PacketDistributor.sendToServer(new ServerboundCarryKeyPressedPacket(pressed));
	}

}