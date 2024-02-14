package dev.huskuraft.effortless.api.events.render;

import dev.huskuraft.effortless.api.renderer.Renderer;

@FunctionalInterface
public interface RenderGui {
    void onRenderGui(Renderer renderer, float deltaTick);
}
