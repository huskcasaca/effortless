package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.renderer.RenderLayer;
import dev.huskuraft.effortless.renderer.texture.RenderLayers;
import net.minecraft.client.renderer.RenderType;

import static dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderLayer.fromMinecraftRenderLayer;

public class MinecraftRenderLayers extends RenderLayers {

    @Override
    public RenderLayer gui() {
        return fromMinecraftRenderLayer(RenderType.gui());
    }

    @Override
    public RenderLayer guiOverlay() {
        return fromMinecraftRenderLayer(RenderType.guiOverlay());
    }

    @Override
    public RenderLayer guiTextHighlight() {
        return fromMinecraftRenderLayer(RenderType.guiTextHighlight());
    }

}
