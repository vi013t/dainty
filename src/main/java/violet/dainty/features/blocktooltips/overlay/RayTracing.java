package violet.dainty.features.blocktooltips.overlay;

import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.impl.ObjectDataCenter;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.ui.ItemStackElement;
import violet.dainty.features.blocktooltips.util.CommonProxy;

public class RayTracing {

	public static final RayTracing INSTANCE = new RayTracing();
	public static Predicate<Entity> ENTITY_FILTER = entity -> true;
	private final Minecraft mc = Minecraft.getInstance();
	@Nullable
	private HitResult target;

	private RayTracing() {
	}

	public static BlockState wrapBlock(BlockGetter level, BlockHitResult hit, CollisionContext context) {
		if (hit.getType() != HitResult.Type.BLOCK) {
			return Blocks.AIR.defaultBlockState();
		}
		BlockState blockState = level.getBlockState(hit.getBlockPos());
		FluidState fluidState = blockState.getFluidState();
		if (!fluidState.isEmpty()) {
			if (blockState.is(Blocks.BARRIER) && WailaClientRegistration.instance().shouldHide(blockState)) {
				return fluidState.createLegacyBlock();
			}
			if (blockState.getShape(level, hit.getBlockPos(), context).isEmpty()) {
				return fluidState.createLegacyBlock();
			}
		}
		return blockState;
	}

	// from ProjectileUtil
	@Nullable
	public static EntityHitResult getEntityHitResult(
			Level worldIn,
			Entity projectile,
			Vec3 startVec,
			Vec3 endVec,
			AABB boundingBox,
			Predicate<Entity> filter) {
		double d0 = Double.MAX_VALUE;
		Entity entity = null;

		for (Entity entity1 : worldIn.getEntities(projectile, boundingBox, filter)) {
			AABB axisalignedbb = entity1.getBoundingBox();
			if (axisalignedbb.getSize() < 0.3) {
				axisalignedbb = axisalignedbb.inflate(0.3);
			}
			if (axisalignedbb.contains(startVec)) {
				entity = entity1;
				break;
			}
			Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
			if (optional.isPresent()) {
				double d1 = startVec.distanceToSqr(optional.get());
				if (d1 < d0) {
					entity = entity1;
					d0 = d1;
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity);
	}

	public static boolean isEmptyElement(IElement element) {
		return element == null || element == ItemStackElement.EMPTY;
	}

	public void fire() {
		Entity viewEntity = mc.getCameraEntity();
		Player viewPlayer = viewEntity instanceof Player ? (Player) viewEntity : mc.player;
		if (viewEntity == null || viewPlayer == null) {
			return;
		}

		if (mc.hitResult != null && mc.hitResult.getType() == Type.ENTITY) {
			@SuppressWarnings("null")
			Entity targetEntity = ((EntityHitResult) mc.hitResult).getEntity();
			if (canBeTarget(targetEntity, viewEntity)) {
				target = mc.hitResult;
				return;
			}
		}

		float extendedReach = IWailaConfig.get().getGeneral().getExtendedReach();
		double blockReach = viewPlayer.blockInteractionRange() + extendedReach;
		double entityReach = viewPlayer.entityInteractionRange() + extendedReach;
		target = rayTrace(viewEntity, blockReach, entityReach);
	}

	@Nullable
	public HitResult getTarget() {
		return target;
	}

	@SuppressWarnings("null")
	public HitResult rayTrace(Entity entity, double blockReach, double entityReach) {
		Camera camera = mc.gameRenderer.getMainCamera();
		float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(true);
		Vec3 eyePosition = entity.getEyePosition(partialTick);
		boolean startFromEye = IWailaConfig.get().getGeneral().getPerspectiveMode() == IWailaConfig.PerspectiveMode.EYE;
		Vec3 traceStart = startFromEye ? eyePosition : camera.getPosition();
		double distance = startFromEye ? 0 : eyePosition.distanceToSqr(traceStart);
		if (distance > 1e-5) {
			distance = Math.sqrt(distance);
			blockReach += distance;
			entityReach += distance;
		}

		Vec3 traceEnd;
		Vec3 lookVector;
		if (mc.hitResult == null) {
			lookVector = startFromEye ? entity.getViewVector(partialTick) : new Vec3(camera.getLookVector());
			traceEnd = traceStart.add(lookVector.scale(entityReach));
		} else {
			traceEnd = mc.hitResult.getLocation().subtract(traceStart);
			lookVector = startFromEye ? entity.getViewVector(partialTick) : traceEnd.normalize();
			// when it comes to a block hit, we only need to find entities that closer than the block
			if (mc.hitResult.getType() == Type.BLOCK && traceEnd.lengthSqr() < entityReach * entityReach) {
				traceEnd = startFromEye ? traceStart.add(lookVector.scale(traceEnd.length() + 1e-5)) : mc.hitResult.getLocation().add(
						lookVector.scale(1e-5));
			} else {
				traceEnd = traceStart.add(lookVector.scale(entityReach));
			}
		}

		Level world = entity.level();
		AABB bound = new AABB(traceStart, traceEnd);
		Predicate<Entity> predicate = e -> canBeTarget(e, entity);
		EntityHitResult entityResult = getEntityHitResult(world, entity, traceStart, traceEnd, bound, predicate);

		if (blockReach != entityReach) {
			traceEnd = traceStart.add(lookVector.scale(blockReach * 1.001));
		}

		BlockState eyeBlock = world.getBlockState(BlockPos.containing(eyePosition));
		ClipContext.Fluid fluidView = ClipContext.Fluid.NONE;
		IWailaConfig.FluidMode fluidMode = IWailaConfig.get().getGeneral().getDisplayFluids();
		if (eyeBlock.getFluidState().isEmpty()) {
			fluidView = fluidMode.ctx;
		}
		CollisionContext collisionContext = CollisionContext.of(entity);
		ClipContext context = new ClipContext(traceStart, traceEnd, ClipContext.Block.OUTLINE, fluidView, collisionContext);

		BlockHitResult blockResult = world.clip(context);
		if (entityResult != null) {
			if (blockResult.getType() == Type.BLOCK) {
				double entityDist = entityResult.getLocation().distanceToSqr(traceStart);
				double blockDist = blockResult.getLocation().distanceToSqr(traceStart);
				if (entityDist < blockDist) {
					return entityResult;
				}
			} else {
				return entityResult;
			}
		}
		if (blockResult.getType() == Type.MISS && mc.hitResult instanceof BlockHitResult hit) {
			// weird, we didn't hit a block in our way. try the vanilla result
			blockResult = hit;
		}
		if (blockResult.getType() == Type.BLOCK) {
			BlockState state = wrapBlock(world, blockResult, collisionContext);
			if (WailaClientRegistration.instance().shouldHide(state)) {
				blockResult = null;
			}
		} else {
			blockResult = null;
		}
		if (blockResult == null && fluidMode == IWailaConfig.FluidMode.FALLBACK) {
			context = new ClipContext(traceStart, traceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, collisionContext);
			blockResult = world.clip(context);
			BlockState state = wrapBlock(world, blockResult, collisionContext);
			if (WailaClientRegistration.instance().shouldHide(state)) {
				blockResult = null;
			}
		}

		return blockResult;
	}

	@SuppressWarnings("null")
	private boolean canBeTarget(Entity target, Entity viewEntity) {
		if (target.isRemoved()) {
			return false;
		}
		if (target.isSpectator()) {
			return false;
		}
		if (target == viewEntity.getVehicle()) {
			return false;
		}
		if (target instanceof Projectile projectile && projectile.tickCount <= 10 &&
				!target.level().tickRateManager().isEntityFrozen(target)) {
			return false;
		}
		if (CommonProxy.isMultipartEntity(target) && !target.isPickable()) {
			return false;
		}
		if (viewEntity instanceof Player player) {
			if (target.isInvisibleTo(player)) {
				return false;
			}
			if (mc.gameMode.isDestroying() && target.getType() == EntityType.ITEM) {
				return false;
			}
		} else {
			if (target.isInvisible()) {
				return false;
			}
		}
		return !WailaClientRegistration.instance().shouldHide(target) && ENTITY_FILTER.test(target);
	}

	public IElement getIcon() {
		Accessor<?> accessor = ObjectDataCenter.get();
		if (accessor == null) {
			return null;
		}

		IElement icon = ObjectDataCenter.getIcon();
		if (isEmptyElement(icon)) {
			return null;
		} else {
			return icon;
		}
	}

}
