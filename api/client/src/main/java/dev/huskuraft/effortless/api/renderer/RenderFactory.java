package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.platform.ClientPlatform;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.DepthTestState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import dev.huskuraft.effortless.api.renderer.programs.ShaderState;

public interface RenderFactory {

    Resource BLANK_TEXTURE_LOCATION = Resource.of("textures/misc/blank.png");
    Resource CHECKERED_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard.png");
    Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_highlight.png");

    Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_thin_64.png");
    Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("textures/misc/checkerboard_cutout.png");
    Resource SELECTION_TEXTURE_LOCATION = Resource.of("textures/misc/selection_64.png");

    RenderLayer vanillaGui();

    RenderLayer vanillaGuiTextHighlight();

    RenderLayer vanillaGuiOverlay();

    RenderState.Builder getRenderStateBuilder();

    RenderLayer createCompositeRenderLayer(String name, VertexFormat vertexFormat, VertexMode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state);

    DepthTestState createDepthTestState(String name, int function);

    ShaderState createShaderState(String name, Shader shader);

    Shader getShader(Shaders shaders);

    VertexFormat getVertexFormat(VertexFormats formats);

    VertexMode getVertexMode(VertexModes modes);

    RenderFactory INSTANCE = ClientPlatform.INSTANCE.getRenderFactory();

}
