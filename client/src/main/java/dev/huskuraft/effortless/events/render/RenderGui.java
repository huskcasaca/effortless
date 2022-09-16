package dev.huskuraft.effortless.events.render;

import dev.huskuraft.effortless.renderer.Renderer;

@FunctionalInterface
public interface RenderGui {
    void onRenderGui(Renderer renderer, float deltaTick);
}
