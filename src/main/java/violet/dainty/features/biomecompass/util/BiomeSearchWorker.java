package violet.dainty.features.biomecompass.util;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.WorldWorkerManager;
import violet.dainty.Dainty;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;

public class BiomeSearchWorker implements WorldWorkerManager.IWorker {

	private final int sampleSpace;
	private final int maxSamples;
	private final int maxRadius;
	private ServerLevel level;
	private ResourceLocation biomeKey;
	private BlockPos startPos;
	private int samples;
	private int nextLength;
	private Direction direction;
	private ItemStack stack;
	private Player player;
	private int x;
	private int z;
	private int[] yValues;
	private int length;
	private boolean finished;
	private int lastRadiusThreshold;

	public BiomeSearchWorker(ServerLevel level, Player player, ItemStack stack, Biome biome, BlockPos startPos) {
		this.level = level;
		this.player = player;
		this.stack = stack;
		this.startPos = startPos;
		x = startPos.getX();
		z = startPos.getZ();
		yValues = Mth.outFromOrigin(startPos.getY(), level.getMinBuildHeight() + 1, level.getMaxBuildHeight(), 64).toArray();
		sampleSpace = 16 * BiomeUtils.getBiomeSize(level);
		maxSamples = 50000;
		maxRadius = 2500 * BiomeUtils.getBiomeSize(level);
		nextLength = sampleSpace;
		length = 0;
		samples = 0;
		direction = Direction.UP;
		finished = false;
		biomeKey = BiomeUtils.getKeyForBiome(level, biome).isPresent() ? BiomeUtils.getKeyForBiome(level, biome).get() : null;
		lastRadiusThreshold = 0;
	}

	public void start() {
		if (!stack.isEmpty() && stack.getItem() == BiomeCompass.naturesCompass) {
			if (maxRadius > 0 && sampleSpace > 0) {
				Dainty.LOGGER.info("Starting search: " + sampleSpace + " sample space, " + maxSamples + " max samples, " + maxRadius + " max radius");
				WorldWorkerManager.addWorker(this);
			} else {
				fail();
			}
		}
	}

	@Override
	public boolean hasWork() {
		return biomeKey != null && !finished && getRadius() <= maxRadius && samples <= maxSamples;
	}

	@Override
	public boolean doWork() {
		if (hasWork()) {
			if (direction == Direction.NORTH) {
				z -= sampleSpace;
			} else if (direction == Direction.EAST) {
				x += sampleSpace;
			} else if (direction == Direction.SOUTH) {
				z += sampleSpace;
			} else if (direction == Direction.WEST) {
				x -= sampleSpace;
			}
			
			int sampleX = QuartPos.fromBlock(x);
			int sampleZ = QuartPos.fromBlock(z);

			for (int y : yValues) {
				int sampleY = QuartPos.fromBlock(y);
				final Biome biomeAtPos = level.getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(sampleX, sampleY, sampleZ, level.getChunkSource().randomState().sampler()).value();
				final Optional<ResourceLocation> optionalBiomeAtPosKey = BiomeUtils.getKeyForBiome(level, biomeAtPos);
				if (optionalBiomeAtPosKey.isPresent() && optionalBiomeAtPosKey.get().equals(biomeKey)) {
					succeed();
					return false;
				}
			}

			samples++;
			length += sampleSpace;
			if (length >= nextLength) {
				if (direction != Direction.UP) {
					nextLength += sampleSpace;
					direction = direction.getClockWise();
				} else {
					direction = Direction.NORTH;
				}
				length = 0;
			}
			int radius = getRadius();
			if (radius > 500 && radius / 500 > lastRadiusThreshold) {
				if (!stack.isEmpty() && stack.getItem() == BiomeCompass.naturesCompass) {
					((NaturesCompassItem) stack.getItem()).setSearchRadius(stack, roundRadius(radius, 500), player);
				}
				lastRadiusThreshold = radius / 500;
			}
		}
		if (hasWork()) {
			return true;
		}
		if (!finished) {
			fail();
		}
		return false;
	}
	
	private void succeed() {
		Dainty.LOGGER.info("Search succeeded: " + getRadius() + " radius, " + samples + " samples");
		if (!stack.isEmpty() && stack.getItem() == BiomeCompass.naturesCompass) {
			((NaturesCompassItem) stack.getItem()).succeed(stack, player, x, z, samples, true);
		} else {
			Dainty.LOGGER.error("Invalid compass after search");
		}
		finished = true;
	}
	
	private void fail() {
		Dainty.LOGGER.info("Search failed: " + getRadius() + " radius, " + samples + " samples");
		if (!stack.isEmpty() && stack.getItem() == BiomeCompass.naturesCompass) {
			((NaturesCompassItem) stack.getItem()).fail(stack, player, roundRadius(getRadius(), 500), samples);
		} else {
			Dainty.LOGGER.error("Invalid compass after search");
		}
		finished = true;
	}
	
	public void stop() {
		Dainty.LOGGER.info("Search stopped: " + getRadius() + " radius, " + samples + " samples");
		finished = true;
	}

	private int getRadius() {
		return BiomeUtils.getDistanceToBiome(startPos, x, z);
	}
	
	private int roundRadius(int radius, int roundTo) {
		return ((int) radius / roundTo) * roundTo;
	}

}