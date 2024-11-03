package violet.dainty.features.blocktooltips.test;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.ui.MessageType;
import violet.dainty.features.blocktooltips.api.view.ClientViewGroup;
import violet.dainty.features.blocktooltips.api.view.FluidView;
import violet.dainty.features.blocktooltips.api.view.IClientExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.IServerExtensionProvider;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;

public enum ExampleFluidStorageProvider
		implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, FluidView> {
	INSTANCE;

	@Override
	public ResourceLocation getUid() {
		return ExamplePlugin.UID_TEST_FLUIDS;
	}

	@Override
	public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
		return ClientViewGroup.map(groups, FluidView::readDefault, (group, clientGroup) -> {
			if (group.id != null) {
				clientGroup.title = Component.literal(group.id);
			}
			clientGroup.messageType = MessageType.SUCCESS;
		});
	}

	@Override
	public List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
		var tank1 = new ViewGroup<>(List.of(FluidView.writeDefault(JadeFluidObject.of(Fluids.LAVA, 1000), 2000)));
		tank1.id = "1";
		var tank2 = new ViewGroup<>(List.of(
				FluidView.writeDefault(JadeFluidObject.of(Fluids.WATER, 500), 2000),
				FluidView.writeDefault(JadeFluidObject.empty(), 2000)));
		// tank2.id = "2";
		return List.of(tank1, tank2, tank2, tank2, tank2);
	}
}
