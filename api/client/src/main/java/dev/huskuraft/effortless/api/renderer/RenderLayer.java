package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

public interface RenderLayer extends PlatformReference {

    RenderLayer GUI = RenderLayer.createComposite("gui", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(RenderState.ShaderState.GUI_SHADER_STATE).setTransparencyState(RenderState.TransparencyState.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderState.DepthTestState.LEQUAL_DEPTH_TEST).create(false));;
    RenderLayer GUI_OVERLAY = RenderLayer.createComposite("gui_text_highlight", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(RenderState.ShaderState.GUI_TEXT_HIGHLIGHT_SHADER_STATE).setTransparencyState(RenderState.TransparencyState.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderState.DepthTestState.NEVER_DEPTH_TEST).setColorLogicState(RenderState.ColorLogicState.OR_REVERSE_COLOR_LOGIC).create(false));
    RenderLayer GUI_TEXT_HIGHLIGHT = RenderLayer.createComposite("gui_overlay", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(RenderState.ShaderState.GUI_OVERLAY_SHADER_STATE).setTransparencyState(RenderState.TransparencyState.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderState.DepthTestState.NEVER_DEPTH_TEST).setWriteMaskState(RenderState.WriteMaskState.COLOR_WRITE).create(false));;

    RenderLayer CUSTOM = createComposite("custom",
            VertexFormats.BLOCK,
            VertexFormats.Modes.TRIANGLES,
            0,
            true,
            true,
            RenderState.builder().setDepthTestState(RenderState.DepthTestState.EQUAL_DEPTH_TEST).create(false));


    static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state) {
        return RenderComponentFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }

    static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexMode, int bufferSize, CompositeRenderState state) {
        return RenderComponentFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, false, false, state);
    }
}
