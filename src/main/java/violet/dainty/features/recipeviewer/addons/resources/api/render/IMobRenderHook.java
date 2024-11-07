package violet.dainty.features.recipeviewer.addons.resources.api.render;

import net.minecraft.world.entity.LivingEntity;

public interface IMobRenderHook<T extends LivingEntity> {
    class RenderInfo {
        public int x, y;
        public double scale, yaw, pitch;

        public RenderInfo(int x, int y, double scale, double yaw, double pitch) {
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    /**
     * This method will be called up drawing mobs in the view
     *
     * @param renderInfo contains info about the current render context see {@link RenderInfo}
     * @param entity     the entity that will be rendered
     * @return the given {@link RenderInfo} with possible changes
     */
    RenderInfo transform(RenderInfo renderInfo, T entity);
}
