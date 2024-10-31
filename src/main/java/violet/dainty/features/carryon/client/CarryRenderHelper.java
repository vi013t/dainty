package violet.dainty.features.carryon.client;

import java.util.Optional;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import violet.dainty.features.carryon.CarryOnData;
import violet.dainty.features.carryon.CarryOnData.CarryType;
import violet.dainty.features.carryon.CarryOnDataManager;
import violet.dainty.features.carryon.CarryOnScript;
import violet.dainty.features.carryon.CarryOnScript.ScriptRender;

public class CarryRenderHelper {

	public static Vec3 getExactPos(Entity entity, float partialTicks) {
		return new Vec3(entity.xOld + (entity.getX() - entity.xOld) * partialTicks, entity.yOld + (entity.getY() - entity.yOld) * partialTicks, entity.zOld + (entity.getZ() - entity.zOld) * partialTicks);
	}

	public static float getExactBodyRotationDegrees(LivingEntity entity, float partialTicks) {
		if (entity.getVehicle() != null && entity.getVehicle() instanceof LivingEntity vehicle) {
			if (vehicle instanceof Player player) return -(player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * partialTicks);
			else return -(entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks);
		}
		else return -(entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks);
	}

	public static Quaternionf getExactBodyRotation(LivingEntity entity, float partialTicks) {
		return Axis.YP.rotationDegrees(getExactBodyRotationDegrees(entity, partialTicks));
	}

	public static void applyGeneralTransformations(Player player, float partialTicks, PoseStack matrix) {
		int perspective = CarryRenderHelper.getPerspective();
		Quaternionf playerRotation = CarryRenderHelper.getExactBodyRotation(player, partialTicks);
		Vec3 playerPosition = CarryRenderHelper.getExactPos(player, partialTicks);
		@SuppressWarnings("resource")
		Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		Vec3 offset = playerPosition.subtract(cameraPos);
		Pose pose = player.getPose();

		matrix.pushPose();
		matrix.translate(offset.x, offset.y, offset.z);

		if (perspective == 2)
			playerRotation.mul(Axis.YP.rotationDegrees(180));
		matrix.mulPose(playerRotation);

		matrix.pushPose();
		matrix.scale(0.6f, 0.6f, 0.6f);

		if (perspective == 2)
			matrix.translate(0, 0, -1.35);

		if (doSneakCheck(player))
		{
			matrix.translate(0, -0.4, 0);
		}

		if (pose == Pose.SWIMMING)
		{
			float f = player.getSwimAmount(partialTicks);
			float f3 = player.isInWater() ? -90.0F - player.xRotO : -90.0F;
			float f4 = Mth.lerp(f, 0.0F, f3);
			if (perspective == 2)
			{
				matrix.translate(0, 0, 1.35);
				matrix.mulPose(Axis.XP.rotationDegrees(f4));
			}
			else
				matrix.mulPose(Axis.XN.rotationDegrees(f4));

			matrix.translate(0, -1.5, -1.848);
			if (perspective == 2)
				matrix.translate(0, 0, 2.38);
		}

		if (pose == Pose.FALL_FLYING)
		{
			float f1 = player.getFallFlyingTicks() + partialTicks;
			float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if (!player.isAutoSpinAttack())
			{
				if (perspective == 2)
					matrix.translate(0, 0, 1.35);

				if (perspective == 2)
					matrix.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - player.xRotO)));
				else
					matrix.mulPose(Axis.XN.rotationDegrees(f2 * (-90.0F - player.xRotO)));
			}

			Vec3 viewVector = player.getViewVector(partialTicks);
			Vec3 deltaMovement = player.getDeltaMovement();
			double d0 = deltaMovement.horizontalDistanceSqr();
			double d1 = deltaMovement.horizontalDistanceSqr();
			if (d0 > 0.0D && d1 > 0.0D)
			{
				double d2 = (deltaMovement.x * viewVector.x + deltaMovement.z * viewVector.z) / (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = deltaMovement.x * viewVector.z - deltaMovement.z * viewVector.x;

				matrix.mulPose(Axis.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
			}

			if (perspective != 2)
				matrix.translate(0, 0, -1.35);
			matrix.translate(0, -0.2, 0);
		}

		matrix.translate(0, 1.6, 0.65);
	}

	public static void applyBlockTransformations(Player player, float partialTicks, PoseStack matrix, Block block) {
		CarryRenderHelper.getPerspective();

		applyGeneralTransformations(player, partialTicks, matrix);

		if (CarryRenderHelper.isChest(block)) {
			matrix.mulPose(Axis.YP.rotationDegrees(180));
		}

		float height = getRenderHeight(player);
		float offset = (height - 1f) / 1.2f;
		matrix.translate(0, -offset, 0);
	}

	public static void applyEntityTransformations(Player player, float partialTicks, PoseStack matrix, Entity entity) {
		int perspective = CarryRenderHelper.getPerspective();
		Pose pose = player.getPose();

		applyGeneralTransformations(player, partialTicks, matrix);

		if (perspective == 2) matrix.translate(0, -1.6, 0.65);
		else matrix.translate(0, -1.6, -0.65);
		matrix.scale(1.666f, 1.666f, 1.666f);

		float height = entity.getBbHeight();
		float width = entity.getBbWidth();
		float multiplier = height * width;
		entity.yo = 0.0f;
		entity.yRotO = 0.0f;
		entity.setYHeadRot(0.0f);
		entity.xo = 0.0f;
		entity.xRotO = 0.0f;

		if (perspective == 2)
			matrix.mulPose(Axis.YP.rotationDegrees(180));

		matrix.scale((10 - multiplier) * 0.08f, (10 - multiplier) * 0.08f, (10 - multiplier) * 0.08f);
		matrix.translate(0.0, height / 2 + -(height / 2) + 1, width - 0.1 < 0.7 ? width - 0.1 + (0.7 - (width - 0.1)) : width - 0.1);

		if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING)
		{
			matrix.mulPose(Axis.XN.rotationDegrees(90));
			matrix.translate(0, -0.2 * height, 0);

			if (pose == Pose.FALL_FLYING)
				matrix.translate(0, 0, 0.2);
		}

	}


	public static void performScriptTransformation(PoseStack matrix, CarryOnScript script) {
		int perspective = getPerspective();

		ScriptRender render = script.scriptRender();

		Vec3 translation = render.renderTranslation().getVec();
		Vec3 rotation = render.renderRotation().getVec();
		Vec3 scale = render.renderScale().getVec(1, 1, 1);

		Quaternionf rot = Axis.XP.rotationDegrees((float) rotation.x);
		rot.mul(Axis.YP.rotationDegrees((float) rotation.y));
		rot.mul(Axis.ZP.rotationDegrees((float) rotation.z));
		matrix.mulPose(rot);

		matrix.translate(translation.x, translation.y, perspective == 1 && script.isBlock() ? -translation.z : translation.z);

		matrix.scale((float) scale.x, (float) scale.y, (float) scale.z);
	}



	public static void renderBakedModel(ItemStack stack, PoseStack matrix, MultiBufferSource buffer, int light, BakedModel model) {
		try {
			ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
			renderer.render(stack, ItemDisplayContext.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);
		}
		catch (Exception e)
		{
		}
	}

	@SuppressWarnings("null")
	public static BlockState getRenderState(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		BlockState state = carry.getBlock().getBlock().defaultBlockState();
		if (carry.getActiveScript().isPresent()) {
			ScriptRender render = carry.getActiveScript().get().scriptRender();
			if (render.renderNameBlock().isPresent()) {
				state = BuiltInRegistries.BLOCK.get(render.renderNameBlock().get()).defaultBlockState();
			}
		}

		Optional<ModelOverride> ov = ModelOverrideHandler.getModelOverride(state, carry.getContentNbt());
		if (ov.isPresent()) {
			var renderObj = ov.get().getRenderObject();
			if (renderObj.right().isPresent()) state = renderObj.right().get();
		}

		return state;
	}

	@SuppressWarnings("deprecation")
	public static BakedModel getRenderBlock(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		BlockState state = getRenderState(player);
		BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);

		if(state.getRenderShape() != RenderShape.MODEL || model.isCustomRenderer() || model.getQuads(state, null, RandomSource.create()).size() <= 0) {
			ItemStack stack = new ItemStack(state.getBlock());
			model = renderer.getModel(stack, player.level(), player, 0);
		}

		Optional<ModelOverride> ov = ModelOverrideHandler.getModelOverride(state, carry.getContentNbt());
		if(ov.isPresent())
		{
			var renderObj = ov.get().getRenderObject();
			if(renderObj.left().isPresent())
				model = renderer.getModel(renderObj.left().get(), player.level(), player, 0);
		}

		return model;
	}

	@SuppressWarnings("null")
	public static Entity getRenderEntity(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		Entity entity = carry.getEntity(player.level());

		if (carry.getActiveScript().isPresent()) {
			CarryOnScript script = carry.getActiveScript().get();
			ScriptRender render = script.scriptRender();
			if (render.renderNameEntity().isPresent()) entity = BuiltInRegistries.ENTITY_TYPE.get(render.renderNameEntity().get()).create(player.level());
			if (render.renderNBT().isPresent()) entity.load(render.renderNBT().get());
		}

		return entity;
	}

	public static float getRenderWidth(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		if (carry.isCarrying(CarryType.BLOCK)) {
			BlockState state = getRenderState(player);
			VoxelShape shape = state.getShape(player.level(), player.blockPosition());
			if (shape == null || shape.isEmpty()) return 1f;
			Optional<ModelOverride> ov = ModelOverrideHandler.getModelOverride(state, carry.getContentNbt());
			if (ov.isPresent()) {
				var renderObj = ov.get().getRenderObject();
				if (renderObj.left().isPresent()) return 0.8f;
			}
			float width = (float)Math.abs(shape.bounds().maxX - shape.bounds().minX);
			return width;
		}
		else if(carry.isCarrying(CarryType.ENTITY)) {
			Entity entity = getRenderEntity(player);
			return entity.getBbWidth();
		}
		else return 1f;
	}

	public static float getRenderHeight(Player player) {
		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		if(carry.isCarrying(CarryType.BLOCK))
		{
			BlockState state = getRenderState(player);
			VoxelShape shape = state.getShape(player.level(), player.blockPosition());
			if(shape == null || shape.isEmpty())
				return 1f;
			Optional<ModelOverride> ov = ModelOverrideHandler.getModelOverride(state, carry.getContentNbt());
			if(ov.isPresent())
			{
				var renderObj = ov.get().getRenderObject();
				if(renderObj.left().isPresent())
					return 0.5f;
			}
			float height = (float)Math.abs(shape.bounds().maxY - shape.bounds().minY);
			return height;
		}
		else if(carry.isCarrying(CarryType.ENTITY))
		{
			Entity entity = getRenderEntity(player);
			return entity.getBbHeight();
		}
		else
			return 1f;
	}


	@SuppressWarnings("resource")
	public static int getPerspective() {
		boolean isThirdPerson = !Minecraft.getInstance().options.getCameraType().isFirstPerson(); // isThirdPerson
		boolean isThirdPersonReverse = Minecraft.getInstance().options.getCameraType().isMirrored();

		if (!isThirdPerson && !isThirdPersonReverse)
			return 0;
		if (isThirdPerson && !isThirdPersonReverse)
			return 1;
		return 2;
	}

	public static boolean doSneakCheck(Player player)
	{
		if (player.getAbilities().flying)
			return false;

		return player.isShiftKeyDown() || player.isCrouching();
	}

	public static boolean isChest(Block block)
	{
		return block == Blocks.CHEST || block == Blocks.ENDER_CHEST || block == Blocks.TRAPPED_CHEST;
	}

}
