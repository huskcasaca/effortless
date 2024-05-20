package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.renderer.BufferSource;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.VertexBuffer;
import net.minecraft.client.renderer.MultiBufferSource;

public record MinecraftBufferSource(
        MultiBufferSource.BufferSource refs
) implements BufferSource {

    @Override
    public VertexBuffer getBuffer(RenderLayer renderLayer) {
        return new MinecraftVertexBuffer(refs.getBuffer(renderLayer.reference()));
    }

    @Override
    public void end() {
        refs.endBatch();
    }

}
