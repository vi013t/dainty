package violet.dainty.features.blocktooltips.test;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.IClientExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ProgressView;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;

public enum ExampleProgressProvider implements IServerExtensionProvider<CompoundTag>,
		IClientExtensionProvider<CompoundTag, ProgressView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return ExamplePlugin.UID_TEST_PROGRESS;
	}

	@Override
	public List<ClientViewGroup<ProgressView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
		return ClientViewGroup.map(groups, ProgressView::read, (group, clientGroup) -> {
			var view = clientGroup.views.getFirst();
			view.style.color(0xFFCC0000);
			view.text = Component.literal("Testtttttttttttttttttttttttttttttttt");

			view = clientGroup.views.get(1);
			view.style.color(0xFF00CC00);
			view.text = Component.literal("Test");
		});
	}

	@Override
	public List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
		Level world = accessor.getLevel();
		float period = 40;
		var progress1 = ProgressView.create(((world.getGameTime() % period) + 1) / period);
		period = 200;
		var progress2 = ProgressView.create(((world.getGameTime() % period) + 1) / period);
		var group = new ViewGroup<>(List.of(progress1, progress2));
		return List.of(group);
	}
}
