package dev.huskcasaca.effortless;

import dev.huskcasaca.effortless.render.BlockPreviewRenderer;
import net.minecraft.client.Minecraft;

public interface MinecraftRendererProvider {

    BlockPreviewRenderer getBlockPreviewRenderer();

    static BlockPreviewRenderer getPreviewRenderer() {
        return ((MinecraftRendererProvider) Minecraft.getInstance()).getBlockPreviewRenderer();
    }

}
