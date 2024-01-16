package dev.huskuraft.effortless.api.renderer.programs;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.RenderFactory;
import dev.huskuraft.effortless.api.renderer.RenderLayers;
import dev.huskuraft.effortless.api.renderer.Shader;

public interface RenderState extends PlatformReference {

    static CompositeBuilder builder() {
        return new CompositeBuilder.Default();
    }

    abstract class CompositeBuilder {

        public abstract CompositeBuilder setTextureState(TextureState state);
        public abstract CompositeBuilder setShaderState(ShaderState state);
        public abstract CompositeBuilder setTransparencyState(TransparencyState state);
        public abstract CompositeBuilder setDepthTestState(DepthTestState state);
        public abstract CompositeBuilder setCullState(CullState state);
        public abstract CompositeBuilder setLightmapState(LightmapState state);
        public abstract CompositeBuilder setOverlayState(OverlayState state);
        public abstract CompositeBuilder setLayeringState(LayeringState state);
        public abstract CompositeBuilder setOutputState(OutputState state);
        public abstract CompositeBuilder setTexturingState(TexturingState state);
        public abstract CompositeBuilder setWriteMaskState(WriteMaskState state);
        public abstract CompositeBuilder setLineState(LineState state);
        public abstract CompositeBuilder setColorLogicState(ColorLogicState state);
        public abstract CompositeRenderState create(boolean affectOutline);


        private static class Default extends CompositeBuilder {

            private TextureState textureState = RenderLayers.NO_TEXTURE; // RenderStateShard.NO_TEXTURE;
            private ShaderState shaderState = RenderLayers.NO_SHADER_STATE; // RenderStateShard.NO_SHADER;
            private TransparencyState transparencyState = RenderLayers.NO_TRANSPARENCY; // RenderStateShard.NO_TRANSPARENCY;
            private DepthTestState depthTestState = RenderLayers.LEQUAL_DEPTH_TEST; // RenderStateShard.LEQUAL_DEPTH_TEST;
            private CullState cullState = RenderLayers.CULL; // RenderStateShard.CULL;
            private LightmapState lightmapState = RenderLayers.NO_LIGHTMAP; // RenderStateShard.NO_LIGHTMAP;
            private OverlayState overlayState = RenderLayers.NO_OVERLAY; // RenderStateShard.NO_OVERLAY;
            private LayeringState layeringState = RenderLayers.NO_LAYERING; // RenderStateShard.NO_LAYERING;
            private OutputState outputState = RenderLayers.NO_TARGET; // RenderStateShard.MAIN_TARGET;
            private TexturingState texturingState = RenderLayers.NO_TEXTURING; // RenderStateShard.DEFAULT_TEXTURING;
            private WriteMaskState writeMaskState = RenderLayers.COLOR_DEPTH_WRITE;
            private LineState lineState = RenderLayers.DEFAULT_WIDTH; // RenderStateShard.DEFAULT_LINE;
            private ColorLogicState colorLogicState = RenderLayers.NO_COLOR_LOGIC; // RenderStateShard.NO_COLOR_LOGIC;


//                this.textureState = RenderStateShard.NO_TEXTURE;
//                this.shaderState = RenderStateShard.NO_SHADER;
//                this.transparencyState = RenderStateShard.NO_TRANSPARENCY;
//                this.depthTestState = RenderStateShard.LEQUAL_DEPTH_TEST;
//                this.cullState = RenderStateShard.CULL;
//                this.lightmapState = RenderStateShard.NO_LIGHTMAP;
//                this.overlayState = RenderStateShard.NO_OVERLAY;
//                this.layeringState = RenderStateShard.NO_LAYERING;
//                this.outputState = RenderStateShard.MAIN_TARGET;
//                this.texturingState = RenderStateShard.DEFAULT_TEXTURING;
//                this.writeMaskState = RenderStateShard.COLOR_DEPTH_WRITE;
//                this.lineState = RenderStateShard.DEFAULT_LINE;
//                this.colorLogicState = RenderStateShard.NO_COLOR_LOGIC;

            @Override
            public CompositeBuilder setTextureState(TextureState state) {
                this.textureState = state;
                return this;
            }

            @Override
            public CompositeBuilder setShaderState(ShaderState state) {
                this.shaderState = state;
                return this;
            }

            @Override
            public CompositeBuilder setTransparencyState(TransparencyState state) {
                this.transparencyState = state;
                return this;
            }

            @Override
            public CompositeBuilder setDepthTestState(DepthTestState state) {
                this.depthTestState = state;
                return this;
            }

            @Override
            public CompositeBuilder setCullState(CullState state) {
                this.cullState = state;
                return this;
            }

            @Override
            public CompositeBuilder setLightmapState(LightmapState state) {
                this.lightmapState = state;
                return this;
            }

            @Override
            public CompositeBuilder setOverlayState(OverlayState state) {
                this.overlayState = state;
                return this;
            }

            @Override
            public CompositeBuilder setLayeringState(LayeringState state) {
                this.layeringState = state;
                return this;
            }

            @Override
            public CompositeBuilder setOutputState(OutputState state) {
                this.outputState = state;
                return this;
            }

            @Override
            public CompositeBuilder setTexturingState(TexturingState state) {
                this.texturingState = state;
                return this;
            }

            @Override
            public CompositeBuilder setWriteMaskState(WriteMaskState state) {
                this.writeMaskState = state;
                return this;
            }

            @Override
            public CompositeBuilder setLineState(LineState state) {
                this.lineState = state;
                return this;
            }

            @Override
            public CompositeBuilder setColorLogicState(ColorLogicState state) {
                this.colorLogicState = state;
                return this;
            }

            @Override
            public CompositeRenderState create(boolean affectOutline) {
                return RenderFactory.INSTANCE.createCompositeState(textureState, shaderState, transparencyState, depthTestState, cullState, lightmapState, overlayState, layeringState, outputState, texturingState, writeMaskState, lineState, colorLogicState, affectOutline);
            }
        }
    }

    interface TextureState extends RenderState {

        record Texture(
                Resource resource,
                boolean blur,
                boolean mipmap
        ) {
        }

        static TextureState create(String name, Texture texture) {
            return RenderFactory.INSTANCE.createTextureState(name, texture);
        }
    }
    interface TransparencyState extends RenderState {

        static TransparencyState create(String name, Type type) {
            return RenderFactory.INSTANCE.createTransparencyState(name, type);
        }

        enum Type {
            NO,
            ADDITIVE,
            LIGHTNING,
            GLINT,
            CRUMBLING,
            TRANSLUCENT,
        }
    }
    interface ShaderState extends RenderState {

        static ShaderState create(String name, Shader shader) {
            return RenderFactory.INSTANCE.createShaderState(name, shader);
        }

    }
    interface DepthTestState extends RenderState {

        static DepthTestState create(String name, int function) {
            return RenderFactory.INSTANCE.createDepthTestState(name, function);
        }
    }
    interface CullState extends RenderState {

        static CullState create(String name, boolean cull) {
            return RenderFactory.INSTANCE.createCullState(name, cull);
        }
    }
    interface LightmapState extends RenderState {

        static LightmapState create(String name, boolean lightmap) {
            return RenderFactory.INSTANCE.createLightmapState(name, lightmap);
        }
    }
    interface OverlayState extends RenderState {

        static OverlayState create(String name, boolean overlay) {
            return RenderFactory.INSTANCE.createOverlayState(name, overlay);
        }
    }
    interface LayeringState extends RenderState {
        enum Type {
            NO,
            POLYGON_OFFSET,
            VIEW_OFFSET_Z
        }

        static LayeringState create(String name, Type type) {
            return RenderFactory.INSTANCE.createLayeringState(name, type);
        }
    }
    interface OutputState extends RenderState {
        enum Target {
            NO,
            OUTLINE,
            TRANSLUCENT,
            PARTICLES,
            WEATHER,
            CLOUDS,
            ITEM_ENTITY,
        }

        static OutputState create(String name, Target target) {
            return RenderFactory.INSTANCE.createOutputState(name, target);
        }
    }
    interface TexturingState extends RenderState {

        static TexturingState create(String name, Runnable setupState, Runnable clearState) {
            return RenderFactory.INSTANCE.createTexturingState(name, setupState, clearState);
        }
    }

    interface OffsetTexturingState extends TexturingState {

        static OffsetTexturingState create(String name, float offsetX, float offsetY) {
            return RenderFactory.INSTANCE.createOffsetTexturingState(name, offsetX, offsetY);
        }
    }
    interface WriteMaskState extends RenderState {

        static WriteMaskState create(String name, boolean writeColor, boolean writeDepth) {
            return RenderFactory.INSTANCE.createWriteMaskState(name, writeColor, writeDepth);
        }
    }
    interface LineState extends RenderState {

        static LineState create(String name, Double width) {
            return RenderFactory.INSTANCE.createLineState(name, width);
        }
    }
    interface ColorLogicState extends RenderState {
        enum Op {
            NO_LOGIC,
            OR_REVERSE_LOGIC;
        }

        static ColorLogicState create(String name, Op op) {
            return RenderFactory.INSTANCE.createColorLogicState(name, op);
        }
    }

}
