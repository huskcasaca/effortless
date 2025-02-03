package dev.huskuraft.effortless.api.renderer;

import java.io.IOException;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.events.render.RegisterShader;

class LazyShader implements Shader {

    private final ResourceLocation location;
    private final VertexFormat vertexFormat;
    private Shader reference;

    public LazyShader(ResourceLocation location, VertexFormat vertexFormat) {
        this.location = location;
        this.vertexFormat = vertexFormat;
    }

    @Override
    public Object refs() {
        return reference.refs();
    }

    @Override
    public ResourceLocation getResource() {
        return location;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    @Override
    public void register(RegisterShader.ShadersSink sink) {
        try {
            sink.register(getResource(), getVertexFormat(), shader -> this.reference = shader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Uniform getUniform(String param) {
        return reference.getUniform(param);
    }

}
