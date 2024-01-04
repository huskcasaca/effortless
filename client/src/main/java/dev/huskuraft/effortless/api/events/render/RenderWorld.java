package dev.huskuraft.effortless.api.events.render;

import dev.huskuraft.effortless.api.renderer.Renderer;

@FunctionalInterface
public interface RenderWorld {
    void onRenderWorld(Renderer renderer, float deltaTick);
}
