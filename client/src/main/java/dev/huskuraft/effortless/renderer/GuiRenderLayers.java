package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.VertexFormats;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

public class GuiRenderLayers {

    protected static final RenderLayer GUI = RenderLayer.createComposite("gui",
            VertexFormats.POSITION_COLOR,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(RenderState.ShaderState.GUI_SHADER_STATE)
                    .setTransparencyState(RenderState.TransparencyState.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderState.DepthTestState.LEQUAL_DEPTH_TEST)
                    .create(false));

//    protected static final RenderLayer GUI_OVERLAY = RenderLayer.createComposite("gui_overlay", VertexFormats.POSITION_COLOR, VertexModes.QUADS, 256,RenderState.builder().setShaderState(RENDERTYPE_GUI_OVERLAY_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
//    protected static final RenderLayer GUI_TEXT_HIGHLIGHT = RenderLayer.createComposite("gui_text_highlight", VertexFormats.POSITION_COLOR, VertexModes.QUADS, 256,RenderState.builder().setShaderState(RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST)/*.setColorLogicState(OR_REVERSE_COLOR_LOGIC)*/.createCompositeState(false));


}
