package dev.huskuraft.effortless.renderer.texture;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderTexture;

public abstract class RenderTextures {

    public static final Resource BLANK_TEXTURE_LOCATION = Resource.of("textures/misc/blank.png");
    public static final Resource CHECKERED_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard.png");
    public static final Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_highlight.png");

    public static final Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_thin_64.png");
    public static final Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_cutout.png");
    public static final Resource SELECTION_TEXTURE_LOCATION = Resource.of("textures/misc/selection_64.png");

    public abstract RenderTexture gui();

    public abstract RenderTexture guiTextHighlight();

    public abstract RenderTexture guiOverlay();

}
