package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.renderer.BufferSource;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.VertexBuffer;
import net.minecraft.client.renderer.MultiBufferSource;

public class MinecraftBufferSource implements BufferSource {

    private final MultiBufferSource.BufferSource reference;

    public MinecraftBufferSource(MultiBufferSource.BufferSource reference) {
        this.reference = reference;
    }

    @Override
    public MultiBufferSource.BufferSource referenceValue() {
        return reference;
    }

    @Override
    public VertexBuffer getBuffer(RenderLayer renderLayer) {
        return new MinecraftVertexBuffer(reference.getBuffer(renderLayer.reference()));
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
