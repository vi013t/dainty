package violet.dainty.features.structurecompass.sorting;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.features.structurecompass.util.StructureUtils;

@OnlyIn(Dist.CLIENT)
public class DimensionSorting implements ISorting {
	
	@Override
	public int compare(ResourceLocation key1, ResourceLocation key2) {
		return StructureUtils.dimensionKeysToString(StructureCompass.dimensionKeysForAllowedStructureKeys.get(key1)).compareTo(StructureUtils.dimensionKeysToString(StructureCompass.dimensionKeysForAllowedStructureKeys.get(key2)));
	}

	@Override
	public Object getValue(ResourceLocation key) {
		return StructureUtils.dimensionKeysToString(StructureCompass.dimensionKeysForAllowedStructureKeys.get(key));
	}

	@Override
	public ISorting next() {
		return new GroupSorting();
	}

	@Override
	public String getLocalizedName() {
		return I18n.get("string.dainty.dimension");
	}

}
