package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Shader extends PlatformReference {

    default void register(RegisterShader.ShadersSink sink) {
        throw new UnsupportedOperationException();
    }

    static Shader lazy(String shaderResource, VertexFormat format) {
        return new LazyShader(shaderResource, format);
    }
}
