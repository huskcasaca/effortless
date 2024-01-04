package dev.huskuraft.effortless.api.renderer.texture;

import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.core.Resource;

public abstract class RenderLayers {

    public static final Resource BLANK_TEXTURE_LOCATION = Resource.of("textures/misc/blank.png");
    public static final Resource CHECKERED_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard.png");
    public static final Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_highlight.png");

    public static final Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_thin_64.png");
    public static final Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_cutout.png");
    public static final Resource SELECTION_TEXTURE_LOCATION = Resource.of("textures/misc/selection_64.png");

    public abstract RenderLayer gui();

    public abstract RenderLayer guiTextHighlight();

    public abstract RenderLayer guiOverlay();

}
