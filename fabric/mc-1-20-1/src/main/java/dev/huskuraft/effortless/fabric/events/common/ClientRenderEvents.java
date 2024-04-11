package dev.huskuraft.effortless.fabric.events.common;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;

public class ClientRenderEvents {

    public static final Event<RenderGui> GUI = EventFactory.createArrayBacked(RenderGui.class, callbacks -> (guiGraphics, deltaTick) -> {
        for (RenderGui callback : callbacks) {
            callback.onRenderGui(guiGraphics, deltaTick);
        }
    });

    @FunctionalInterface
    public interface RenderGui {
        void onRenderGui(GuiGraphics guiGraphics, float deltaTick);
    }

}

