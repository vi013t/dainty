package violet.dainty.features.blocktooltips.impl.template;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;

/**
 * A template implementation for script languages like KubeJS
 */
public class TemplateServerDataProvider<T extends Accessor<?>> implements IServerDataProvider<T> {
	private final ResourceLocation uid;
	private BiConsumer<CompoundTag, T> dataFunction = (data, accessor) -> {};
	private Predicate<T> shouldRequestData = accessor -> true;

	protected TemplateServerDataProvider(ResourceLocation uid) {
		this.uid = uid;
	}

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	@Override
	public void appendServerData(CompoundTag data, T accessor) {
		dataFunction.accept(data, accessor);
	}

	@Override
	public boolean shouldRequestData(T accessor) {
		return shouldRequestData.test(accessor);
	}

	public void setDataFunction(BiConsumer<CompoundTag, T> dataFunction) {
		this.dataFunction = dataFunction;
	}

	public void setShouldRequestData(Predicate<T> shouldRequestData) {
		this.shouldRequestData = shouldRequestData;
	}
}
