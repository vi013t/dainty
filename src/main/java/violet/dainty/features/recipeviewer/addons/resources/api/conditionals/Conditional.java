package violet.dainty.features.recipeviewer.addons.resources.api.conditionals;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.addons.resources.api.render.TextModifier;

public class Conditional {
    public static final Map<Conditional, Conditional> reverse = new LinkedHashMap<>();

    public static final Conditional magmaCream = new Conditional("dainty.magmaCream.text", TextModifier.darkRed);
    public static final Conditional slimeBall = new Conditional("dainty.slimeBall.text", TextModifier.lightGreen);
    public static final Conditional rareDrop = new Conditional("dainty.rareDrop.text", TextModifier.purple);
    public static final Conditional silkTouch = new Conditional("dainty.worldgen.silkTouch", TextModifier.darkCyan);
    public static final Conditional equipmentDrop = new Conditional("dainty.equipmentDrop.text", TextModifier.lightCyan);
    public static final Conditional affectedByLooting = new Conditional("dainty.affectedByLooting.text", TextModifier.lightCyan);
    public static final Conditional affectedByFortune = new Conditional("dainty.affectedByFortune.text", TextModifier.lightCyan);
    public static final Conditional powered = new Conditional("dainty.powered.text", TextModifier.lightCyan);

    public static final Conditional burning = new Conditional("dainty.burning.text", TextModifier.lightRed);
    public static final Conditional notBurning = new Conditional("dainty.notBurning.text", burning);
    public static final Conditional wet = new Conditional("dainty.wet.text", TextModifier.lightCyan);
    public static final Conditional notWet = new Conditional("dainty.notWet.text", wet);
    public static final Conditional hasPotion = new Conditional("dainty.hasPotion.text", TextModifier.pink);
    public static final Conditional hasNoPotion = new Conditional("dainty.hasNoPotion.text", hasPotion);
    public static final Conditional beyond = new Conditional("dainty.beyond.text", TextModifier.darkGreen);
    public static final Conditional nearer = new Conditional("dainty.nearer.text", beyond);
    public static final Conditional raining = new Conditional("dainty.raining.text", TextModifier.lightGrey);
    public static final Conditional dry = new Conditional("dainty.dry.text", raining);
    public static final Conditional thundering = new Conditional("dainty.thundering.text", TextModifier.darkGrey);
    public static final Conditional notThundering = new Conditional("dainty.notThundering.text", thundering);
    public static final Conditional moonPhase = new Conditional("dainty.moonPhase.text");
    public static final Conditional notMoonPhase = new Conditional("dainty.notMoonPhase.text", moonPhase);
    public static final Conditional pastTime = new Conditional("dainty.pastTime.text", TextModifier.lilac);
    public static final Conditional beforeTime = new Conditional("dainty.beforeTime.text", pastTime);
    public static final Conditional pastWorldTime = new Conditional("dainty.pastWorldTime.text", TextModifier.purple);
    public static final Conditional beforeWorldTime = new Conditional("dainty.beforeWorldTime.text", pastWorldTime);
    public static final Conditional pastWorldDifficulty = new Conditional("dainty.pastWorldDifficulty.text", TextModifier.orange);
    public static final Conditional beforeWorldDifficulty = new Conditional("dainty.beforeWorldDifficulty.text", pastWorldDifficulty);
    public static final Conditional gameDifficulty = new Conditional("dainty.gameDifficulty.text", TextModifier.orange);
    public static final Conditional notGameDifficulty = new Conditional("dainty.notGameDifficulty.text", gameDifficulty);
    public static final Conditional inDimension = new Conditional("dainty.inDimension.text", TextModifier.yellow);
    public static final Conditional notInDimension = new Conditional("dainty.notInDimension.text", inDimension);
    public static final Conditional inBiome = new Conditional("dainty.inBiome.text", TextModifier.orange);
    public static final Conditional notInBiome = new Conditional("dainty.notInBiome.text", inBiome);
    public static final Conditional onBlock = new Conditional("dainty.onBlock.text", TextModifier.lightRed);
    public static final Conditional notOnBlock = new Conditional("dainty.notOnBlock.text", onBlock);
    public static final Conditional below = new Conditional("dainty.below.text", TextModifier.darkGreen);
    public static final Conditional above = new Conditional("dainty.above.text", below);
    public static final Conditional playerOnline = new Conditional("dainty.playerOnline.text", TextModifier.bold);
    public static final Conditional playerOffline = new Conditional("dainty.playerOffline.text", playerOnline);
    public static final Conditional playerKill = new Conditional("dainty.playerKill.text");
    public static final Conditional notPlayerKill = new Conditional("dainty.notPlayerKill.text", playerKill);
    public static final Conditional aboveLooting = new Conditional("dainty.aboveLooting.text", TextModifier.darkBlue);
    public static final Conditional belowLooting = new Conditional("dainty.belowLooting.text", aboveLooting);
    public static final Conditional killedBy = new Conditional("dainty.killedBy.text", TextModifier.darkRed);
    public static final Conditional notKilledBy = new Conditional("dainty.notKilledBy.text", killedBy);

    protected String text;
    protected String colour = "";


    public Conditional() {
    }

    public Conditional(String text, TextModifier... textModifiers) {
        this.text = text;
        for (TextModifier textModifier : textModifiers)
            colour += textModifier.toString();
    }

    public Conditional(String text, Conditional opposite) {
        this(text);
        this.colour = opposite.colour;
        reverse.put(opposite, this);
        reverse.put(this, opposite);
    }

    public Component toStringTextComponent() {
        return Component.literal(toString());
    }

    @Override
    public String toString() {
        return colour + I18n.get(text);
    }
}
