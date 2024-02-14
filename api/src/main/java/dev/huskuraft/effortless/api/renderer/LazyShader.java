package dev.huskuraft.effortless.api.renderer;

import java.io.IOException;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.events.render.RegisterShader;

class LazyShader implements Shader {

    private Shader reference;
    private final ResourceLocation location;
    private final VertexFormat vertexFormat;

    public LazyShader(ResourceLocation location, VertexFormat vertexFormat) {
        this.location = location;
        this.vertexFormat = vertexFormat;
    }

    @Override
    public Object referenceValue() {
        return reference.referenceValue();
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
            sink.register(location, vertexFormat, shader -> this.reference = shader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Uniform getUniform(String param) {
        return reference.getUniform(param);
    }

}
