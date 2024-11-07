package violet.dainty.features.recipeviewer.core.gui.ingredients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import violet.dainty.features.recipeviewer.core.common.util.StringUtil;
import violet.dainty.features.recipeviewer.core.common.util.Translator;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public class ListElementInfoTooltip implements ITooltipBuilder {
	private final List<FormattedText> tooltip = new ArrayList<>();

	@Override
	public void add(FormattedText component) {
		tooltip.add(component);
	}

	@Override
	public void addAll(Collection<? extends FormattedText> components) {
		tooltip.addAll(components);
	}

	@Override
	public void add(TooltipComponent component) {
		// ignored for the purposes of searching tooltips
	}

	@Override
	public void setIngredient(ITypedIngredient<?> typedIngredient) {
		// ignored for the purposes of searching tooltips
	}

	@Override
	public void clear() {
		tooltip.clear();
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> toLegacyToComponents() {
		return tooltip.stream()
			.<Component>mapMulti((f, consumer) -> {
				if (f instanceof Component c) {
					consumer.accept(c);
				}
			})
			.collect(Collectors.toCollection(ArrayList::new));
	}

	@SuppressWarnings("removal")
	@Override
	public void removeAll(List<Component> components) {
		for (Component component : components) {
			tooltip.remove(component);
		}
	}

	public Set<String> getStrings() {
		Set<String> result = new HashSet<>();
		for (FormattedText component : tooltip) {
			String string = component.getString();
			string = StringUtil.removeChatFormatting(string);
			string = Translator.toLowercaseWithLocale(string);
			// Split tooltip strings into words to keep them from being too long.
			// Longer strings are more expensive for the suffix tree to handle.
			String[] strings = string.split(" ");
			Collections.addAll(result, strings);
		}
		return result;
	}
}
