package violet.dainty.features.veinmine;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.Dainty;

public record VeinMineSettings(Shape shape, int maxBlocks, boolean sameBlocks) implements CustomPacketPayload {

	public static final Codec<VeinMineSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("shape").forGetter(VeinMineSettings::shapeId),
		Codec.INT.fieldOf("maxBlocks").forGetter(VeinMineSettings::maxBlocks),
		Codec.BOOL.fieldOf("sameBlocks").forGetter(VeinMineSettings::sameBlocks)
	).apply(instance, VeinMineSettings::fromShapeId));

	public static final StreamCodec<ByteBuf, VeinMineSettings> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, VeinMineSettings::shapeId,
		ByteBufCodecs.INT, VeinMineSettings::maxBlocks,
		ByteBufCodecs.BOOL, VeinMineSettings::sameBlocks,
		VeinMineSettings::fromShapeId
	);

	public static final VeinMineSettings DEFAULT = new VeinMineSettings(Shape.TUNNEL_3x3, 64, true);

	public int shapeId() {
		return this.shape().ordinal();
	}

	private static VeinMineSettings fromShapeId(int shapeId, int maxBlocks, boolean sameBlocks) {
		return new VeinMineSettings(Shape.values()[shapeId], maxBlocks, sameBlocks);
	}

	/**
	 * Gets a list of blocks to vein mine based on these vein mine settings, using the given source block (the block the player actually
	 * broke) and direction (the direction the player was facing when the block was broken). This logic is used and handled in
	 * {@link violet.dainty.features.veinmine.VeinMineEventHandler#veinMine(net.neoforged.neoforge.event.level.BlockEvent.BreakEvent)
	 * the corresponding part of the vein mine event handler}.
	 *
	 * @param level The level the block was broken in
	 * @param source The block the player broke while holding the vein-mine key
	 * @param direction The direction the player was facing while holding the vein-mine key
	 * 
	 * @return The blocks to be affected by the vein mine.
	 */
	public BlockPos[] carve(Level level, BlockPos source, Direction direction) {
		switch(this.shape()) {
			case TUNNEL_1x1: {
				List<BlockPos> positions = new ArrayList<>();
				BlockState blockBroken = level.getBlockState(source);
				for (int depth = 0; depth < this.maxBlocks; depth++) {
					BlockPos position = source.relative(direction, depth);
					BlockState blockAtPosition = level.getBlockState(position);
					if (!this.sameBlocks || blockAtPosition == blockBroken) positions.add(position);
				}
				return positions.toArray(BlockPos[]::new);
			}
			case TUNNEL_3x3: {
				List<BlockPos> positions = new ArrayList<>();
				BlockState blockBroken = level.getBlockState(source);
				for (int depth = 0; depth < this.maxBlocks / 9; depth++) {
					// Middle block
					BlockPos position = source.relative(direction, depth);
					BlockState blockAtPosition = level.getBlockState(position);
					if (!this.sameBlocks || blockAtPosition == blockBroken) positions.add(position);

					// Around blocks
					int normalIndex = direction.getAxis().ordinal();
					for (int xOffset = -1; xOffset <= 1; xOffset++) {
						for (int yOffset = -1; yOffset <= 1; yOffset++) {
							for (int zOffset = -1; zOffset <= 1; zOffset++) {
								if ((new int[] { xOffset, yOffset, zOffset })[normalIndex] != 0) continue;
								BlockPos aroundPosition = source.relative(direction, depth).offset(new Vec3i(xOffset, yOffset, zOffset));
								BlockState blockAtAroundPosition = level.getBlockState(aroundPosition);
								if (!this.sameBlocks || blockAtAroundPosition == blockBroken) positions.add(aroundPosition);
							}
						}
					}
				}
				return positions.toArray(BlockPos[]::new);
			}
			case CUBE: {
				List<BlockPos> positions = new ArrayList<>();
				BlockState blockBroken = level.getBlockState(source);
				int sideLength = (int) Math.cbrt(this.maxBlocks);
				for (int depth = 0; depth < sideLength; depth++) {
					// Middle block
					BlockPos position = source.relative(direction, depth);
					BlockState blockAtPosition = level.getBlockState(position);
					if (!this.sameBlocks || blockAtPosition == blockBroken) positions.add(position);

					// Around blocks
					int normalIndex = direction.getAxis().ordinal();
					for (int xOffset = -sideLength / 2; xOffset <= sideLength / 2; xOffset++) {
						for (int yOffset = -sideLength / 2; yOffset <= sideLength / 2; yOffset++) {
							for (int zOffset = -sideLength / 2 ; zOffset <= sideLength / 2; zOffset++) {
								if ((new int[] { xOffset, yOffset, zOffset })[normalIndex] != 0) continue;
								BlockPos aroundPosition = source.relative(direction, depth).offset(new Vec3i(xOffset, yOffset, zOffset));
								BlockState blockAtAroundPosition = level.getBlockState(aroundPosition);
								if (!this.sameBlocks || blockAtAroundPosition == blockBroken) positions.add(aroundPosition);
							}
						}
					}
				}
				return positions.toArray(BlockPos[]::new);
			}
			default: throw new IllegalStateException("Unrecognized vein mine shape: " + this.shape());
		}
	}

	public VeinMineSettings changeShape(int delta) {
		return new VeinMineSettings(Shape.values()[(Shape.values().length + this.shape().ordinal() - delta) % Shape.values().length], this.maxBlocks, this.sameBlocks);
	}

	enum Shape {
		TUNNEL_1x1,
		TUNNEL_3x3,
		PLAYER_TUNNEL,
		STAIRCASE_DOWN,
		STAIRCASE_UP,
		CUBE;

		@Override
		public String toString() {
			return switch(this) {
				case TUNNEL_1x1 -> "1x1 Tunnel";
				case TUNNEL_3x3 -> "3x3 Tunnel";
				case CUBE -> "Cube";
				case PLAYER_TUNNEL -> "Player Tunnel";
				case STAIRCASE_DOWN -> "Staircase Down";
				case STAIRCASE_UP -> "Staircase Up";
			};
		}
	}

	public static final CustomPacketPayload.Type<VeinMineSettings> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "veinminesettings"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
