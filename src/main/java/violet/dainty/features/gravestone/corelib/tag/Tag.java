package violet.dainty.features.gravestone.corelib.tag;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface Tag<T> {

    ResourceLocation getName();

    boolean contains(T t);

    List<T> getAll();

}
