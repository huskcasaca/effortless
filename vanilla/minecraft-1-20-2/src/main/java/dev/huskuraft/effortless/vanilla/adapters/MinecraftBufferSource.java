package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.renderer.BufferSource;
import dev.huskuraft.effortless.renderer.RenderLayer;
import dev.huskuraft.effortless.renderer.VertexBuffer;
import net.minecraft.client.renderer.MultiBufferSource;

public class MinecraftBufferSource implements BufferSource {

    private final MultiBufferSource.BufferSource reference;

    MinecraftBufferSource(MultiBufferSource.BufferSource reference) {
        this.reference = reference;
    }

    public static BufferSource fromMinecraftBufferSource(MultiBufferSource.BufferSource bufferSource) {
        return new MinecraftBufferSource(bufferSource);
    }

    public static MultiBufferSource.BufferSource toMinecraftBufferSource(BufferSource bufferSource) {
        return ((MinecraftBufferSource) bufferSource).reference;
    }

    @Override
    public VertexBuffer getBuffer(RenderLayer renderLayer) {
        return new MinecraftVertexBuffer(reference.getBuffer(MinecraftRenderLayer.toMinecraftRenderLayer(renderLayer)));
    }

    @Override
    public void end() {
        reference.endBatch();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBufferSource bufferSource && reference.equals(bufferSource.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
