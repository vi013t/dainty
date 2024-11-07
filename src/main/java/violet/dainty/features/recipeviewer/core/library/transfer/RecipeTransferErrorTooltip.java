package violet.dainty.features.recipeviewer.core.library.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;

public class RecipeTransferErrorTooltip implements IRecipeTransferError {
	private final List<Component> message = new ArrayList<>();

	public RecipeTransferErrorTooltip(Component message) {
		this.message.add(Component.translatable("dainty.tooltip.transfer"));
		MutableComponent messageTextComponent = message.copy();
		this.message.add(messageTextComponent.withStyle(ChatFormatting.RED));
	}

	@Override
	public Type getType() {
		return Type.USER_FACING;
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> getTooltip() {
		return Collections.unmodifiableList(message);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip) {
		tooltip.addAll(message);
	}
}
