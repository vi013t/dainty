package violet.dainty.features.blocktooltips.addon.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IWailaClientRegistration;
import violet.dainty.features.blocktooltips.api.IWailaCommonRegistration;
import violet.dainty.features.blocktooltips.api.IWailaPlugin;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.WailaPlugin;
import violet.dainty.features.blocktooltips.impl.BlockAccessorClientHandler;
import violet.dainty.features.blocktooltips.impl.EntityAccessorClientHandler;

@WailaPlugin
public class CorePlugin implements IWailaPlugin {

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(ObjectNameProvider.getBlock(), BlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerAccessorHandler(BlockAccessor.class, new BlockAccessorClientHandler());
		registration.registerAccessorHandler(EntityAccessor.class, new EntityAccessorClientHandler());

		registration.addConfig(JadeIds.CORE_DISTANCE, false);
		registration.addConfig(JadeIds.CORE_COORDINATES, false);
		registration.addConfig(JadeIds.CORE_REL_COORDINATES, false);

		registration.registerBlockComponent(ObjectNameProvider.getBlock(), Block.class);
		registration.registerBlockComponent(ModNameProvider.getBlock(), Block.class);
		registration.registerBlockComponent(DistanceProvider.getBlock(), Block.class);
		registration.registerBlockComponent(BlockFaceProvider.INSTANCE, Block.class);

		registration.registerEntityComponent(ObjectNameProvider.getEntity(), Entity.class);
		registration.registerEntityComponent(ModNameProvider.getEntity(), Entity.class);
		registration.registerEntityComponent(DistanceProvider.getEntity(), Entity.class);

		registration.markAsClientFeature(JadeIds.CORE_DISTANCE);
		registration.markAsClientFeature(JadeIds.CORE_COORDINATES);
		registration.markAsClientFeature(JadeIds.CORE_REL_COORDINATES);
		registration.markAsClientFeature(JadeIds.CORE_MOD_NAME);
		registration.markAsClientFeature(JadeIds.CORE_BLOCK_FACE);
	}
}
