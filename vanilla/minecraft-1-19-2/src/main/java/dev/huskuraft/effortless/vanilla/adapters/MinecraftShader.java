package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.VertexFormat;

public interface MinecraftShader extends Shader {

    net.minecraft.client.renderer.ShaderInstance referenceValue();

    default MinecraftUniform getUniform(String param) {
        var uniform = referenceValue().getUniform(param);
        return uniform == null ? null : () -> uniform;
    }

    default VertexFormat getVertexFormat() {
        return () -> referenceValue().getVertexFormat();
    }

    default String getResource() {
        return referenceValue().getName();
    }

    default void register(RegisterShader.ShadersSink sink) {
        throw new UnsupportedOperationException();
    }

}
