package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.VertexFormat;

@FunctionalInterface
public interface MinecraftShader extends Shader {

    net.minecraft.client.renderer.ShaderInstance referenceValue();

    @Override
    default Resource getResource() {
        return Resource.vanilla(referenceValue().getName());
    }

    default VertexFormat getVertexFormat() {
        return () -> referenceValue().getVertexFormat();
    }

    default MinecraftUniform getUniform(String param) {
        return new MinecraftUniform(referenceValue().getUniform(param));
    }
}
