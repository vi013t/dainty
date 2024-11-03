package violet.dainty.features.structurecompass.sorting;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import violet.dainty.features.structurecompass.StructureCompass;

@OnlyIn(Dist.CLIENT)
public class GroupSorting implements ISorting {
	
	@Override
	public int compare(ResourceLocation key1, ResourceLocation key2) {
		return StructureCompass.structureKeysToTypeKeys.get(key1).compareTo(StructureCompass.structureKeysToTypeKeys.get(key2));
	}

	@Override
	public Object getValue(ResourceLocation key) {
		return StructureCompass.structureKeysToTypeKeys.get(key);
	}

	@Override
	public ISorting next() {
		return new NameSorting();
	}

	@Override
	public String getLocalizedName() {
		return I18n.get("string.dainty.group");
	}

}
