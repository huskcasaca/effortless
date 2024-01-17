package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.renderer.BufferSource;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.VertexBuffer;
import net.minecraft.client.renderer.MultiBufferSource;

class MinecraftBufferSource implements BufferSource {

    private final MultiBufferSource.BufferSource reference;

    MinecraftBufferSource(MultiBufferSource.BufferSource reference) {
        this.reference = reference;
    }

    @Override
    public MultiBufferSource.BufferSource referenceValue() {
        return reference;
    }

    @Override
    public VertexBuffer getBuffer(RenderLayer renderLayer) {
        return MinecraftConvertor.fromPlatformVertexBuffer(reference.getBuffer(renderLayer.reference()));
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
