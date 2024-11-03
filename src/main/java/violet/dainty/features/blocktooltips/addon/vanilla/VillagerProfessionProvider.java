package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.util.CommonProxy;

// @MerchantScreen / Villager.getTypeName
public enum VillagerProfessionProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final Component LEVEL_SEPARATOR = Component.literal(" - ");

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		VillagerData data = null;
		if (accessor.getEntity() instanceof Villager) {
			data = ((Villager) accessor.getEntity()).getVillagerData();
		} else if (accessor.getEntity() instanceof ZombieVillager) {
			data = ((ZombieVillager) accessor.getEntity()).getVillagerData();
		}
		if (data == null) {
			return;
		}
		int level = data.getLevel();
		VillagerProfession profession = data.getProfession();
		MutableComponent component = CommonProxy.getProfessionName(profession);
		if (profession != VillagerProfession.NONE && profession != VillagerProfession.NITWIT && level > 0 && level <= 5) {
			component.append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + level));
		}
		tooltip.add(component);
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_VILLAGER_PROFESSION;
	}

}
