package dev.huskuraft.effortless.api.renderer.programs;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.RenderFactory;

public interface RenderState extends PlatformReference {

    interface Builder {

        Builder setDepthTestState(DepthTestState shard);
        Builder setShaderState(ShaderState shard);

        CompositeRenderState build();

    }

    static Builder builder() {
        return RenderFactory.INSTANCE.getRenderStateBuilder();
    }
}
