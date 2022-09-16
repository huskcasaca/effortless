package dev.huskuraft.effortless.events.render;

import dev.huskuraft.effortless.renderer.Renderer;

@FunctionalInterface
public interface RenderWorld {
    void onRenderWorld(Renderer renderer, float deltaTick);
}
