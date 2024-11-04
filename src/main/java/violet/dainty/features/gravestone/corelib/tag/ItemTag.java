package violet.dainty.features.gravestone.corelib.tag;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemTag implements Tag<Item> {

    private final HolderSet.Named<Item> holderSet;

    public ItemTag(HolderSet.Named<Item> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public ResourceLocation getName() {
        return holderSet.key().location();
    }

	@Override
    @SuppressWarnings("deprecation")
    public boolean contains(Item block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public List<Item> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
