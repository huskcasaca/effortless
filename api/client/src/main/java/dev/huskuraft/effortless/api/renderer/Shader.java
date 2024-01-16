package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.platform.PlatformReference;

import javax.annotation.Nullable;

public interface Shader extends PlatformReference {

    static Shader lazy(String shaderResource, VertexFormat format) {
        return new LazyShader(shaderResource, format);
    }

    default void register(RegisterShader.ShadersSink sink) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    default Uniform getUniform(String param) {
        throw new UnsupportedOperationException();
    }

}
