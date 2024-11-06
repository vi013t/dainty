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

/**
 * The registry class for keybindings. This class handles the instantiation and registration of all keybindings used by
 * Dainty. It's marked as {@link EventBusSubscriber an event listener} so that it can {@link #register(RegisterKeyMappingsEvent)
 * listen to the keybinding registration event}.
 */
@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class DaintyKeyBindings {

	/**
	 * The set of all keybindings in the mod. Keybindings are added to this set when they are created using 
	 * {@link #key(String, int, KeyConflictContext) the key function}, and this is used to register the keybindings in
	 * {@link #register(RegisterKeyMappingsEvent) the register method}.
	 */
	private static Set<Lazy<KeyMapping>> KEYS = new HashSet<>();

	/**
	 * The vein-mine keybinding. Holding this will allow the player to break multiple blocks at once, or chance the pattern
	 * of their vein-mine. The logic for this is handled in 
	 * {@link violet.dainty.features.veinmine.VeinMineEventHandler#veinMine(net.neoforged.neoforge.event.level.BlockEvent.BreakEvent)
	 * the corresponding part of the vein mine event handler}.
	 * 
	 * <br/><br/>
	 * 
	 * The default key for this binding is {@link GLFW#GLFW_KEY_GRAVE_ACCENT tilde}, and this is an "in-game" (not GUI) keybinding.
	 */
	public static final Lazy<KeyMapping> VEINMINE = key("veinmine", GLFW.GLFW_KEY_GRAVE_ACCENT, KeyConflictContext.IN_GAME);

	/**
	 * The crawl keybinding. Holding this will force the player into "crawl" mode. The logic for this is handled in
	 * {@link violet.dainty.features.crawl.CrawlEventListener#crawl(net.neoforged.neoforge.client.event.ClientTickEvent.Post)
	 * the corresponding part of the crawl event handler}.
	 * 
	 * <br/><br/>
	 * 
	 * The default key for this binding is {@link GLFW#GLFW_KEY_LEFT_ALT left alt}, and this is an "in-game" (not GUI) keybinding.
	 */
	public static final Lazy<KeyMapping> CRAWL = key("crawl", GLFW.GLFW_KEY_LEFT_ALT, KeyConflictContext.IN_GAME);

	/**
	 * The zoom keybinding. Holding this will "zoom in" on the player's client. The logic for this is handled in
	 * {@link violet.dainty.features.zoom.ZoomEventHandler#checkForZoom(net.neoforged.neoforge.client.event.ClientTickEvent.Post)
	 * the corresponding part of the zoom event handler}.
	 * 
	 * <br/><br/>
	 * 
	 * The default key for this binding is {@link GLFW#GLFW_KEY_Z Z}, and this is an "in-game" (not GUI) keybinding.
	 */
	public static final Lazy<KeyMapping> ZOOM = key("zoom", GLFW.GLFW_KEY_Z, KeyConflictContext.IN_GAME);

	/**
	 * Creates a new keybinding. The keybinding is added to {@link #KEYS the set of all keybindings in the mod}, and
	 * returned.
	 * 
	 * @param name The name of the keybinding. This should be a unique identifier for the keybinding (distinct from
	 * all other keybindings in the mod), and it will be the key used in language files.
	 * @param defaultKey The default key for this keybinding. Use the static fields of the {@link GLFW} class for key
	 * values, such as {@link GLFW#GLFW_KEY_Z} for {@code Z}.
	 * @param context The key conflict context. This describes the context that the keybinding is used in, such as 
	 * "in-game" or in "gui". See {@link KeyConflictContext} for more information.
	 * 
	 * @return The registered keybinding as a {@link Lazy} {@link KeyMapping}, as that's the convention described by
	 * <a href="https://docs.neoforged.net/docs/misc/keymappings/#registering-a-keymapping">the corresponding part of
	 * the Neoforge documentation</a>
	 */
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

	/**
	 * Registers the key bindings for the mod. This uses {@link #KEYS the set of all keybindings in the mod} to find
	 * all keybindings and register them. Naturally, this event is fired on {@link Bus#MOD the mod event bus}.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The key mapping registration event given by Neoforge.
	 */
	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event) {
		for (var keybinding : KEYS) {
			event.register(keybinding.get());
		}
	}
}
