package dev.huskuraft.effortless.renderer.texture;

import dev.huskuraft.effortless.renderer.RenderTexture;

public abstract class BlockRenderTextures {

    public abstract RenderTexture lines();

    public abstract RenderTexture planes();

    public abstract RenderTexture solid(int color);
}
