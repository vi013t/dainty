package violet.dainty.features.recipeviewer.addons.resources.common.profiling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;

public class ProfilingBlacklist {
    private static final String scanBlacklistName = "scan-blacklist.txt";

    public static File getScanBlacklistFile() {
        return Services.PLATFORM.getConfigDir().resolve(scanBlacklistName).toFile();
    }  

    private List<String> blacklist;

    public ProfilingBlacklist() {
        blacklist = new LinkedList<>();
        File scanBlacklistFile = getScanBlacklistFile();
        if (scanBlacklistFile.exists()) {
            try {
                blacklist = Files.readAllLines(scanBlacklistFile.toPath());
            } catch (IOException ignored) {

            }
        }
    }

    public boolean contains(BlockState blockState) {
        final String blockString = blockState.toString();
        return blacklist.stream().anyMatch((blockString::startsWith));
    }
}
