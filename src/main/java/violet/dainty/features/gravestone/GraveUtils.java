package violet.dainty.features.gravestone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import violet.dainty.features.gravestone.tileentity.GraveStoneTileEntity;

public class GraveUtils {

    public static final UUID EMPTY_UUID = new UUID(0L, 0L);

    @Nullable
    public static BlockPos getGraveStoneLocation(Level world, BlockPos pos) {
        BlockPos.MutableBlockPos location = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

        location.set(world.getWorldBorder().clampToBounds(location));

        if (world.isOutsideBuildHeight(location) && location.getY() <= world.getMinBuildHeight()) {
            location.set(location.getX(), world.getMinBuildHeight() + 1, location.getZ());
        }

        while (!world.isOutsideBuildHeight(location)) {
            if (isReplaceable(world, location)) {
                return location;
            }

            location.move(0, 1, 0);
        }

        return null;
    }

    public static boolean isReplaceable(Level world, BlockPos pos) {
        Block b = world.getBlockState(pos).getBlock();

        if (world.isEmptyBlock(pos)) {
            return true;
        }

        return GravestoneServerConfig.REPLACEABLE_BLOCKS_SPEC.stream().anyMatch(blockTag -> blockTag.contains(b));
    }

    @Nullable
    public static MutableComponent getDate(long timestamp) {
        if (timestamp <= 0L) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(Component.translatable("gui.gravestone.date_format").getString());
        return Component.literal(dateFormat.format(new Date(timestamp)));
    }

    public static boolean canBreakGrave(Level world, Player player, BlockPos pos) {
        if (player.isDeadOrDying()) {
            return false;
        }
        if (!GravestoneServerConfig.ONLY_OWNERS_CAN_BREAK) {
            return true;
        }

        BlockEntity te = world.getBlockEntity(pos);

        if (!(te instanceof GraveStoneTileEntity grave)) {
            return true;
        }

        if (player instanceof ServerPlayer p) {
            if (p.hasPermissions(p.server.getOperatorUserPermissionLevel())) {
                return true;
            }
        }
        UUID uuid = grave.getDeath().getPlayerUUID();
        if (uuid.equals(GraveUtils.EMPTY_UUID)) {
            return true;
        }

        return player.getUUID().equals(uuid);
    }

}
