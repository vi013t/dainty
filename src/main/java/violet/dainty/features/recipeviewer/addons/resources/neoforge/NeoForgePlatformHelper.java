package violet.dainty.features.recipeviewer.addons.resources.neoforge;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.objectweb.asm.Type;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.ModFileScanData;
import violet.dainty.features.recipeviewer.addons.resources.api.IJERAPI;
import violet.dainty.features.recipeviewer.addons.resources.api.IJERPlugin;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.ILootTableHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.IModList;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.IPlatformHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.proxy.CommonProxy;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LogHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public CommonProxy getProxy() {
        return JEResources.PROXY;
    }

    @Override
    public IModList getModsList() {
        return new violet.dainty.features.recipeviewer.addons.resources.neoforge.ModList(ModList.get());
    }

    @Override
    public void injectApi(IJERAPI instance) {
        Type pluginAnnotation = Type.getType(IJERPlugin.class); 
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), pluginAnnotation)) {
                    try {
                        Class<?> clazz = Class.forName(a.clazz().getClassName());
                        IJERPlugin plugin = (IJERPlugin) clazz.getDeclaredConstructor().newInstance();
                        plugin.receive(instance);
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException
                             | InstantiationException | InvocationTargetException e) {
                        LogHelper.warn("Failed to set: {}" + a.clazz().getClassName() + "." + a.memberName());
                    }
                }
            }
        }
    }

    @Override
    public boolean isCorrectToolForBlock(Block block, BlockState blockState, BlockGetter level, BlockPos blockPos, Player player) {
        return block.canHarvestBlock(blockState, level, blockPos, player);
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public ILootTableHelper getLootTableHelper() {
        return LootTableHelper.instance();
    }
}
