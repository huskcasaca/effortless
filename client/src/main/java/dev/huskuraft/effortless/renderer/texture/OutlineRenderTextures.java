package dev.huskuraft.effortless.renderer.texture;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderTexture;

public abstract class OutlineRenderTextures {

    public abstract RenderTexture outlineSolid();

    public abstract RenderTexture outlineSolid(boolean overlap);

    public abstract RenderTexture outlineTranslucent(Resource texture, boolean cull);

    public abstract RenderTexture glowingSolid(Resource texture);

    public abstract RenderTexture glowingSolid();

    public abstract RenderTexture glowingTranslucent(Resource texture);

    public abstract RenderTexture additive();

    public abstract RenderTexture glowingTranslucent();

    public abstract RenderTexture itemPartialSolid();

    public abstract RenderTexture itemPartialTranslucent();

    public abstract RenderTexture fluid();

}