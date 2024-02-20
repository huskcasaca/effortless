package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Uniform;
import dev.huskuraft.effortless.api.renderer.VertexFormat;

public class MinecraftShader implements Shader {

    private final net.minecraft.client.renderer.ShaderInstance reference;

    public MinecraftShader(net.minecraft.client.renderer.ShaderInstance reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.client.renderer.ShaderInstance referenceValue() {
        return reference;
    }

    @Override
    public ResourceLocation getResource() {
        return ResourceLocation.vanilla(reference.getName());
    }

    public VertexFormat getVertexFormat() {
        return () -> reference.getVertexFormat();
    }

    public Uniform getUniform(String param) {
        return new MinecraftUniform(reference.getUniform(param));
    }
}
