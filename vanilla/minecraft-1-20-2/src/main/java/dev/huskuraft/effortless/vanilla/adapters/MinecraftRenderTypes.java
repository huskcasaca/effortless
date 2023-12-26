package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderType;
import dev.huskuraft.effortless.renderer.RenderTypes;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import dev.huskuraft.effortless.vanilla.renderer.OutlineRenderType;

public class MinecraftRenderTypes extends RenderTypes {

    @Override
    public RenderType gui() {
        return new MinecraftRenderType(net.minecraft.client.renderer.RenderType.gui());
    }

    @Override
    public RenderType guiOverlay() {
        return new MinecraftRenderType(net.minecraft.client.renderer.RenderType.guiOverlay());
    }

    @Override
    public RenderType guiTextHighlight() {
        return new MinecraftRenderType(net.minecraft.client.renderer.RenderType.guiTextHighlight());
    }

    @Override
    public RenderType lines() {
        return new MinecraftRenderType(BlockRenderType.EF_LINES);
    }

    @Override
    public RenderType planes() {
        return new MinecraftRenderType(BlockRenderType.EF_PLANES);
    }

    @Override
    public RenderType solid(int color) {
        return new MinecraftRenderType(BlockRenderType.blockPreview(color));
    }

    @Override
    public RenderType outlineSolid() {
        return new MinecraftRenderType(OutlineRenderType.outlineSolid());
    }

    @Override
    public RenderType outlineSolid(boolean overlap) {
        return new MinecraftRenderType(OutlineRenderType.outlineSolid(overlap));
    }

    @Override
    public RenderType outlineTranslucent(Resource texture, boolean cull) {
        return new MinecraftRenderType(OutlineRenderType.outlineTranslucent(MinecraftClientAdapter.adapt(texture), cull));
    }
}
