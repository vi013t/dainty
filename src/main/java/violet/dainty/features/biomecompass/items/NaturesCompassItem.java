package violet.dainty.features.biomecompass.items;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.biomecompass.gui.GuiWrapper;
import violet.dainty.features.biomecompass.network.SyncPacket;
import violet.dainty.features.biomecompass.util.BiomeSearchWorker;
import violet.dainty.features.biomecompass.util.BiomeUtils;
import violet.dainty.features.biomecompass.util.CompassState;
import violet.dainty.features.biomecompass.util.ItemUtils;
import violet.dainty.features.biomecompass.util.PlayerUtils;

public class NaturesCompassItem extends Item {

	public static final String NAME = "naturescompass";
	
	private BiomeSearchWorker worker;

	public NaturesCompassItem() {
		super(new Properties().stacksTo(1));
	}

	@SuppressWarnings("null")
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (!player.isCrouching()) {
			if (level.isClientSide()) {
				final ItemStack stack = ItemUtils.getHeldNatureCompass(player);
				GuiWrapper.openGUI(level, player, stack);
			} else {
				final ServerLevel serverLevel = (ServerLevel) level;
				final ServerPlayer serverPlayer = (ServerPlayer) player;
				final boolean canTeleport = PlayerUtils.canTeleport(serverPlayer.getServer(), player);
				final List<ResourceLocation> allowedBiomeKeys = BiomeUtils.getAllowedBiomeKeys(level);
				PacketDistributor.sendToPlayer(serverPlayer, new SyncPacket(canTeleport, allowedBiomeKeys, BiomeUtils.getGeneratingDimensionsForAllowedBiomes(serverLevel)));
			}
		} else {
			if (worker != null) {
				worker.stop();
				worker = null;
			}
			setState(player.getItemInHand(hand), null, CompassState.INACTIVE, player);
		}

		return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, player.getItemInHand(hand));
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		if (getState(oldStack) == getState(newStack)) {
			return false;
		}
		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}

	public void searchForBiome(ServerLevel level, Player player, ResourceLocation biomeKey, BlockPos pos, ItemStack stack) {
		setSearching(stack, biomeKey, player);
		Optional<Biome> optionalBiome = BiomeUtils.getBiomeForKey(level, biomeKey);
		if (optionalBiome.isPresent()) {
			if (worker != null) {
				worker.stop();
			}
			worker = new BiomeSearchWorker(level, player, stack, optionalBiome.get(), pos);
			worker.start();
		}
	}
	
	public void succeed(ItemStack stack, Player player, int x, int z, int samples, boolean displayCoordinates) {
		setFound(stack, x, z, samples, player);
		setDisplayCoordinates(stack, displayCoordinates);
		worker = null;
	}
	
	public void fail(ItemStack stack, Player player, int radius, int samples) {
		setNotFound(stack, player, radius, samples);
		worker = null;
	}

	public boolean isActive(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			return getState(stack) != CompassState.INACTIVE;
		}

		return false;
	}

	public void setSearching(ItemStack stack, ResourceLocation biomeKey, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.BIOME_ID, biomeKey.toString());
			stack.set(BiomeCompass.COMPASS_STATE, CompassState.SEARCHING.getID());
		}
	}

	public void setFound(ItemStack stack, int x, int z, int samples, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.COMPASS_STATE, CompassState.FOUND.getID());
			stack.set(BiomeCompass.FOUND_X, x);
			stack.set(BiomeCompass.FOUND_Z, z);
			stack.set(BiomeCompass.SAMPLES, samples);
		}
	}

	public void setNotFound(ItemStack stack, Player player, int searchRadius, int samples) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.COMPASS_STATE, CompassState.NOT_FOUND.getID());
			stack.set(BiomeCompass.SEARCH_RADIUS, searchRadius);
			stack.set(BiomeCompass.SAMPLES, samples);
		}
	}

	public void setInactive(ItemStack stack, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.COMPASS_STATE, CompassState.INACTIVE.getID());
		}
	}

	public void setState(ItemStack stack, BlockPos pos, CompassState state, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.COMPASS_STATE, state.getID());
		}
	}

	public void setFoundBiomeX(ItemStack stack, int x, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.FOUND_X, x);
		}
	}

	public void setFoundBiomeZ(ItemStack stack, int z, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.FOUND_Z, z);
		}
	}

	public void setBiomeKey(ItemStack stack, ResourceLocation biomeKey, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.BIOME_ID, biomeKey.toString());
		}
	}

	public void setSearchRadius(ItemStack stack, int searchRadius, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.SEARCH_RADIUS, searchRadius);
		}
	}

	public void setSamples(ItemStack stack, int samples, Player player) {
		if (stack.getItem() == BiomeCompass.naturesCompass) {
			stack.set(BiomeCompass.SAMPLES, samples);
		}
	}

	@SuppressWarnings("null")
	public CompassState getState(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.COMPASS_STATE)) {
			return CompassState.fromID(stack.get(BiomeCompass.COMPASS_STATE));
		}

		return null;
	}
	
	public void setDisplayCoordinates(ItemStack stack, boolean displayPosition) {
 		if (stack.getItem() == BiomeCompass.naturesCompass) {
 			stack.set(BiomeCompass.DISPLAY_COORDS, displayPosition);
 		}
 	}

	@SuppressWarnings("null")
	public int getFoundBiomeX(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.FOUND_X)) {
			return stack.get(BiomeCompass.FOUND_X);
		}

		return 0;
	}

	@SuppressWarnings("null")
	public int getFoundBiomeZ(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.FOUND_Z)) {
			return stack.get(BiomeCompass.FOUND_Z);
		}

		return 0;
	}

	@SuppressWarnings("null")
	public ResourceLocation getBiomeKey(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.BIOME_ID)) {
			return ResourceLocation.parse(stack.get(BiomeCompass.BIOME_ID));
		}

		return ResourceLocation.fromNamespaceAndPath("", "");
	}

	@SuppressWarnings("null")
	public int getSearchRadius(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.SEARCH_RADIUS)) {
			return stack.get(BiomeCompass.SEARCH_RADIUS);
		}

		return -1;
	}

	@SuppressWarnings("null")
	public int getSamples(ItemStack stack) {
		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.SAMPLES)) {
			return stack.get(BiomeCompass.SAMPLES);
		}

		return -1;
	}

	public int getDistanceToBiome(Player player, ItemStack stack) {
		return BiomeUtils.getDistanceToBiome(player, getFoundBiomeX(stack), getFoundBiomeZ(stack));
	}
	
	@SuppressWarnings("null")
	public boolean shouldDisplayCoordinates(ItemStack stack) {
 		if (stack.getItem() == BiomeCompass.naturesCompass && stack.has(BiomeCompass.DISPLAY_COORDS)) {
 			return stack.get(BiomeCompass.DISPLAY_COORDS);
 		}

 		return true;
 	}

}
