package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.events.render.RegisterShader;

import java.io.IOException;

class LazyShader implements Shader {

    private Shader reference;
    private final String shaderResource;
    private final VertexFormat format;

    public LazyShader(String shaderResource, VertexFormat format) {
        this.shaderResource = shaderResource;
        this.format = format;
    }

    @Override
    public Object referenceValue() {
        return reference.referenceValue();
    }

    @Override
    public void register(RegisterShader.ShadersSink sink) {
        try {
            sink.register(shaderResource, format, shader -> this.reference = shader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Uniform getUniform(String param) {
        return reference.getUniform(param);
    }

}
