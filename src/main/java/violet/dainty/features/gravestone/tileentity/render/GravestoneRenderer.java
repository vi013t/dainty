package violet.dainty.features.gravestone.tileentity.render;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.gui.Font;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.gravestone.GraveUtils;
import violet.dainty.features.gravestone.GravestoneClientConfig;
import violet.dainty.features.gravestone.blocks.GraveStoneBlock;
import violet.dainty.features.gravestone.corelib.client.PlayerSkins;
import violet.dainty.features.gravestone.tileentity.GraveStoneTileEntity;

public class GravestoneRenderer implements BlockEntityRenderer<GraveStoneTileEntity> {

    private BlockEntityRendererProvider.Context renderer;

    public GravestoneRenderer(BlockEntityRendererProvider.Context renderer) {
        this.renderer = renderer;
    }

    @Override
    public void render(@Nonnull GraveStoneTileEntity grave, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Component name = grave.getGraveName();
        if (name == null) {
            return;
        }

        Direction direction = grave.getBlockState().getValue(GraveStoneBlock.FACING);

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1D, 0.5D);
        matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180F + direction.toYRot()));

        Font font = renderer.getFont();

        int textWidth = font.width(name.getString());
        double textScale = Math.min(0.8D / textWidth, 0.02D);

        matrixStack.translate(0D, 0.3D, 0.37D);
        matrixStack.scale((float) textScale, (float) textScale, (float) textScale);
        float left = (float) (-textWidth / 2);  
        font.drawInBatch(name.getString(), left, 0F, GravestoneClientConfig.GRAVE_TEXT_COLOR_SPEC, false, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, combinedLight);
        matrixStack.popPose();

        @SuppressWarnings("null")
		BlockState state = grave.getLevel().getBlockState(grave.getBlockPos().below());

        @SuppressWarnings("null")
		boolean render = state.isRedstoneConductor(grave.getLevel(), grave.getBlockPos()); //TODO fix with slime block
        UUID playerUUID = grave.getDeath().getPlayerUUID();
        if (playerUUID != null && !playerUUID.equals(GraveUtils.EMPTY_UUID) && GravestoneClientConfig.RENDER_SKULL && render) {
            renderSkull(playerUUID, name.getString(), direction, matrixStack, buffer, combinedLight);
        }
    }  

    public void renderSkull(UUID uuid, String name, Direction rotation, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        SkullModel model = new SkullModel(renderer.bakeLayer(ModelLayers.PLAYER_HEAD));
        ResourceLocation resourceLocation = PlayerSkins.getSkin(uuid, name);

        matrixStack.pushPose();

        matrixStack.translate(0.5D, 0D, 0.5D);

        matrixStack.mulPose(Axis.YP.rotationDegrees(180F - rotation.toYRot()));
        matrixStack.mulPose(Axis.YP.rotationDegrees(-26F));
        matrixStack.translate(0D, -0.14D, 0.18D);
        matrixStack.mulPose(Axis.XP.rotationDegrees(180F));
        matrixStack.mulPose(Axis.XP.rotationDegrees(-61F));

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        model.renderToBuffer(matrixStack, buffer.getBuffer(model.renderType(resourceLocation)), combinedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
