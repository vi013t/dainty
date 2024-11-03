package violet.dainty.features.blocktooltips.addon.vanilla;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import violet.dainty.features.blocktooltips.addon.core.ObjectNameProvider;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.IToggleableProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public abstract class MobSpawnerProvider implements IToggleableProvider {

	public static ForBlock getBlock() {
		return ForBlock.INSTANCE;
	}

	public static ForEntity getEntity() {
		return ForEntity.INSTANCE;
	}

	public static class ForBlock extends MobSpawnerProvider implements IBlockComponentProvider {
		private static final ForBlock INSTANCE = new ForBlock();

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			MutableComponent name = accessor.getBlock().getName();
			Level level = accessor.getLevel();
			BlockPos pos = accessor.getPosition();
			if (accessor.getBlockEntity() instanceof SpawnerBlockEntity spawner) {
				appendTooltip(tooltip, spawner.getSpawner().getOrCreateDisplayEntity(level, pos), name);
			} else if (accessor.getBlockEntity() instanceof TrialSpawnerBlockEntity spawner) {
				TrialSpawnerData data = spawner.getTrialSpawner().getData();
				appendTooltip(tooltip, data.getOrCreateDisplayEntity(spawner.getTrialSpawner(), level, spawner.getState()), name);
			}
		}
	}

	public static class ForEntity extends MobSpawnerProvider implements IEntityComponentProvider {
		private static final ForEntity INSTANCE = new ForEntity();

		@Override
		public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
			MinecartSpawner spawner = (MinecartSpawner) accessor.getEntity();
			MutableComponent name = ObjectNameProvider.getEntityName(spawner, false).copy();
			appendTooltip(
					tooltip,
					spawner.getSpawner().getOrCreateDisplayEntity(accessor.getLevel(), accessor.getEntity().blockPosition()),
					name);
		}
	}

	public static void appendTooltip(ITooltip tooltip, @Nullable Entity displayEntity, MutableComponent name) {
		if (displayEntity == null) {
			return;
		}
		name = Component.translatable("dainty.spawner", name, displayEntity.getDisplayName());
		tooltip.replace(JadeIds.CORE_OBJECT_NAME, IThemeHelper.get().title(name));
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_MOB_SPAWNER;
	}

	@Override
	public int getDefaultPriority() {
		return ObjectNameProvider.getEntity().getDefaultPriority() + 10;
	}

}
