package violet.dainty.features.carryon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ScriptManager {
	public static final List<CarryOnScript> SCRIPTS = new ArrayList<>();

	public static Optional<CarryOnScript> inspectBlock(BlockState state, Level level, BlockPos pos, @Nullable CompoundTag tag) {
		return Optional.empty();
	}

	public static Optional<CarryOnScript> inspectEntity(Entity entity) {
		return Optional.empty();
	}

}