package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderStyle;
import dev.huskuraft.effortless.renderer.RenderStyleProvider;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import dev.huskuraft.effortless.vanilla.renderer.OutlineRenderType;
import net.minecraft.client.renderer.RenderType;

import java.awt.*;

class MinecraftRenderStyleProvider extends RenderStyleProvider {

    @Override
    public RenderStyle gui() {
        return MinecraftClientAdapter.adapt(RenderType.gui());
    }

    @Override
    public RenderStyle guiOverlay() {
        return MinecraftClientAdapter.adapt(RenderType.guiOverlay());
    }

    @Override
    public RenderStyle guiTextHighlight() {
        return MinecraftClientAdapter.adapt(RenderType.guiTextHighlight());
    }

    @Override
    public RenderStyle lines() {
        return MinecraftClientAdapter.adapt(BlockRenderType.EF_LINES);
    }

    @Override
    public RenderStyle planes() {
        return MinecraftClientAdapter.adapt(BlockRenderType.EF_PLANES);
    }

    @Override
    public RenderStyle solid(Color color) {
        return MinecraftClientAdapter.adapt(BlockRenderType.blockPreview(color));
    }

    @Override
    public RenderStyle outlineSolid() {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineSolid());
    }

    @Override
    public RenderStyle outlineSolid(boolean overlap) {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineSolid(overlap));
    }

    @Override
    public RenderStyle outlineTranslucent(Resource texture, boolean cull) {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineTranslucent(MinecraftClientAdapter.adapt(texture), cull));
    }
}
