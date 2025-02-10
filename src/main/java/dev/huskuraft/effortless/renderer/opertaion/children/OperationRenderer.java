package dev.huskuraft.effortless.renderer.opertaion.children;

import dev.huskuraft.universal.api.renderer.Renderer;

public interface OperationRenderer {

    void render(Renderer renderer, RenderContext renderContext, float deltaTick);

    interface RenderContext {

        boolean showBlockPreview();

        int maxRenderVolume();

        int maxRenderDistance();

        record Default(boolean showBlockPreview, int maxRenderVolume, int maxRenderDistance) implements RenderContext {
        }


    }
}
