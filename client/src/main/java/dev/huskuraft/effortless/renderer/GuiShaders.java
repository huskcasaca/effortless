package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.VertexFormats;

public interface GuiShaders {

    // TODO: 15/1/24 move to Shaders
    Shader GUI = Shader.lazy( "rendertype_gui", VertexFormats.POSITION_COLOR);
    Shader GUI_OVERLAY = Shader.lazy( "rendertype_gui_overlay", VertexFormats.POSITION_COLOR);
    Shader GUI_TEXT_HIGHLIGHT = Shader.lazy( "rendertype_gui_text_highlight", VertexFormats.POSITION_COLOR);

}
