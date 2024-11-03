package violet.dainty.features.gravestone.corelib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RendererProviders {

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static BlockEntityRendererProvider.Context createBlockEntityRendererContext() {
        return new BlockEntityRendererProvider.Context(minecraft.getBlockEntityRenderDispatcher(), minecraft.getBlockRenderer(), minecraft.getItemRenderer(), minecraft.getEntityRenderDispatcher(), minecraft.getEntityModels(), minecraft.font);
    }

    public static EntityRendererProvider.Context createEntityRendererContext() {
        return new EntityRendererProvider.Context(minecraft.getEntityRenderDispatcher(), minecraft.getItemRenderer(), minecraft.getBlockRenderer(), minecraft.gameRenderer.itemInHandRenderer, minecraft.getResourceManager(), minecraft.getEntityModels(), minecraft.font);
    }

}
