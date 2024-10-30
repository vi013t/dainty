package violet.dainty.features.carryon;

import java.util.Collections;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScriptReloadListener extends SimpleJsonResourceReloadListener {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public ScriptReloadListener() {
		super(GSON, "dainty/scripts");
	}

	@Override
	protected void apply(@SuppressWarnings("null") Map<ResourceLocation, JsonElement> objects, @SuppressWarnings("null") ResourceManager manager, @SuppressWarnings("null") ProfilerFiller profiler) {
		ScriptManager.SCRIPTS.clear();
		try {
			objects.forEach((path, jsonElem) -> {
				DataResult<CarryOnScript> res = CarryOnScript.CODEC.parse(JsonOps.INSTANCE, jsonElem);
				if(res.result().isPresent()) {
					CarryOnScript script = res.result().get();
					if (script.isValid())
						ScriptManager.SCRIPTS.add(script);
				}
				else System.err.println("Error while parsing script: " + res.error().get().message());
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(ScriptManager.SCRIPTS, (s1, s2) -> Long.compare(s2.priority(), s1.priority()));
	}

	public static void syncScriptsWithClient(ServerPlayer player) {
		if (player != null) {
			DataResult<Tag> result = Codec.list(CarryOnScript.CODEC).encodeStart(NbtOps.INSTANCE, ScriptManager.SCRIPTS);
			Tag tag = result.getOrThrow(s -> {throw new RuntimeException("Error while synching Carry On Scripts: " + s);});
			PacketDistributor.sendToPlayer(player, new ClientboundSyncScriptsPacket(tag));
		}
	}
}