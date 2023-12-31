package dev.huskuraft.effortless.renderer.texture;

import dev.huskuraft.effortless.renderer.RenderLayer;

public abstract class BlockRenderLayers {

    public abstract RenderLayer lines();

    public abstract RenderLayer planes();

    public abstract RenderLayer block(int color);
}
