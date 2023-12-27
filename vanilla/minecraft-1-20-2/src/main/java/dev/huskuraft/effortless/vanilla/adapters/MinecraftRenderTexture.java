package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.RenderTexture;
import net.minecraft.client.renderer.RenderType;

public class MinecraftRenderTexture extends RenderTexture {

    private final RenderType reference;

    MinecraftRenderTexture(RenderType reference) {
        this.reference = reference;
    }

    public static RenderTexture fromMinecraftRenderType(RenderType renderTexture) {
        return new MinecraftRenderTexture(renderTexture);
    }

    public static RenderType toMinecraftRenderType(RenderTexture renderTexture) {
        return ((MinecraftRenderTexture) renderTexture).reference;
    }

}
