package violet.dainty.mixins.playerspecificloot;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

@Mixin(DimensionDataStorage.class)
public interface MixinDimensionDataStorage {
  @Accessor
  Map<String, SavedData> getCache();
}
