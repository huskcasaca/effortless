package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.DepthTestState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

public interface RenderLayer extends PlatformReference {

    RenderLayer GUI = RenderFactory.INSTANCE.vanillaGui();

    RenderLayer CUSTOM = createComposite("custom",
            VertexFormats.BLOCK,
            VertexModes.TRIANGLES,
            0,
            true,
            true,
            RenderState.builder().setDepthTestState(DepthTestState.EQUAL_DEPTH_TEST).build());

    static RenderLayer createComposite(String name, VertexFormat vertexFormat, VertexMode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state) {
        return RenderFactory.INSTANCE.createCompositeRenderLayer(name, vertexFormat, vertexMode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }
}
