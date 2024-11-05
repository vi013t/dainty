package violet.dainty.mixins.friendlyberries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import violet.dainty.config.DaintyConfig;

@Mixin(SweetBerryBushBlock.class)
public class BerryBushMixin {
	
	/**
	 * Called when an entity is inside a berry bush. In vanilla, this slows the entity and damages them. Dainty
	 * provides configuration values to both {@link DaintyConfig#DISABLE_BERRY_SLOWDOWN disable the slowdown effect}
	 * and {@link DaintyConfig#DISABLE_BERRY_DAMAGE disable the damage}, both of which are enabled by default (meaning
	 * no damage or slowdown by default).
	 * 
	 * @param state The block state of the berry bush the entity is in
	 * @param level The level the entity and berry bush exist in
	 * @param pos The position of the berry bush block
	 * @param entity The entity that's in the berry bush
	 * @param callbackInfo The callback info provided by the mixin library
	 */
	@Inject(method = "entityInside", at = @At("HEAD"))
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {

			// Slow entity
			if (!DaintyConfig.DISABLE_BERRY_SLOWDOWN.get()) entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));

			// Damage entity
			if (!DaintyConfig.DISABLE_BERRY_DAMAGE.get()) {
				if (!level.isClientSide && state.getValue(SweetBerryBushBlock.AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
					double d0 = Math.abs(entity.getX() - entity.xOld);
					double d1 = Math.abs(entity.getZ() - entity.zOld);
					if (d0 >= 0.003F || d1 >= 0.003F) {
						entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
					}
				}
            }
        }
    }
}
