package dev.huskuraft.effortless.api.renderer;

public interface BufferSource {

    VertexBuffer getBuffer(RenderLayer renderLayer);

    void end();

}
