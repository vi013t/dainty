package violet.dainty.features.playerspecificloot.common.client.item;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrChestBlockEntity;
import violet.dainty.features.playerspecificloot.common.entity.LootrChestMinecartEntity;

public class LootrChestItemRenderer extends BlockEntityWithoutLevelRenderer {
  private static LootrChestItemRenderer INSTANCE = null;

  private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
  private final LootrChestBlockEntity blockEntity;

  public LootrChestItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
    super(pBlockEntityRenderDispatcher, pEntityModelSet);
    this.blockEntityRenderDispatcher = pBlockEntityRenderDispatcher;
    this.blockEntity = new LootrChestBlockEntity(BlockPos.ZERO, LootrRegistry.getChestBlock().defaultBlockState());
  }

  public LootrChestItemRenderer() {
    this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
  }

  public static LootrChestItemRenderer getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new LootrChestItemRenderer();
    }

    return INSTANCE;
  }

  @Override
  public void renderByItem(@Nonnull ItemStack p_108830_, @Nonnull ItemDisplayContext p_270899_, @Nonnull PoseStack p_108832_, @Nonnull MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
    this.blockEntityRenderDispatcher.renderItem(blockEntity, p_108832_, p_108833_, p_108834_, p_108835_);
  }

  public void renderByMinecart(LootrChestMinecartEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
    // Don't change this to `hasClientOpened`
    boolean open = blockEntity.isClientOpened();
    blockEntity.setClientOpened(entity.isClientOpened());
    this.blockEntityRenderDispatcher.renderItem(blockEntity, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY);
    blockEntity.setClientOpened(open);
  }
}
