package violet.dainty.features.foodinfo.client;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.food.FoodData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.common.NeoForge;
import violet.dainty.features.foodinfo.ModConfig;
import violet.dainty.features.foodinfo.helpers.HungerHelper;

@OnlyIn(Dist.CLIENT)
public class DebugInfoHandler
{
	private static final DecimalFormat saturationDF = new DecimalFormat("#.##");
	private static final DecimalFormat exhaustionValDF = new DecimalFormat("0.00");
	private static final DecimalFormat exhaustionMaxDF = new DecimalFormat("#.##");

	public static void init()
	{
		NeoForge.EVENT_BUS.register(new DebugInfoHandler());
	}

	@SubscribeEvent
	public void onTextRender(CustomizeGuiOverlayEvent.DebugText textEvent)
	{
		if (!ModConfig.SHOW_FOOD_DEBUG_INFO)
			return;

		Minecraft mc = Minecraft.getInstance();

		if (!mc.getDebugOverlay().showDebugScreen())
			return;

		@SuppressWarnings("null")
		FoodData stats = mc.player.getFoodData();
		float curExhaustion = stats.getExhaustionLevel();
		float maxExhaustion = HungerHelper.getMaxExhaustion(mc.player);
		textEvent.getLeft().add("hunger: " + stats.getFoodLevel() + ", sat: " + saturationDF.format(stats.getSaturationLevel()) + ", exh: " + exhaustionValDF.format(curExhaustion) + "/" + exhaustionMaxDF.format(maxExhaustion));
	}
}
