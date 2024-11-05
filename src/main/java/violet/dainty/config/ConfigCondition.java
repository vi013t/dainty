package violet.dainty.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Implementation of {@link ICondition} for boolean config values. This is used in some data files, such as recipes, to conditionally
 * load assets based on configuration values; See {@link violet.dainty.config.DaintyConfig#CONDITION the registered condition} for more
 * information.
 * 
 * <br/><br/>
 * 
 * This class shouldn't really ever have to be referenced; It's automatically instantiated by Neoforge when the condition is used in a
 * data file. For example usage, see {@code /src/resources/dainty/data/recipe/bricks_to_terracotta.json}.
 */
public record ConfigCondition(String value) implements ICondition {

	/**
	 * The {@link Codec} for serializing a {@link ConfigCondition}. This simply serializes and deserializes
	 * the string value stored in {@link #value}. This is required because all custom conditions
	 * require a codec to be supplied; See <a href="https://docs.neoforged.net/docs/1.20.4/resources/server/conditional/#creating-custom-conditions">
	 * the corresponding part of the Neoforge documentation</a> for more information.
	 */
	public static final MapCodec<ConfigCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
		builder.group(
			Codec.STRING.fieldOf("value").forGetter(ConfigCondition::value)
		).apply(builder, ConfigCondition::new)
	);

	@Override
	public boolean test(IContext context) {
		return (boolean) DaintyConfig.getConfigValueFromName(this.value).get().get();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}
