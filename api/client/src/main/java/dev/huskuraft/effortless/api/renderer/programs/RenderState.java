package dev.huskuraft.effortless.api.renderer.programs;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.RenderFactory;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Shaders;
import org.lwjgl.opengl.GL11;

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

            private TextureState textureState = TextureState.NO_TEXTURE;
            private ShaderState shaderState = ShaderState.NO_SHADER;
            private TransparencyState transparencyState = TransparencyState.NO_TRANSPARENCY;
            private DepthTestState depthTestState = DepthTestState.ALWAYS_DEPTH_TEST; // RenderStateShard.LEQUAL_DEPTH_TEST;
            private CullState cullState = CullState.CULL; // RenderStateShard.CULL;
            private LightmapState lightmapState = LightmapState.NO_LIGHTMAP;
            private OverlayState overlayState = OverlayState.NO_OVERLAY;
            private LayeringState layeringState = LayeringState.NO_LAYERING;
            private OutputState outputState = OutputState.NO_TARGET;
            private TexturingState texturingState = TexturingState.NO_TEXTURING; // RenderStateShard.DEFAULT_TEXTURING;
            private WriteMaskState writeMaskState = WriteMaskState.COLOR_DEPTH_WRITE;
            private LineState lineState = LineState.DEFAULT_WIDTH; // RenderStateShard.DEFAULT_LINE;
            private ColorLogicState colorLogicState = null;


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

        TextureState NO_TEXTURE = create("none");

        record Texture(
                Resource resource,
                boolean blur,
                boolean mipmap
        ) {
        }

        static TextureState create(String name) {
            return RenderFactory.INSTANCE.createTextureState(name);
        }

        static TextureState create(String name, Texture texture) {
            return RenderFactory.INSTANCE.createTextureState(name, texture);
        }
    }
    interface TransparencyState extends RenderState {

        TransparencyState NO_TRANSPARENCY = create("none", Type.NO);
        TransparencyState ADDITIVE_TRANSPARENCY = create("additive", Type.ADDITIVE);
        TransparencyState LIGHTNING_TRANSPARENCY = create("lightning", Type.LIGHTNING);
        TransparencyState GLINT_TRANSPARENCY = create("glint", Type.GLINT);
        TransparencyState CRUMBLING_TRANSPARENCY = create("crumbling", Type.CRUMBLING);
        TransparencyState TRANSLUCENT_TRANSPARENCY = create("translucent", Type.TRANSLUCENT);

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

        ShaderState NO_SHADER = create("none", null);
        ShaderState GUI_SHADER_STATE = create("gui", Shaders.GUI);

        static ShaderState create(String name, Shader shader) {
            return RenderFactory.INSTANCE.createShaderState(name, shader);
        }

    }
    interface DepthTestState extends RenderState {

        DepthTestState NEVER_DEPTH_TEST = create("never", GL11.GL_NEVER);
        DepthTestState LESS_DEPTH_TEST = create("less", GL11.GL_LESS);
        DepthTestState EQUAL_DEPTH_TEST = create("equal", GL11.GL_EQUAL);
        DepthTestState LEQUAL_DEPTH_TEST = create("lequal", GL11.GL_LEQUAL);
        DepthTestState GREATER_DEPTH_TEST = create("greater", GL11.GL_GREATER);
        DepthTestState NOTEQUAL_DEPTH_TEST = create("notequal", GL11.GL_NOTEQUAL);
        DepthTestState GEQUAL_DEPTH_TEST = create("gequal", GL11.GL_GEQUAL);
        DepthTestState ALWAYS_DEPTH_TEST = create("always", GL11.GL_ALWAYS);

        static DepthTestState create(String name, int function) {
            return RenderFactory.INSTANCE.createDepthTestState(name, function);
        }
    }
    interface CullState extends RenderState {

        CullState CULL = create("cull", true);
        CullState DISABLE_CULL = create("disable_cull", false);

        static CullState create(String name, boolean cull) {
            return RenderFactory.INSTANCE.createCullState(name, cull);
        }
    }
    interface LightmapState extends RenderState {

        LightmapState LIGHTMAP = create("lightmap", true);
        LightmapState NO_LIGHTMAP = create("no_lightmap", false);

        static LightmapState create(String name, boolean lightmap) {
            return RenderFactory.INSTANCE.createLightmapState(name, lightmap);
        }
    }
    interface OverlayState extends RenderState {

        OverlayState OVERLAY = create("overlay", true);
        OverlayState NO_OVERLAY = create("no_overlay", false);

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

        LayeringState NO_LAYERING = create("none", Type.NO);
        LayeringState POLYGON_OFFSET_LAYERING = create("polygon_offset", Type.POLYGON_OFFSET);
        LayeringState VIEW_OFFSET_Z_LAYERING = create("view_offset_z", Type.VIEW_OFFSET_Z);

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

        OutputState NO_TARGET = create("main", Target.NO);
        OutputState OUTLINE_TARGET = create("outline", Target.OUTLINE);
        OutputState TRANSLUCENT_TARGET = create("translucent", Target.TRANSLUCENT);
        OutputState PARTICLES_TARGET = create("particles", Target.PARTICLES);
        OutputState WEATHER_TARGET = create("weather", Target.WEATHER);
        OutputState CLOUDS_TARGET = create("clouds", Target.CLOUDS);
        OutputState ITEM_ENTITY_TARGET = create("item_entity", Target.ITEM_ENTITY);

        static OutputState create(String name, Target target) {
            return RenderFactory.INSTANCE.createOutputState(name, target);
        }
    }
    interface TexturingState extends RenderState {

        TexturingState NO_TEXTURING = create("none", () -> {}, () -> {});
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

        WriteMaskState COLOR_DEPTH_WRITE = create("color_depth_write", true, true);
        WriteMaskState COLOR_WRITE = create("color_write", true, false);
        WriteMaskState DEPTH_WRITE = create("depth_write", false, true);

        static WriteMaskState create(String name, boolean writeColor, boolean writeDepth) {
            return RenderFactory.INSTANCE.createWriteMaskState(name, writeColor, writeDepth);
        }
    }
    interface LineState extends RenderState {

        LineState NO_WIDTH = create("no_width");
        LineState DEFAULT_WIDTH = create("default_width", 1.0f);

        static LineState create(String name) {
            return RenderFactory.INSTANCE.createLineState(name);
        }
        static LineState create(String name, double width) {
            return RenderFactory.INSTANCE.createLineState(name, width);
        }
    }
    interface ColorLogicState extends RenderState {
        enum Op {
            NO_LOGIC,
            OR_REVERSE_LOGIC;
        }

        ColorLogicState NO_COLOR_LOGIC = create("none", Op.NO_LOGIC);
        ColorLogicState OR_REVERSE_COLOR_LOGIC = create("or_reverse", Op.OR_REVERSE_LOGIC);

        static ColorLogicState create(String name, Op op) {
            return RenderFactory.INSTANCE.createColorLogicState(name, op);
        }
    }

}
