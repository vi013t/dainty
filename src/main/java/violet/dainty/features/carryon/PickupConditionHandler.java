package violet.dainty.features.carryon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class PickupConditionHandler {
	private static final List<PickupCondition> BLOCK_CONDITIONS = new ArrayList<>();
	private static final List<PickupCondition> ENTITY_CONDITIONS = new ArrayList<>();

	public static void initPickupConditions() {
		BLOCK_CONDITIONS.clear();
		ENTITY_CONDITIONS.clear();
	}

	public static Optional<PickupCondition> getPickupCondition(BlockState state) {
		for (PickupCondition cond : BLOCK_CONDITIONS) {
			if (cond.matches(state)) return Optional.of(cond);
		}
		return Optional.empty();
	}

	public static Optional<PickupCondition> getPickupCondition(Entity entity) {
		for (PickupCondition cond : ENTITY_CONDITIONS) {
			if (cond.matches(entity)) return Optional.of(cond);
		}
		return Optional.empty();
	}
}