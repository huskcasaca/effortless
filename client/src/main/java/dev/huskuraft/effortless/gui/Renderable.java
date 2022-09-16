package dev.huskuraft.effortless.gui;

import dev.huskuraft.effortless.renderer.Renderer;

public interface Renderable {

    void render(Renderer renderer, int mouseX, int mouseY, float deltaTick);

    void renderOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick);

}
