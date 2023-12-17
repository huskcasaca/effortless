package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.Resource;

public abstract class RenderStyleProvider {

    public static final Resource BLANK_TEXTURE_LOCATION = Resource.of("textures/misc/blank.png");
    public static final Resource CHECKERED_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard.png");
    public static final Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_highlight.png");

    public static final Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_thin_64.png");
    public static final Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_cutout.png");
    public static final Resource SELECTION_TEXTURE_LOCATION = Resource.of("textures/misc/selection_64.png");

    public abstract RenderType gui();

    public abstract RenderType guiTextHighlight();

    public abstract RenderType guiOverlay();

    public abstract RenderType lines();

    public abstract RenderType planes();

    public abstract RenderType solid(int color);

    public abstract RenderType outlineSolid();

    public abstract RenderType outlineSolid(boolean overlap);

    public abstract RenderType outlineTranslucent(Resource texture, boolean cull);

}
