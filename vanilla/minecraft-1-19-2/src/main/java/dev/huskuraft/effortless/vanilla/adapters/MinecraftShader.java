package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.renderer.Shader;

@FunctionalInterface
public interface MinecraftShader extends Shader {

    net.minecraft.client.renderer.ShaderInstance referenceValue();

    default MinecraftUniform getUniform(String param) {
        var uniform = referenceValue().getUniform(param);
        return uniform == null ? null : () -> uniform;
    }
}
