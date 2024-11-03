package violet.dainty.features.gravestone.corelib.energy;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyUtils {

    /**
     * Pushes energy between two energy storages
     *
     * @param provider  the energy provider
     * @param receiver  the energy receiver
     * @param maxAmount the maximum amount to push
     * @return the amount that actually got transferred
     */
    public static int pushEnergy(IEnergyStorage provider, IEnergyStorage receiver, int maxAmount) {
        int energySim = provider.extractEnergy(maxAmount, true);

        int receivedSim = receiver.receiveEnergy(energySim, true);

        int energy = provider.extractEnergy(receivedSim, false);

        receiver.receiveEnergy(energy, false);
        return energy;
    }

    /**
     * Gets the energy storage at the specified position and direction
     *
     * @param world the world
     * @param pos   the position
     * @param side  the side
     * @return the energy storage
     */
    @Nullable
    public static IEnergyStorage getEnergyStorage(Level world, BlockPos pos, Direction side) {
        return world.getCapability(Capabilities.EnergyStorage.BLOCK, pos, side.getOpposite());
    }

    /**
     * Gets the energy storage at the specified position offset one block to the specified direction
     *
     * @param world the world
     * @param pos   the position
     * @param side  the side
     * @return the energy storage
     */
    @Nullable
    public static IEnergyStorage getEnergyStorageOffset(Level world, BlockPos pos, Direction side) {
        return getEnergyStorage(world, pos.relative(side), side.getOpposite());
    }

}
