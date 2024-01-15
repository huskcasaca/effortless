package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.events.render.RegisterShader;

import java.io.IOException;

class LazyShader implements Shader {

    private Object reference;
    private final String shaderResource;
    private final VertexFormat format;

    public LazyShader(String shaderResource, VertexFormat format) {
        this.shaderResource = shaderResource;
        this.format = format;
    }

    @Override
    public Object referenceValue() {
        return reference;
    }

    @Override
    public void register(RegisterShader.ShadersSink sink) {
        try {
            sink.register(shaderResource, format, this::setReferenceValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setReferenceValue(Object value) {
        this.reference = value;
    }
}
