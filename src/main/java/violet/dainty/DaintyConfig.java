package violet.dainty;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DaintyConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Float> ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE = BUILDER
		.comment("Drop chance of phantom membranes from enderman")
		.define("endermanPhantomMembraneDropChance", 0.5f);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_PHANTOMS = BUILDER
		.comment("Disable phantom spawns")
		.define("disablePhantoms", true);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_FARMLAND_TRAMPLING = BUILDER
		.comment("Disable farmland trampling")
		.define("disableFarmlandTrampling", true);

    private static final ModConfigSpec.ConfigValue<Boolean> ALLOW_JUMPING_ON_FENCES = BUILDER
		.comment("Allow jumping on fences")
		.define("allowJumpingOnFences", true);

	private static final ModConfigSpec.ConfigValue<Integer> WITHER_SKELETON_KILLS_PER_GUARANTEED_SKULL = BUILDER
		.comment("Number of wither skeleton kills between guaranteed skull drops")
		.define("witherSkeletonKillsPerGuaranteedSkull", 20);

    private static final ModConfigSpec.ConfigValue<Float> WARDEN_HEART_DROP_CHANCE = BUILDER
		.comment("Chance for Wardens to drop a Warden Heart")
		.define("wardenHeartDropChance", 1f);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static float endermanPhantomMembraneDropChance;
	private static boolean disablePhantoms;
	private static boolean disableFarmlandTrampling;
	private static boolean allowJumpingOnFences;
	private static int witherSkeletonKillsPerGuaranteedSkull;
	private static float wardenHeartDropChance;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        endermanPhantomMembraneDropChance = ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE.get();
		disablePhantoms = DISABLE_PHANTOMS.get();
		disableFarmlandTrampling = DISABLE_FARMLAND_TRAMPLING.get();
		allowJumpingOnFences = ALLOW_JUMPING_ON_FENCES.get();
		witherSkeletonKillsPerGuaranteedSkull = WITHER_SKELETON_KILLS_PER_GUARANTEED_SKULL.get();
		wardenHeartDropChance = WARDEN_HEART_DROP_CHANCE.get();
    }

	public static float endermanPhantomMembraneDropChance() {
		return endermanPhantomMembraneDropChance;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static boolean disableFarmlandTrampling() {
		return disableFarmlandTrampling;
	}
	
	public static boolean allowJumpingOnFences() {
		return allowJumpingOnFences;
	}

	public static int witherSkeletonKillsPerGuaranteedSkull() {
		return witherSkeletonKillsPerGuaranteedSkull;
	}

	public static float wardenHeartDropChance() {
		return wardenHeartDropChance;
	}
}
