package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

public interface RenderLayer extends PlatformReference {

    RenderLayer GUI = RenderFactory.INSTANCE.getGuiRenderLayer();

    RenderLayer CUSTOM = createComposite("custom",
            VertexFormats.BLOCK,
            VertexFormats.Modes.TRIANGLES,
            0,
            true,
            true,
            RenderState.builder().setDepthTestState(RenderState.DepthTestState.EQUAL_DEPTH_TEST).create());

    static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexFormat.Mode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state) {
        return RenderFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }
}
