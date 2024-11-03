package violet.dainty.features.blocktooltips.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.Jade;
import violet.dainty.features.blocktooltips.gui.HomeConfigScreen;
import violet.dainty.features.blocktooltips.util.DumpGenerator;

public class JadeClientCommand {

	public static <T> LiteralArgumentBuilder<T> create(
			Function<String, LiteralArgumentBuilder<T>> literalFactory,
			BiConsumer<T, Component> sendSuccess,
			BiConsumer<T, Component> sendFailure) {
		return literalFactory.apply(Dainty.MODID + "c").then(literalFactory.apply("handlers").executes(context -> {
			File file = new File("jade_handlers.md");
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(DumpGenerator.generateInfoDump());
				sendSuccess.accept(context.getSource(), Component.translatable("command.dainty.dump.success"));
				return 1;
			} catch (IOException e) {
				sendFailure.accept(context.getSource(), Component.literal(e.getClass().getSimpleName() + ": " + e.getMessage()));
				return 0;
			}
		})).then(literalFactory.apply("config").executes(context -> {
			Minecraft.getInstance().tell(() -> {
				Jade.CONFIG.invalidate();
				Minecraft.getInstance().setScreen(new HomeConfigScreen(null));
			});
			return 1;
		}));
	}
}
