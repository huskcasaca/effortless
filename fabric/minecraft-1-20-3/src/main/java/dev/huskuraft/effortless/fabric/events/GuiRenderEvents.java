package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderEvents {

    public static final Event<RenderGui> RENDER_GUI = EventFactory.createArrayBacked(RenderGui.class, callbacks -> (guiGraphics, deltaTick) -> {
        for (RenderGui callback : callbacks) {
            callback.onRenderGui(guiGraphics, deltaTick);
        }
    });

    @FunctionalInterface
    public interface RenderGui {
        void onRenderGui(GuiGraphics guiGraphics, float deltaTick);
    }

}

