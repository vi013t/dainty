package violet.dainty.features.carryon;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import violet.dainty.features.carryon.CarryOnScript.ScriptObject.ScriptObjectBlock;
import violet.dainty.features.carryon.CarryOnScript.ScriptObject.ScriptObjectEntity;
import violet.dainty.features.carryon.Matchables.AdvancementCondition;
import violet.dainty.features.carryon.Matchables.EffectsCondition;
import violet.dainty.features.carryon.Matchables.GamestageCondition;
import violet.dainty.features.carryon.Matchables.NBTCondition;
import violet.dainty.features.carryon.Matchables.NumberBoundCondition;
import violet.dainty.features.carryon.Matchables.OptionalVec3;
import violet.dainty.features.carryon.Matchables.PositionCondition;
import violet.dainty.features.carryon.Matchables.ScoreboardCondition;

public record CarryOnScript(long priority, ScriptObject scriptObject, ScriptConditions scriptConditions, ScriptRender scriptRender, ScriptEffects scriptEffects) {

	public boolean isValid() {
		return (isBlock() ^ isEntity()) && (scriptConditions != ScriptConditions.EMPTY || scriptRender != ScriptRender.EMPTY || scriptEffects != ScriptEffects.EMPTY);
	}

	public boolean isBlock() {
		return scriptObject.block() != ScriptObjectBlock.EMPTY;
	}

	public boolean isEntity() {
		return scriptObject.entity() != ScriptObjectEntity.EMPTY;
	}

	public static final Codec<CarryOnScript> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.LONG.optionalFieldOf("priority", 0L).forGetter(CarryOnScript::priority),
			ScriptObject.CODEC.fieldOf("object").forGetter(CarryOnScript::scriptObject),
			ScriptConditions.CODEC.optionalFieldOf("conditions", ScriptConditions.EMPTY).forGetter(CarryOnScript::scriptConditions),
			ScriptRender.CODEC.optionalFieldOf("render", ScriptRender.EMPTY).forGetter(CarryOnScript::scriptRender),
			ScriptEffects.CODEC.optionalFieldOf("effects", ScriptEffects.EMPTY).forGetter(CarryOnScript::scriptEffects)
		).apply(instance, CarryOnScript::new)
	);

	public boolean fulfillsConditions(ServerPlayer player) {
		ScriptConditions cond = this.scriptConditions();

		boolean achievement = cond.conditionAchievement.matches(player);
		boolean gamemode = cond.conditionGamemode.matches(player.gameMode.getGameModeForPlayer().getId());
		boolean gamestage = cond.conditionGamestage.matches(player);
		boolean position = cond.conditionPosition.matches(player);
		boolean xp = cond.conditionXp.matches(player.experienceLevel);
		boolean scoreboard = cond.conditionScoreboard.matches(player);
		boolean effects = cond.conditionEffects.matches(player);

		return achievement && gamemode && gamestage && position && xp && scoreboard && effects;
	}

	public record ScriptObject(ScriptObjectBlock block, ScriptObjectEntity entity) {
		public static final Codec<ScriptObject> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				ScriptObjectBlock.CODEC.optionalFieldOf("block", ScriptObjectBlock.EMPTY).forGetter(ScriptObject::block),
				ScriptObjectEntity.CODEC.optionalFieldOf("entity", ScriptObjectEntity.EMPTY).forGetter(ScriptObject::entity)
			).apply(instance, ScriptObject::new)
		);

		public record ScriptObjectBlock(Optional<ResourceKey<Block>> typeNameBlock, NumberBoundCondition typeHardness, NumberBoundCondition typeResistance, NBTCondition typeBlockTag) {

			public static final ScriptObjectBlock EMPTY = new ScriptObjectBlock(Optional.empty(), NumberBoundCondition.NONE, NumberBoundCondition.NONE, NBTCondition.NONE);

			public static final Codec<ScriptObjectBlock> CODEC = RecordCodecBuilder.create(instance ->
				instance.group( 
					ResourceKey.codec(Registries.BLOCK).optionalFieldOf("name").forGetter(ScriptObjectBlock::typeNameBlock),
					NumberBoundCondition.CODEC.optionalFieldOf("hardness", NumberBoundCondition.NONE).forGetter(ScriptObjectBlock::typeHardness),
					NumberBoundCondition.CODEC.optionalFieldOf("resistance", NumberBoundCondition.NONE).forGetter(ScriptObjectBlock::typeResistance),
					NBTCondition.CODEC.optionalFieldOf("nbt", NBTCondition.NONE).forGetter(ScriptObjectBlock::typeBlockTag)
				).apply(instance, ScriptObjectBlock::new)
			);
		}

		public record ScriptObjectEntity(Optional<ResourceKey<EntityType<?>>> typeNameEntity, NumberBoundCondition typeHealth, NumberBoundCondition typeHeight, NumberBoundCondition typeWidth, NBTCondition typeEntityTag) {

			public static final ScriptObjectEntity EMPTY = new ScriptObjectEntity(Optional.empty(), NumberBoundCondition.NONE, NumberBoundCondition.NONE, NumberBoundCondition.NONE, NBTCondition.NONE);

			public static final Codec<ScriptObjectEntity> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
					ResourceKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("name").forGetter(ScriptObjectEntity::typeNameEntity),
					NumberBoundCondition.CODEC.optionalFieldOf("health", NumberBoundCondition.NONE).forGetter(ScriptObjectEntity::typeHealth),
					NumberBoundCondition.CODEC.optionalFieldOf("height", NumberBoundCondition.NONE).forGetter(ScriptObjectEntity::typeHeight),
					NumberBoundCondition.CODEC.optionalFieldOf("width", NumberBoundCondition.NONE).forGetter(ScriptObjectEntity::typeWidth),
					NBTCondition.CODEC.optionalFieldOf("nbt", NBTCondition.NONE).forGetter(ScriptObjectEntity::typeEntityTag)
				).apply(instance, ScriptObjectEntity::new)
			);
		}
	}

	public record ScriptConditions(
			GamestageCondition conditionGamestage,
			AdvancementCondition conditionAchievement,
			NumberBoundCondition conditionXp,
			NumberBoundCondition conditionGamemode,
			ScoreboardCondition conditionScoreboard,
			PositionCondition conditionPosition,
			EffectsCondition conditionEffects
	){
		public static final ScriptConditions EMPTY = new ScriptConditions(GamestageCondition.NONE, AdvancementCondition.NONE, NumberBoundCondition.NONE, NumberBoundCondition.NONE, ScoreboardCondition.NONE, PositionCondition.NONE, EffectsCondition.NONE);

		public static final Codec<ScriptConditions> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				GamestageCondition.CODEC.optionalFieldOf("gamestage", GamestageCondition.NONE).forGetter(ScriptConditions::conditionGamestage),
				AdvancementCondition.CODEC.optionalFieldOf("advancement", AdvancementCondition.NONE).forGetter(ScriptConditions::conditionAchievement),
				NumberBoundCondition.CODEC.optionalFieldOf("xp", NumberBoundCondition.NONE).forGetter(ScriptConditions::conditionXp),
				NumberBoundCondition.CODEC.optionalFieldOf("gamemode", NumberBoundCondition.NONE).forGetter(ScriptConditions::conditionGamemode),
				ScoreboardCondition.CODEC.optionalFieldOf("scoreboard", ScoreboardCondition.NONE).forGetter(ScriptConditions::conditionScoreboard),
				PositionCondition.CODEC.optionalFieldOf("position", PositionCondition.NONE).forGetter(ScriptConditions::conditionPosition),
				EffectsCondition.CODEC.optionalFieldOf("effects", EffectsCondition.NONE).forGetter(ScriptConditions::conditionEffects)
			).apply(instance, ScriptConditions::new)
		);
	}

	public record ScriptRender(
		Optional<ResourceKey<Block>> renderNameBlock,
		Optional<ResourceKey<EntityType<?>>> renderNameEntity,
		Optional<CompoundTag> renderNBT,
		OptionalVec3 renderTranslation,
		OptionalVec3 renderRotation,
		OptionalVec3 renderScale,
		OptionalVec3 renderRotationLeftArm,
		OptionalVec3 renderRotationRightArm,
		boolean renderLeftArm,
		boolean renderRightArm
	){
		public static final ScriptRender EMPTY = new ScriptRender(Optional.empty(), Optional.empty(), Optional.empty(), OptionalVec3.NONE, OptionalVec3.NONE, OptionalVec3.NONE, OptionalVec3.NONE, OptionalVec3.NONE, true, true);

		public static final Codec<ScriptRender> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				ResourceKey.codec(Registries.BLOCK).optionalFieldOf("name_block").forGetter(ScriptRender::renderNameBlock),
				ResourceKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("name_entity").forGetter(ScriptRender::renderNameEntity),
				CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(ScriptRender::renderNBT),
				OptionalVec3.CODEC.optionalFieldOf("translation", OptionalVec3.NONE).forGetter(ScriptRender::renderTranslation),
				OptionalVec3.CODEC.optionalFieldOf("rotation", OptionalVec3.NONE).forGetter(ScriptRender::renderRotation),
				OptionalVec3.CODEC.optionalFieldOf("scale", OptionalVec3.NONE).forGetter(ScriptRender::renderScale),
				OptionalVec3.CODEC.optionalFieldOf("rotation_left_arm", OptionalVec3.NONE).forGetter(ScriptRender::renderRotationLeftArm),
				OptionalVec3.CODEC.optionalFieldOf("rotation_right_arm", OptionalVec3.NONE).forGetter(ScriptRender::renderRotationRightArm),
				Codec.BOOL.optionalFieldOf("render_left_arm", true).forGetter(ScriptRender::renderLeftArm),
				Codec.BOOL.optionalFieldOf("render_right_arm", true).forGetter(ScriptRender::renderRightArm)
			).apply(instance, ScriptRender::new)
		);
	}

	public record ScriptEffects(String commandInit, String commandLoop, String commandPlace){
		public static final ScriptEffects EMPTY = new ScriptEffects("", "", "");

		public static final Codec<ScriptEffects> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.STRING.optionalFieldOf("commandPickup", "").forGetter(ScriptEffects::commandInit),
				Codec.STRING.optionalFieldOf("commandLoop", "").forGetter(ScriptEffects::commandLoop),
				Codec.STRING.optionalFieldOf("commandPlace", "").forGetter(ScriptEffects::commandPlace)
			).apply(instance, ScriptEffects::new)
		);
	}
}
