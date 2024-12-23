package violet.dainty.features.recipeviewer.addons.resources.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CollectionHelper {
    public static List<ItemStack> create(ItemStack... itemStacks) {
        return new ArrayList<>(Arrays.asList(itemStacks));
    }

    public static List<String> create(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    public static List<Component> create(Function<String, Component> function, Stream<String> strings) {
        return strings.map(function).collect(Collectors.toList());
    }
}
