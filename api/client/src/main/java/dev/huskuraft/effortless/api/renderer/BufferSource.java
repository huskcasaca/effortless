package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface BufferSource extends PlatformReference {

    VertexBuffer getBuffer(RenderLayer renderLayer);

    void end();

}
