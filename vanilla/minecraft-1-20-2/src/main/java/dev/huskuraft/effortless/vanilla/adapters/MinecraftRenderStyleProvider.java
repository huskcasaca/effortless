package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderStyleProvider;
import dev.huskuraft.effortless.renderer.RenderType;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import dev.huskuraft.effortless.vanilla.renderer.OutlineRenderType;

class MinecraftRenderStyleProvider extends RenderStyleProvider {

    @Override
    public RenderType gui() {
        return MinecraftClientAdapter.adapt(net.minecraft.client.renderer.RenderType.gui());
    }

    @Override
    public RenderType guiOverlay() {
        return MinecraftClientAdapter.adapt(net.minecraft.client.renderer.RenderType.guiOverlay());
    }

    @Override
    public RenderType guiTextHighlight() {
        return MinecraftClientAdapter.adapt(net.minecraft.client.renderer.RenderType.guiTextHighlight());
    }

    @Override
    public RenderType lines() {
        return MinecraftClientAdapter.adapt(BlockRenderType.EF_LINES);
    }

    @Override
    public RenderType planes() {
        return MinecraftClientAdapter.adapt(BlockRenderType.EF_PLANES);
    }

    @Override
    public RenderType solid(int color) {
        return MinecraftClientAdapter.adapt(BlockRenderType.blockPreview(color));
    }

    @Override
    public RenderType outlineSolid() {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineSolid());
    }

    @Override
    public RenderType outlineSolid(boolean overlap) {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineSolid(overlap));
    }

    @Override
    public RenderType outlineTranslucent(Resource texture, boolean cull) {
        return MinecraftClientAdapter.adapt(OutlineRenderType.outlineTranslucent(MinecraftClientAdapter.adapt(texture), cull));
    }
}
