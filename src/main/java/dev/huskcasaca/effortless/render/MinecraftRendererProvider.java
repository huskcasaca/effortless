package dev.huskcasaca.effortless.render;

import net.minecraft.client.Minecraft;

public interface MinecraftRendererProvider {

    BlockPreviewRenderer getBlockPreviewRenderer();

    static BlockPreviewRenderer getPreviewRenderer() {
        return ((MinecraftRendererProvider) Minecraft.getInstance()).getBlockPreviewRenderer();
    }

}
