package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.fuel;

import com.google.common.base.Preconditions;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiFuelingRecipe;

import java.util.Collection;
import java.util.List;

public class FuelingRecipe implements IJeiFuelingRecipe {
	private final List<ItemStack> inputs;
	private final int burnTime;

	public FuelingRecipe(Collection<ItemStack> input, int burnTime) {
		Preconditions.checkArgument(burnTime > 0, "burn time must be greater than 0");
		this.inputs = List.copyOf(input);
		this.burnTime = burnTime;
	}

	@Override
	public List<ItemStack> getInputs() {
		return inputs;
	}

	@Override
	public int getBurnTime() {
		return burnTime;
	}
}
