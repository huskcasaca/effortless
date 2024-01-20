package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.RenderType;
import dev.huskuraft.effortless.renderer.RenderTypes;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import dev.huskuraft.effortless.vanilla.renderer.OutlineRenderType;

public class MinecraftRenderTypes extends RenderTypes {

    @Override
    public RenderType gui() {
        return MinecraftRenderType.fromMinecraftRenderType(net.minecraft.client.renderer.RenderType.gui());
    }

    @Override
    public RenderType guiOverlay() {
        return MinecraftRenderType.fromMinecraftRenderType(net.minecraft.client.renderer.RenderType.guiOverlay());
    }

    @Override
    public RenderType guiTextHighlight() {
        return MinecraftRenderType.fromMinecraftRenderType(net.minecraft.client.renderer.RenderType.guiTextHighlight());
    }

    @Override
    public RenderType lines() {
        return MinecraftRenderType.fromMinecraftRenderType(BlockRenderType.EF_LINES);
    }

    @Override
    public RenderType planes() {
        return MinecraftRenderType.fromMinecraftRenderType(BlockRenderType.EF_PLANES);
    }

    @Override
    public RenderType solid(int color) {
        return MinecraftRenderType.fromMinecraftRenderType(BlockRenderType.blockPreview(color));
    }

    @Override
    public RenderType outlineSolid() {
        return MinecraftRenderType.fromMinecraftRenderType(OutlineRenderType.outlineSolid());
    }

    @Override
    public RenderType outlineSolid(boolean overlap) {
        return MinecraftRenderType.fromMinecraftRenderType(OutlineRenderType.outlineSolid(overlap));
    }

    @Override
    public RenderType outlineTranslucent(Resource texture, boolean cull) {
        return MinecraftRenderType.fromMinecraftRenderType(OutlineRenderType.outlineTranslucent(MinecraftResource.toMinecraftResource(texture), cull));
    }
}
