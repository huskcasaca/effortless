package dev.huskuraft.effortless.api.events.render;


import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.VertexFormat;

import java.io.IOException;
import java.util.function.Consumer;

public interface RegisterShader {

    void onRegisterShader(ShadersSink sink);

    @FunctionalInterface
    interface ShadersSink {
        void register(Resource resource, VertexFormat format, Consumer<Shader> consumer) throws IOException;
    }

}
