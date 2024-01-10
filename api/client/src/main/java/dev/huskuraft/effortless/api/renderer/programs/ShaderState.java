package dev.huskuraft.effortless.api.renderer.programs;

import dev.huskuraft.effortless.api.renderer.RenderFactory;
import dev.huskuraft.effortless.api.renderer.Shader;

public interface ShaderState extends RenderState {

    static ShaderState create(String name, Shader shader) {
        return RenderFactory.INSTANCE.createShaderState(name, shader);
    }

}
