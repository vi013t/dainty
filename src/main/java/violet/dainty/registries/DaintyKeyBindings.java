package violet.dainty.registries;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class DaintyKeyBindings {

	private static Set<Lazy<KeyMapping>> KEYS = new HashSet<>();

	public static final Lazy<KeyMapping> VEINMINE = key("veinmine", GLFW.GLFW_KEY_GRAVE_ACCENT, KeyConflictContext.IN_GAME);
	public static final Lazy<KeyMapping> CRAWL = key("crawl", GLFW.GLFW_KEY_LEFT_ALT, KeyConflictContext.IN_GAME);

	private static Lazy<KeyMapping> key(String name, int defaultKey, KeyConflictContext context) {
		var keybinding = Lazy.of(() -> new KeyMapping(
			"key.dainty." + name,
			context,
			InputConstants.Type.KEYSYM,
			defaultKey,
			"key.dainty.category"
		));
		KEYS.add(keybinding);
		return keybinding;
	}

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event) {
		for (var keybinding : KEYS) {
			event.register(keybinding.get());
		}
	}
}
