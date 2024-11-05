package violet.dainty.features.biomecompass.sorting;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.biomecompass.util.BiomeUtils;

public class DimensionSorting implements ISorting<String> {
	
	private static final Minecraft mc = Minecraft.getInstance();

	@Override
	public int compare(Biome biome1, Biome biome2) {
		return getValue(biome1).compareTo(getValue(biome2));
	}

	@Override
	public String getValue(Biome biome) {
		if (mc.level != null) {
			Optional<ResourceLocation> optionalBiomeKey = BiomeUtils.getKeyForBiome(mc.level, biome);
			if (optionalBiomeKey.isPresent()) {
				return BiomeUtils.dimensionKeysToString(BiomeCompass.dimensionKeysForAllowedBiomeKeys.get(optionalBiomeKey.get()));
			}
		}
		return "";
	}

	@Override
	public ISorting<?> next() {
		return new RainfallSorting();
	}

	@Override
	public String getLocalizedName() {
		return I18n.get("string.naturescompass.dimension");
	}

}
