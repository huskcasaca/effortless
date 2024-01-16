package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.api.platform.PlatformResource;
import dev.huskuraft.effortless.api.renderer.RenderFactory;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Shaders;

public interface CompatibleRenderFactory extends RenderFactory {

    @Override
    default Shader getShader(Shaders shaders) {
        return switch (shaders) {
            case GUI ->                 GuiShaders.GUI;
            case GUI_OVERLAY ->         GuiShaders.GUI_OVERLAY;
            case GUI_TEXT_HIGHLIGHT ->  GuiShaders.GUI_TEXT_HIGHLIGHT;
            default ->                  PlatformResource::unavailable;
        };
    }

}
