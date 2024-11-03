package violet.dainty.features.gravestone.corelib.helpers;

import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AbstractStack<T> {

    protected T stack;

    public AbstractStack(T stack) {
        this.stack = stack;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(GuiGraphics guiGraphics, int x, int y);

    @OnlyIn(Dist.CLIENT)
    public abstract List<Component> getTooltip();

    public abstract Component getDisplayName();

    public abstract boolean isEmpty();
}
