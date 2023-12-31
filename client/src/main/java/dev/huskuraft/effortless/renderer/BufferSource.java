package dev.huskuraft.effortless.renderer;

public interface BufferSource {

    VertexBuffer getBuffer(RenderLayer renderLayer);

    void end();

}
