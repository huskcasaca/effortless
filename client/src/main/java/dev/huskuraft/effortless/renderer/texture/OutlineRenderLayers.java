package dev.huskuraft.effortless.renderer.texture;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderLayer;

public abstract class OutlineRenderLayers {

    public abstract RenderLayer outlineSolid();

    public abstract RenderLayer outlineSolid(boolean overlap);

    public abstract RenderLayer outlineTranslucent(Resource texture, boolean cull);

    public abstract RenderLayer glowingSolid(Resource texture);

    public abstract RenderLayer glowingSolid();

    public abstract RenderLayer glowingTranslucent(Resource texture);

    public abstract RenderLayer additive();

    public abstract RenderLayer glowingTranslucent();

    public abstract RenderLayer itemPartialSolid();

    public abstract RenderLayer itemPartialTranslucent();

    public abstract RenderLayer fluid();

}