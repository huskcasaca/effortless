package dev.huskuraft.effortless.fabric.events;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ClientRenderEvents {

    public static final Event<RenderGui> GUI = EventFactory.createArrayBacked(RenderGui.class, callbacks -> (matrixStack, deltaTick) -> {
        for (RenderGui callback : callbacks) {
            callback.onRenderGui(matrixStack, deltaTick);
        }
    });

    @FunctionalInterface
    public interface RenderGui {
        void onRenderGui(PoseStack matrixStack, float deltaTick);
    }

}

