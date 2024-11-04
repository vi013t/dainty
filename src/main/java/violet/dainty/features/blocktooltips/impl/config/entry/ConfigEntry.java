package violet.dainty.features.blocktooltips.impl.config.entry;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.gui.config.OptionsList;
import violet.dainty.features.blocktooltips.gui.config.value.OptionValue;

public abstract class ConfigEntry<T> {

	public final ResourceLocation id;
	private final T defaultValue;
	private boolean synced;
	protected T value;
	private List<Consumer<ResourceLocation>> listeners = List.of();

	public ConfigEntry(ResourceLocation id, T defaultValue) {
		this.id = id;
		value = this.defaultValue = defaultValue;
	}

	public ResourceLocation getId() {
		return id;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		if (!Objects.equals(this.value, value)) {
			this.value = (T) value;
			notifyChange();
		}
	}

	abstract public boolean isValidValue(Object value);

	abstract public OptionValue<?> createUI(OptionsList options, String optionName, BiConsumer<ResourceLocation, Object> setter);

	public void addListener(Consumer<ResourceLocation> listener) {
		if (listeners.isEmpty()) {
			listeners = Lists.newArrayList();
		}
		listeners.add(listener);
	}

	public void notifyChange() {
		for (Consumer<ResourceLocation> listener : listeners) {
			listener.accept(id);
		}
	}
}
