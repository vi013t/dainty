package violet.dainty.features.carryon.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.DataResult;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class ModelOverrideHandler {

	private static String[] modelOverrides = {
		"minecraft:redstone_wire->(item)minecraft:redstone",
		"minecraft:bamboo_sapling->(block)minecraft:bamboo",
		"minecraft:candle_cake->(block)minecraft:cake"
	};

	private static List<ModelOverride> OVERRIDES = new ArrayList<>();

	public static void initModelOverrides(HolderLookup.Provider provider) {
		OVERRIDES.clear();

		for (String ov : modelOverrides) {
			addFromString(ov, provider);
		}
	}

	public static Optional<ModelOverride> getModelOverride(BlockState state, @Nullable CompoundTag tag) {
		for(ModelOverride ov : OVERRIDES) {
			if (ov.matches(state, tag)) return Optional.of(ov);
		}
		return Optional.empty();
	}

	public static void addFromString(String str, HolderLookup.Provider provider) {
		DataResult<ModelOverride> res = ModelOverride.of(str, provider);
		if (res.result().isPresent()) {
			ModelOverride override = res.result().get();
			OVERRIDES.add(override);
		}
		else {
			System.err.println("Error while parsing ModelOverride: " + res.error().get().message());
		}
	}

}