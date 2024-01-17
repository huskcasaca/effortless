package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.io.IOException;

public interface Shader extends PlatformReference {

    static Shader lazy(Resource resource, VertexFormat format) {
        return new LazyShader(resource, format);
    }

    Resource getResource();

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
