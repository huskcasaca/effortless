package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.RenderType;

public class MinecraftRenderType extends RenderType {

    private final net.minecraft.client.renderer.RenderType reference;

    MinecraftRenderType(net.minecraft.client.renderer.RenderType reference) {
        this.reference = reference;
    }

    public static RenderType fromMinecraftRenderType(net.minecraft.client.renderer.RenderType renderType) {
        return new MinecraftRenderType(renderType);
    }

    public static net.minecraft.client.renderer.RenderType toMinecraftRenderType(RenderType renderType) {
        return ((MinecraftRenderType) renderType).reference;
    }

}
