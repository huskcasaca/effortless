package dev.huskuraft.effortless.renderer.opertaion.children;

public interface RendererParams {

    boolean showBlockPreview();

    int maxRenderBlocks();

    int maxRenderDistance();

    record Default(boolean showBlockPreview, int maxRenderBlocks, int maxRenderDistance) implements RendererParams {
    }


}
