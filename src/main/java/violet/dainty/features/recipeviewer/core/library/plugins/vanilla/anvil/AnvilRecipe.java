package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiAnvilRecipe;

import org.jetbrains.annotations.Nullable;

public record AnvilRecipe(
	List<ItemStack> leftInputs,
	List<ItemStack> rightInputs,
	List<ItemStack> outputs,
	@Nullable ResourceLocation uid
) implements IJeiAnvilRecipe {

	@Override
	public List<ItemStack> getLeftInputs() {
		return leftInputs;
	}

	@Override
	public List<ItemStack> getRightInputs() {
		return rightInputs;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	@Nullable
	public ResourceLocation getUid() {
		return uid;
	}
}
