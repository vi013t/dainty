package violet.dainty.features.recipeviewer.addons.resources.neoforge.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Reference;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LogHelper;

public class Config {
    public static Config instance = new Config();

    private Config() {

    }

    public static final ModConfigSpec COMMON = ConfigValues.build();

    public void loadConfig(ModConfigSpec spec, Path path) {
        LogHelper.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();

        LogHelper.debug("Built TOML config for {}", path.toString());
        configData.load();
        LogHelper.debug("Loaded TOML config file {}", path.toString());
        spec.correct(configData);
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onLoad(final ModConfigEvent.Loading configEvent) {
        LogHelper.debug("Loaded {} config file {}", Reference.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onFileChange(final ModConfigEvent.Reloading configEvent) {
        LogHelper.debug("Reloaded {} config file {}", Reference.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }
}
