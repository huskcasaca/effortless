package dev.huskuraft.effortless.api.renderer.texture;

import dev.huskuraft.effortless.api.renderer.RenderLayer;

public abstract class BlockRenderLayers {

    public abstract RenderLayer lines();

    public abstract RenderLayer planes();

    public abstract RenderLayer block(int color);
}
