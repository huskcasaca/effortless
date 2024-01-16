package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;

public interface RenderLayer extends PlatformReference {

        static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state) {
        return RenderFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }

    static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexMode, int bufferSize, CompositeRenderState state) {
        return RenderFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, false, false, state);
    }
}
