package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.renderer.RenderTexture;
import dev.huskuraft.effortless.renderer.texture.RenderTextures;
import net.minecraft.client.renderer.RenderType;

import static dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderTexture.fromMinecraftRenderType;

public class MinecraftRenderTextures extends RenderTextures {

    @Override
    public RenderTexture gui() {
        return fromMinecraftRenderType(RenderType.gui());
    }

    @Override
    public RenderTexture guiOverlay() {
        return fromMinecraftRenderType(RenderType.guiOverlay());
    }

    @Override
    public RenderTexture guiTextHighlight() {
        return fromMinecraftRenderType(RenderType.guiTextHighlight());
    }

}
