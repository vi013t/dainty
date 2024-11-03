package violet.dainty.features.blocktooltips.impl.config.entry;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.gui.config.OptionsList;
import violet.dainty.features.blocktooltips.gui.config.value.OptionValue;

public class StringConfigEntry extends ConfigEntry<String> {

	private Predicate<String> validator;

	public StringConfigEntry(ResourceLocation id, String defaultValue, Predicate<String> validator) {
		super(id, defaultValue);
		this.validator = validator;
	}

	@Override
	public boolean isValidValue(Object value) {
		return value.getClass() == String.class && validator.test((String) value);
	}

	@Override
	public OptionValue<?> createUI(OptionsList options, String optionName, BiConsumer<ResourceLocation, Object> setter) {
		return options.input(optionName, this::getValue, s -> setter.accept(id, s), validator);
	}

}
