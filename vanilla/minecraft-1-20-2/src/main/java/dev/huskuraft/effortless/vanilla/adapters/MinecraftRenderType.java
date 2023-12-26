package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.RenderType;

public class MinecraftRenderType extends RenderType {

    private final net.minecraft.client.renderer.RenderType renderType;

    MinecraftRenderType(net.minecraft.client.renderer.RenderType renderType) {
        this.renderType = renderType;
    }

    public net.minecraft.client.renderer.RenderType getRef() {
        return renderType;
    }

}
