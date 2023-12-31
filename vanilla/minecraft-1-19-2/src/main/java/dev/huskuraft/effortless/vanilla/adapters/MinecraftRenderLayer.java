package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.RenderLayer;
import net.minecraft.client.renderer.RenderType;

public class MinecraftRenderLayer implements RenderLayer {

    private final RenderType reference;

    MinecraftRenderLayer(RenderType reference) {
        this.reference = reference;
    }

    public static RenderLayer fromMinecraftRenderLayer(RenderType renderLayer) {
        return new MinecraftRenderLayer(renderLayer);
    }

    public static RenderType toMinecraftRenderLayer(RenderLayer renderLayer) {
        return ((MinecraftRenderLayer) renderLayer).reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftRenderLayer renderLayer && reference.equals(renderLayer.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
