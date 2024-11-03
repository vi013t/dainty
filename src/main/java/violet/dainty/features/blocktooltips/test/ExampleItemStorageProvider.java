package violet.dainty.features.blocktooltips.test;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.ui.MessageType;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.IClientExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ItemView;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;

public enum ExampleItemStorageProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return ExamplePlugin.UID_TEST_BREWING;
	}

	@Override
	public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
		return ClientViewGroup.map(groups, ItemView::new, (group, clientGroup) -> {
			clientGroup.title = Component.literal(group.id);
			clientGroup.messageType = MessageType.WARNING;
		});
	}

	@Override
	public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
		BrewingStandBlockEntity target = (BrewingStandBlockEntity) Objects.requireNonNull(accessor.getTarget());
		var potions = new ViewGroup<>(IntStream.of(0, 1, 2).mapToObj(target::getItem).filter($ -> !$.isEmpty()).toList());
		potions.id = "Potions";
		var ingredient = new ViewGroup<>(IntStream.of(3).mapToObj(target::getItem).filter($ -> !$.isEmpty()).toList());
		ingredient.id = "Ingredient";
		return List.of(ingredient, potions);
	}
}
