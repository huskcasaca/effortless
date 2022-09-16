package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.Resource;

import java.awt.*;

public abstract class RenderStyleProvider {

    public static final Resource BLANK_TEXTURE_LOCATION = Resource.of("textures/misc/blank.png");
    public static final Resource CHECKERED_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard.png");
    public static final Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_highlight.png");

    public static final Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_thin_64.png");
    public static final Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_cutout.png");
    public static final Resource SELECTION_TEXTURE_LOCATION = Resource.of("textures/misc/selection_64.png");

    public abstract RenderStyle gui();

    public abstract RenderStyle guiTextHighlight();

    public abstract RenderStyle guiOverlay();

    public abstract RenderStyle lines();

    public abstract RenderStyle planes();

    public abstract RenderStyle solid(Color color);

    public abstract RenderStyle outlineSolid();

    public abstract RenderStyle outlineSolid(boolean overlap);

    public abstract RenderStyle outlineTranslucent(Resource texture, boolean cull);

}
