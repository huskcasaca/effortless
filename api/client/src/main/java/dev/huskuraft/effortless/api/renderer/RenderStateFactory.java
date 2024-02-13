package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.SafeServiceLoader;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

public interface RenderStateFactory {

    RenderLayer createCompositeRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexFormatMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state);

    CompositeRenderState createCompositeState(RenderState.TextureState textureState, RenderState.ShaderState shaderState, RenderState.TransparencyState transparencyState, RenderState.DepthTestState depthTestState, RenderState.CullState cullState, RenderState.LightmapState lightmapState, RenderState.OverlayState overlayState, RenderState.LayeringState layeringState, RenderState.OutputState outputState, RenderState.TexturingState texturingState, RenderState.WriteMaskState writeMaskState, RenderState.LineState lineState, RenderState.ColorLogicState colorLogicState, boolean affectOutline);

    RenderState createRenderState(String name, Runnable setupState, Runnable clearState);

    RenderState.TextureState createTextureState(String name, RenderState.TextureState.Texture texture);

    RenderState.ShaderState createShaderState(String name, Shader shader);

    RenderState.TransparencyState createTransparencyState(String name, RenderState.TransparencyState.Type type);

    RenderState.DepthTestState createDepthTestState(String name, int function);

    RenderState.CullState createCullState(String name, boolean cull);

    RenderState.LightmapState createLightmapState(String name, boolean lightmap);

    RenderState.OverlayState createOverlayState(String name, boolean overlay);

    RenderState.LayeringState createLayeringState(String name, RenderState.LayeringState.Type type);

    RenderState.OutputState createOutputState(String name, RenderState.OutputState.Target target);

    RenderState.TexturingState createTexturingState(String name, Runnable setupState, Runnable clearState);

    RenderState.OffsetTexturingState createOffsetTexturingState(String name, float offsetX, float offsetY);

    RenderState.WriteMaskState createWriteMaskState(String name, boolean writeColor, boolean writeDepth);

    RenderState.LineState createLineState(String name, Double width);

    RenderState.ColorLogicState createColorLogicState(String name, RenderState.ColorLogicState.Op op);

    Shader getShader(Shaders shaders);

    VertexFormat getVertexFormat(VertexFormats formats);

    VertexFormat.Mode getVertexFormatMode(VertexFormats.Modes modes);

    RenderStateFactory INSTANCE = SafeServiceLoader.load(RenderStateFactory.class).getFirst();

    static RenderStateFactory getInstance() {
        return INSTANCE;
    }

}
