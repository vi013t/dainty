package violet.dainty.features.carryon.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SequencedMap;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import violet.dainty.features.carryon.CarryOnData;
import violet.dainty.features.carryon.CarryOnData.CarryType;
import violet.dainty.features.carryon.CarryOnDataManager;
import violet.dainty.features.carryon.CarryOnScript;
import violet.dainty.features.carryon.CarryOnScript.ScriptRender;

public class CarriedObjectRender {

	private static SequencedMap<RenderType, ByteBufferBuilder> builders = new LinkedHashMap<>(Map.of(
		RenderType.glint(), new ByteBufferBuilder(RenderType.glint().bufferSize()),
		RenderType.armorEntityGlint(), new ByteBufferBuilder(RenderType.armorEntityGlint().bufferSize()),
		RenderType.glintTranslucent(), new ByteBufferBuilder(RenderType.glintTranslucent().bufferSize()),
		RenderType.entityGlint(), new ByteBufferBuilder(RenderType.entityGlint().bufferSize()),
		RenderType.entityGlintDirect(), new ByteBufferBuilder(RenderType.entityGlintDirect().bufferSize())
	));

	public static boolean drawFirstPerson(Player player, MultiBufferSource buffer, PoseStack matrix, int light, float partialTicks) {
		if (ModList.get().isLoaded("firstperson") || ModList.get().isLoaded("firstpersonmod")) return false;

		CarryOnData carry = CarryOnDataManager.getCarryData(player);
		try {
			if (carry.isCarrying(CarryType.BLOCK))
				drawFirstPersonBlock(player, buffer, matrix, light, CarryRenderHelper.getRenderState(player));
			else if (carry.isCarrying(CarryType.ENTITY))
				drawFirstPersonEntity(player, buffer, matrix, light, partialTicks);
		}
		catch (Exception e) {}

		if (carry.getActiveScript().isPresent()) {
			ScriptRender render = carry.getActiveScript().get().scriptRender();
			if (!render.renderLeftArm() && player.getMainArm() == HumanoidArm.LEFT) return false;
			if (!render.renderRightArm() && player.getMainArm() == HumanoidArm.RIGHT) return false;
		}

		return carry.isCarrying();
	}

	private static void drawFirstPersonBlock(Player player, MultiBufferSource buffer, PoseStack matrix, int light, BlockState state) {
		matrix.pushPose();
		matrix.scale(2.5f, 2.5f, 2.5f);
		matrix.translate(0, -0.5, -1);
		RenderSystem.enableBlend();
		RenderSystem.disableCull();

		CarryOnData carry = CarryOnDataManager.getCarryData(player);

		if (CarryRenderHelper.isChest(state.getBlock())) {
			matrix.mulPose(Axis.YP.rotationDegrees(180));
			matrix.mulPose(Axis.XN.rotationDegrees(8));
		} else {
			matrix.mulPose(Axis.XP.rotationDegrees(8));
		}

		if (carry.getActiveScript().isPresent()) CarryRenderHelper.performScriptTransformation(matrix, carry.getActiveScript().get());

		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

		ItemStack stack = new ItemStack(state.getBlock().asItem());
		BakedModel model = CarryRenderHelper.getRenderBlock(player);
		CarryRenderHelper.renderBakedModel(stack, matrix, buffer, light, model);

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		matrix.popPose();
	}

	private static void drawFirstPersonEntity(Player player, MultiBufferSource buffer, PoseStack matrix, int light, float partialTicks) {
		EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
		Entity entity = CarryRenderHelper.getRenderEntity(player);
		CarryOnData carry = CarryOnDataManager.getCarryData(player);

		if (entity != null) {
			Vec3 playerPosition = CarryRenderHelper.getExactPos(player, partialTicks);
			entity.setPos(playerPosition.x, playerPosition.y, playerPosition.z);
			entity.xRotO = 0.0f;
			entity.yRotO = 0.0f;
			entity.setYHeadRot(0.0f);

			float height = entity.getBbHeight();
			float width = entity.getBbWidth();

			matrix.pushPose();
			matrix.scale(0.8f, 0.8f, 0.8f);
			matrix.mulPose(Axis.YP.rotationDegrees(180));
			matrix.translate(0.0, -height - .1, width + 0.1);

			manager.setRenderShadow(false);

			Optional<CarryOnScript> res = carry.getActiveScript();
			if (res.isPresent()) {
				CarryOnScript script = res.get();
				CarryRenderHelper.performScriptTransformation(matrix, script);
			}

			if (entity instanceof LivingEntity living) living.hurtTime = 0;

			try {
				manager.render(entity, 0, 0, 0, 0f, 0, matrix, buffer, light);
			}
			catch (Exception e) {}
			manager.setRenderShadow(true);
			matrix.popPose();

		}

		// RenderSystem.disableAlphaTest();
	}

	/**
	 * Draws the third person view of entities and blocks
	 * @param partialTicks
	 * @param mat
	 */
	@SuppressWarnings({ "null", "deprecation" })
	public static void drawThirdPerson(float partialTicks, Matrix4f mat) {
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		int light = 0;
		int perspective = CarryRenderHelper.getPerspective();
		EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();

		PoseStack matrix = new PoseStack();
		matrix.mulPose(mat);

		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();

		BufferSource buffer = MultiBufferSource.immediateWithBuffers(builders, builders.get(RenderType.glint()));

		for (Player player : level.players()) {
			try {
				CarryOnData carry = CarryOnDataManager.getCarryData(player);
				if (perspective == 0 && player == mc.player && !(ModList.get().isLoaded("firstperson") || ModList.get().isLoaded("firstpersonmod") || ModList.get().isLoaded("realcamera"))) continue;
				light = manager.getPackedLightCoords(player, partialTicks);

				// Carrying block
				if (carry.isCarrying(CarryType.BLOCK)) {
					BlockState state = CarryRenderHelper.getRenderState(player);
					CarryRenderHelper.applyBlockTransformations(player, partialTicks, matrix, state.getBlock());
					ItemStack tileItem = new ItemStack(state.getBlock().asItem());
					BakedModel model = CarryRenderHelper.getRenderBlock(player);

					Optional<CarryOnScript> res = carry.getActiveScript();
					if (res.isPresent()) {
						CarryOnScript script = res.get();
						CarryRenderHelper.performScriptTransformation(matrix, script);
					}

					RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
					RenderSystem.enableCull();

					PoseStack.Pose p = matrix.last();
					PoseStack copy = new PoseStack();
					copy.mulPose(p.pose());
					matrix.popPose();

					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

					CarryRenderHelper.renderBakedModel(tileItem, copy, buffer, light, model);

					matrix.popPose();
				} 
				
				// Carrying entity
				else if (carry.isCarrying(CarryType.ENTITY)) {
					Entity entity = CarryRenderHelper.getRenderEntity(player);

					if (entity != null) {
						CarryRenderHelper.applyEntityTransformations(player, partialTicks, matrix, entity);

						manager.setRenderShadow(false);

						Optional<CarryOnScript> res = carry.getActiveScript();
						if (res.isPresent()) {
							CarryOnScript script = res.get();
							CarryRenderHelper.performScriptTransformation(matrix, script);
						}

						if (entity instanceof LivingEntity le)
							le.hurtTime = 0;

						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

						manager.render(entity, 0, 0, 0, 0f, 0, matrix, buffer, light);
						matrix.popPose();
						manager.setRenderShadow(true);
						matrix.popPose();
					}
				}


			}
			catch (Exception e)
			{
			}

		}
		buffer.endLastBatch();

		buffer.endBatch(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
		buffer.endBatch(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
		buffer.endBatch(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
		buffer.endBatch(RenderType.entitySmoothCutout(TextureAtlas.LOCATION_BLOCKS));

		RenderSystem.enableDepthTest();
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
	}

}