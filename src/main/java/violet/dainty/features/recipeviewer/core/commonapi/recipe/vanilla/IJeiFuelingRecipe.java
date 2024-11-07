package violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnegative;
import java.util.List;

/**
 * Fueling recipes represent items that can be used as fuel in the Furnace, Smoker, Blast Furnace, etc.
 *
 * JEI automatically creates a fueling recipe for anything that has a burn time.
 *
 * @since 9.5.0
 */
public interface IJeiFuelingRecipe {
	/**
	 * @return the inputs that act as a fuel
	 */
	@Unmodifiable
	List<ItemStack> getInputs();

	/**
	 * @return the fuel's burn time in ticks. Always greater than 0.
	 */
	@Nonnegative
	int getBurnTime();
}
