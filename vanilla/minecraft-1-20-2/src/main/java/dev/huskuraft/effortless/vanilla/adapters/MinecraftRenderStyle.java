package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.RenderStyle;
import net.minecraft.client.renderer.RenderType;

class MinecraftRenderStyle extends RenderStyle {

    private final RenderType renderType;

    MinecraftRenderStyle(RenderType renderType) {
        this.renderType = renderType;
    }

    public RenderType getRef() {
        return renderType;
    }

}
