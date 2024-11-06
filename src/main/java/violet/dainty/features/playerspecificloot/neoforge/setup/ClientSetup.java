package violet.dainty.features.playerspecificloot.neoforge.setup;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrChestBlockEntity;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrShulkerBlockEntity;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrTrappedChestBlockEntity;
import violet.dainty.features.playerspecificloot.common.client.block.LootrChestBlockRenderer;
import violet.dainty.features.playerspecificloot.common.client.block.LootrShulkerBlockRenderer;
import violet.dainty.features.playerspecificloot.common.client.entity.LootrChestCartRenderer;
import violet.dainty.features.playerspecificloot.common.client.item.LootrChestItemRenderer;
import violet.dainty.features.playerspecificloot.common.client.item.LootrShulkerItemRenderer;
import violet.dainty.features.playerspecificloot.common.entity.LootrChestMinecartEntity;
import violet.dainty.features.playerspecificloot.neoforge.client.block.BarrelModel;

@EventBusSubscriber(modid = Dainty.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

  @SubscribeEvent 
  public static void modelRegister(ModelEvent.RegisterGeometryLoaders event) {
    event.register(LootrAPI.rl("barrel"), BarrelModel.Loader.INSTANCE);
  }

  @SuppressWarnings("unchecked")
  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer((BlockEntityType<LootrChestBlockEntity>) LootrRegistry.getChestBlockEntity(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer((BlockEntityType<LootrTrappedChestBlockEntity>) LootrRegistry.getTrappedChestBlockEntity(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer((BlockEntityType<LootrChestBlockEntity>) LootrRegistry.getChestBlockEntity(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer((BlockEntityType<LootrShulkerBlockEntity>) LootrRegistry.getShulkerBlockEntity(), LootrShulkerBlockRenderer::new);
    event.registerEntityRenderer((EntityType<LootrChestMinecartEntity>) LootrRegistry.getMinecart(), (e) -> new LootrChestCartRenderer<>(e, ModelLayers.CHEST_MINECART));
  }

  @SubscribeEvent
  public static void registerClientExtensions (RegisterClientExtensionsEvent event) {
    event.registerItem(new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return LootrChestItemRenderer.getInstance();
      }
    }, LootrRegistry.getChestItem());
    event.registerItem(new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return LootrShulkerItemRenderer.getInstance();
      }
    }, LootrRegistry.getShulkerItem());
  }

}
