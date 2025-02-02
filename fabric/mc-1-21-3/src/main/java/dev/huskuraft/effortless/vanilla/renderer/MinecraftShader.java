package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Uniform;
import dev.huskuraft.effortless.api.renderer.VertexFormat;
import dev.huskuraft.effortless.vanilla.core.MinecraftResourceLocation;

public record MinecraftShader(
        net.minecraft.client.renderer.ShaderProgram refs
) implements Shader {

    @Override
    public ResourceLocation getResource() {
        return MinecraftResourceLocation.ofNullable(refs.configId());
    }

    public VertexFormat getVertexFormat() {
        return refs::vertexFormat;
    }

    public Uniform getUniform(String param) {
        return new MinecraftUniform(net.minecraft.client.Minecraft.getInstance().getShaderManager().getProgram(refs).getUniform(param));
    }
}
