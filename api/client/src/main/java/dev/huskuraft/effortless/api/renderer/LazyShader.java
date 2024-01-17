package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.events.render.RegisterShader;

import java.io.IOException;

class LazyShader implements Shader {

    private Shader reference;
    private final Resource resource;
    private final VertexFormat vertexFormat;

    public LazyShader(Resource resource, VertexFormat vertexFormat) {
        this.resource = resource;
        this.vertexFormat = vertexFormat;
    }

    @Override
    public Object referenceValue() {
        return reference.referenceValue();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    @Override
    public void register(RegisterShader.ShadersSink sink) {
        try {
            sink.register(resource, vertexFormat, shader -> this.reference = shader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Uniform getUniform(String param) {
        return reference.getUniform(param);
    }

}
