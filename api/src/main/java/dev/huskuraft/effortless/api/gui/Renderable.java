package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.renderer.Renderer;

public interface Renderable {

    void render(Renderer renderer, int mouseX, int mouseY, float deltaTick);

    void renderOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick);

}
