package violet.dainty.features.recipeviewer.addons.resources.common.platform;

import java.nio.file.Path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.recipeviewer.addons.resources.api.IJERAPI;
import violet.dainty.features.recipeviewer.addons.resources.common.proxy.CommonProxy;

public interface IPlatformHelper {
    String getPlatformName();

    boolean isClient();

    CommonProxy getProxy();

    IModList getModsList();

    void injectApi(IJERAPI instance);

    boolean isCorrectToolForBlock(Block block, BlockState blockState, BlockGetter level, BlockPos blockPos, Player player);

    Path getConfigDir();

    ILootTableHelper getLootTableHelper();
}
