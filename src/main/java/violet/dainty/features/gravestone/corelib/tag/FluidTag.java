package violet.dainty.features.gravestone.corelib.tag;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class FluidTag implements Tag<Fluid> {

    private final HolderSet.Named<Fluid> holderSet;

    public FluidTag(HolderSet.Named<Fluid> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public ResourceLocation getName() {
        return holderSet.key().location();
    }

	@Override
    @SuppressWarnings("deprecation")
    public boolean contains(Fluid block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public List<Fluid> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
