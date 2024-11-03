package violet.dainty.features.structurecompass.sorting;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import violet.dainty.features.structurecompass.util.StructureUtils;

@OnlyIn(Dist.CLIENT)
public class NameSorting implements ISorting {

	@Override
	public int compare(ResourceLocation key1, ResourceLocation key2) {
		return StructureUtils.getPrettyStructureName(key1).compareTo(StructureUtils.getPrettyStructureName(key2));
	}

	@Override
	public Object getValue(ResourceLocation key) {
		return StructureUtils.getPrettyStructureName(key);
	}

	@Override
	public ISorting next() {
		return new SourceSorting();
	}

	@Override
	public String getLocalizedName() {
		return I18n.get("string.dainty.name");
	}

}
