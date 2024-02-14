package dev.huskuraft.effortless.api.renderer;

import java.io.IOException;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Shader extends PlatformReference {

    static Shader lazy(ResourceLocation location, VertexFormat format) {
        return new LazyShader(location, format);
    }

    ResourceLocation getResource();

    VertexFormat getVertexFormat();

    default void register(RegisterShader.ShadersSink sink) {
        try {
            sink.register(getResource(), getVertexFormat(), shader -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Uniform getUniform(String param);

}
