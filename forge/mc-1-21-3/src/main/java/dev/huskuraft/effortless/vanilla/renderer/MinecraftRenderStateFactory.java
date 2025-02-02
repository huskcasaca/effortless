package dev.huskuraft.effortless.vanilla.renderer;

import java.util.OptionalDouble;

import com.google.auto.service.AutoService;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.RenderStateFactory;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Shaders;
import dev.huskuraft.effortless.api.renderer.VertexFormat;
import dev.huskuraft.effortless.api.renderer.VertexFormats;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

@AutoService(RenderStateFactory.class)
public final class MinecraftRenderStateFactory implements RenderStateFactory {

    @Override
    public RenderLayer createCompositeRenderLayer(String name,
                                                  VertexFormat vertexFormat,
                                                  VertexFormat.Mode vertexFormatMode,
                                                  int bufferSize,
                                                  boolean affectsCrumbling,
                                                  boolean sortOnUpload,
                                                  CompositeRenderState state) {
        var layer = RenderType.create(
                name,
                vertexFormat.reference(),
                vertexFormatMode.reference(),
                bufferSize,
                affectsCrumbling,
                sortOnUpload,
                state.reference()
        );
        return () -> layer;
    }

    @Override
    public CompositeRenderState createCompositeState(RenderState.TextureState textureState,
                                                     RenderState.ShaderState shaderState,
                                                     RenderState.TransparencyState transparencyState,
                                                     RenderState.DepthTestState depthTestState,
                                                     RenderState.CullState cullState,
                                                     RenderState.LightmapState lightmapState,
                                                     RenderState.OverlayState overlayState,
                                                     RenderState.LayeringState layeringState,
                                                     RenderState.OutputState outputState,
                                                     RenderState.TexturingState texturingState,
                                                     RenderState.WriteMaskState writeMaskState,
                                                     RenderState.LineState lineState,
                                                     RenderState.ColorLogicState colorLogicState,
                                                     boolean affectOutline) {
        var state = RenderType.CompositeState.builder()
                .setTextureState(textureState.reference())
                .setShaderState(shaderState.reference())
                .setTransparencyState(transparencyState.reference())
                .setDepthTestState(depthTestState.reference())
                .setCullState(cullState.reference())
                .setLightmapState(lightmapState.reference())
                .setOverlayState(overlayState.reference())
                .setLayeringState(layeringState.reference())
                .setOutputState(outputState.reference())
                .setTexturingState(texturingState.reference())
                .setWriteMaskState(writeMaskState.reference())
                .setLineState(lineState.reference())
                .setColorLogicState(colorLogicState.reference())
                .createCompositeState(affectOutline);
        return () -> state;
    }

    @Override
    public RenderState createRenderState(String name, Runnable setupState, Runnable clearState) {
        var state = new RenderStateShard(name, setupState, clearState) {
        };
        return () -> state;
    }

    @Override
    public RenderState.TextureState createTextureState(String name, RenderState.TextureState.Texture texture) {
        var state = texture == null ? RenderType.NO_TEXTURE : new RenderType.TextureStateShard(texture.location().reference(), texture.blur() ? net.minecraft.util.TriState.TRUE : net.minecraft.util.TriState.FALSE, texture.mipmap());
        return () -> state;
    }

    @Override
    public RenderState.ShaderState createShaderState(String name, Shader shader) {
        var state = shader == null ? new RenderType.ShaderStateShard() : new RenderType.ShaderStateShard(shader.reference());
        return () -> state;
    }

    @Override
    public RenderState.TransparencyState createTransparencyState(String name, RenderState.TransparencyState.Type type) {
        var state = switch (type) {
            case NO -> RenderType.NO_TRANSPARENCY;
            case ADDITIVE -> RenderType.ADDITIVE_TRANSPARENCY;
            case LIGHTNING -> RenderType.LIGHTNING_TRANSPARENCY;
            case GLINT -> RenderType.GLINT_TRANSPARENCY;
            case CRUMBLING -> RenderType.CRUMBLING_TRANSPARENCY;
            case TRANSLUCENT -> RenderType.TRANSLUCENT_TRANSPARENCY;
        };
        return () -> state;
    }

    @Override
    public RenderState.DepthTestState createDepthTestState(String name, int function) {
        var state = new RenderType.DepthTestStateShard(name, function);
        return () -> state;
    }

    @Override
    public RenderState.CullState createCullState(String name, boolean cull) {
        var state = cull ? RenderType.CULL : RenderType.NO_CULL;
        return () -> state;
    }

    @Override
    public RenderState.LightmapState createLightmapState(String name, boolean lightmap) {
        var state = lightmap ? RenderType.LIGHTMAP : RenderType.NO_LIGHTMAP;
        return () -> state;
    }

    @Override
    public RenderState.OverlayState createOverlayState(String name, boolean overlay) {
        var state = overlay ? RenderType.OVERLAY : RenderType.NO_OVERLAY;
        return () -> state;
    }

    @Override
    public RenderState.LayeringState createLayeringState(String name, RenderState.LayeringState.Type type) {
        var state = switch (type) {
            case NO -> RenderType.NO_LAYERING;
            case POLYGON_OFFSET -> RenderType.POLYGON_OFFSET_LAYERING;
            case VIEW_OFFSET_Z -> RenderType.VIEW_OFFSET_Z_LAYERING;
        };
        return () -> state;
    }

    @Override
    public RenderState.OutputState createOutputState(String name, RenderState.OutputState.Target target) {
        var state = switch (target) {
            case NO -> RenderType.MAIN_TARGET;
            case OUTLINE -> RenderType.OUTLINE_TARGET;
            case TRANSLUCENT -> RenderType.TRANSLUCENT_TARGET;
            case PARTICLES -> RenderType.PARTICLES_TARGET;
            case WEATHER -> RenderType.WEATHER_TARGET;
            case CLOUDS -> RenderType.CLOUDS_TARGET;
            case ITEM_ENTITY -> RenderType.ITEM_ENTITY_TARGET;
        };
        return () -> state;
    }

    @Override
    public RenderState.TexturingState createTexturingState(String name, Runnable setupState, Runnable clearState) {
        var state = new RenderType.TexturingStateShard(name, setupState, clearState);
        return () -> state;
    }

    @Override
    public RenderState.OffsetTexturingState createOffsetTexturingState(String name, float offsetX, float offsetY) {
        var state = new RenderType.OffsetTexturingStateShard(offsetX, offsetY);
        return () -> state;
    }

    @Override
    public RenderState.WriteMaskState createWriteMaskState(String name, boolean writeColor, boolean writeDepth) {
        var state = new RenderType.WriteMaskStateShard(writeColor, writeDepth);
        return () -> state;
    }

    @Override
    public RenderState.LineState createLineState(String name, Double width) {
        var state = new RenderType.LineStateShard(width == null ? OptionalDouble.empty() : OptionalDouble.of(width));
        return () -> state;
    }

    @Override
    public RenderState.ColorLogicState createColorLogicState(String name, RenderState.ColorLogicState.Op op) {
        var state = switch (op) {
            case NO_LOGIC -> RenderType.NO_COLOR_LOGIC;
            case OR_REVERSE_LOGIC -> RenderType.OR_REVERSE_COLOR_LOGIC;
        };
        return () -> state;
    }

    @Override
    public Shader getShader(Shaders shaders) {
        var shader = switch (shaders) {
            case POSITION_COLOR_LIGHTMAP -> CoreShaders.POSITION_COLOR_LIGHTMAP;
            case POSITION -> CoreShaders.POSITION;
            case POSITION_COLOR_TEX -> CoreShaders.POSITION_TEX;
            case POSITION_TEX -> CoreShaders.POSITION_TEX;
            case POSITION_COLOR_TEX_LIGHTMAP -> CoreShaders.POSITION_COLOR_TEX_LIGHTMAP;
            case POSITION_COLOR -> CoreShaders.POSITION_COLOR;
            case SOLID -> CoreShaders.RENDERTYPE_SOLID;
            case CUTOUT_MIPPED -> CoreShaders.RENDERTYPE_CUTOUT_MIPPED;
            case CUTOUT -> CoreShaders.RENDERTYPE_CUTOUT;
            case TRANSLUCENT -> CoreShaders.RENDERTYPE_TRANSLUCENT;
            case TRANSLUCENT_MOVING_BLOCK -> CoreShaders.RENDERTYPE_TRANSLUCENT_MOVING_BLOCK;
            case TRANSLUCENT_NO_CRUMBLING -> null;
            case ARMOR_CUTOUT_NO_CULL -> CoreShaders.RENDERTYPE_ARMOR_CUTOUT_NO_CULL;
            case ENTITY_SOLID -> CoreShaders.RENDERTYPE_ENTITY_SOLID;
            case ENTITY_CUTOUT -> CoreShaders.RENDERTYPE_ENTITY_CUTOUT;
            case ENTITY_CUTOUT_NO_CULL -> CoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL;
            case ENTITY_CUTOUT_NO_CULL_Z_OFFSET -> CoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET;
            case ITEM_ENTITY_TRANSLUCENT_CULL -> CoreShaders.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL;
            case ENTITY_TRANSLUCENT_CULL -> CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT;
            case ENTITY_TRANSLUCENT -> CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT;
            case ENTITY_TRANSLUCENT_EMISSIVE -> CoreShaders.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE;
            case ENTITY_SMOOTH_CUTOUT -> CoreShaders.RENDERTYPE_ENTITY_SMOOTH_CUTOUT;
            case BEACON_BEAM -> CoreShaders.RENDERTYPE_BEACON_BEAM;
            case ENTITY_DECAL -> CoreShaders.RENDERTYPE_ENTITY_DECAL;
            case ENTITY_NO_OUTLINE -> CoreShaders.RENDERTYPE_ENTITY_NO_OUTLINE;
            case ENTITY_SHADOW -> CoreShaders.RENDERTYPE_ENTITY_SHADOW;
            case ENTITY_ALPHA -> CoreShaders.RENDERTYPE_ENTITY_ALPHA;
            case EYES -> CoreShaders.RENDERTYPE_EYES;
            case ENERGY_SWIRL -> CoreShaders.RENDERTYPE_ENERGY_SWIRL;
            case LEASH -> CoreShaders.RENDERTYPE_LEASH;
            case WATER_MASK -> CoreShaders.RENDERTYPE_WATER_MASK;
            case OUTLINE -> CoreShaders.RENDERTYPE_OUTLINE;
            case ARMOR_GLINT -> CoreShaders.RENDERTYPE_ARMOR_ENTITY_GLINT;
            case ARMOR_ENTITY_GLINT -> null;
            case GLINT_TRANSLUCENT -> CoreShaders.RENDERTYPE_GLINT_TRANSLUCENT;
            case GLINT -> CoreShaders.RENDERTYPE_GLINT;
            case GLINT_DIRECT -> null;
            case ENTITY_GLINT -> CoreShaders.RENDERTYPE_ENTITY_GLINT;
            case ENTITY_GLINT_DIRECT -> null;
            case CRUMBLING -> CoreShaders.RENDERTYPE_CRUMBLING;
            case TEXT -> CoreShaders.RENDERTYPE_TEXT;
            case TEXT_BACKGROUND -> CoreShaders.RENDERTYPE_TEXT_BACKGROUND;
            case TEXT_INTENSITY -> CoreShaders.RENDERTYPE_TEXT_INTENSITY;
            case TEXT_SEE_THROUGH -> CoreShaders.RENDERTYPE_TEXT_SEE_THROUGH;
            case TEXT_BACKGROUND_SEE_THROUGH -> CoreShaders.RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH;
            case TEXT_INTENSITY_SEE_THROUGH -> CoreShaders.RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH;
            case LIGHTNING -> CoreShaders.RENDERTYPE_LIGHTNING;
            case TRIPWIRE -> CoreShaders.RENDERTYPE_TRIPWIRE;
            case END_PORTAL -> CoreShaders.RENDERTYPE_END_PORTAL;
            case END_GATEWAY -> CoreShaders.RENDERTYPE_END_GATEWAY;
            case LINES -> CoreShaders.RENDERTYPE_LINES;
            case GUI -> CoreShaders.RENDERTYPE_GUI;
            case GUI_OVERLAY -> CoreShaders.RENDERTYPE_GUI_OVERLAY;
            case GUI_TEXT_HIGHLIGHT -> CoreShaders.RENDERTYPE_GUI_TEXT_HIGHLIGHT;
            case GUI_GHOST_RECIPE_OVERLAY -> CoreShaders.RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY;
        };
        return new MinecraftShader(shader);
    }

    @Override
    public VertexFormat getVertexFormat(VertexFormats formats) {
        var format = switch (formats) {
            case BLIT_SCREEN -> DefaultVertexFormat.BLIT_SCREEN;
            case BLOCK -> DefaultVertexFormat.BLOCK;
            case NEW_ENTITY -> DefaultVertexFormat.NEW_ENTITY;
            case PARTICLE -> DefaultVertexFormat.PARTICLE;
            case POSITION -> DefaultVertexFormat.POSITION;
            case POSITION_COLOR -> DefaultVertexFormat.POSITION_COLOR;
            case POSITION_COLOR_NORMAL -> DefaultVertexFormat.POSITION_COLOR_NORMAL;
            case POSITION_COLOR_LIGHTMAP -> DefaultVertexFormat.POSITION_COLOR_LIGHTMAP;
            case POSITION_TEX -> DefaultVertexFormat.POSITION_TEX;
            case POSITION_COLOR_TEX -> null;
            case POSITION_TEX_COLOR -> DefaultVertexFormat.POSITION_TEX_COLOR;
            case POSITION_COLOR_TEX_LIGHTMAP -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            case POSITION_TEX_LIGHTMAP_COLOR -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR;
            case POSITION_TEX_COLOR_NORMAL -> DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL;
        };
        return () -> format;
    }

    @Override
    public VertexFormat.Mode getVertexFormatMode(VertexFormats.Modes modes) {
        var mode = switch (modes) {
            case LINES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES;
            case LINE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINE_STRIP;
            case DEBUG_LINES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES;
            case DEBUG_LINE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINE_STRIP;
            case TRIANGLES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES;
            case TRIANGLE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_STRIP;
            case TRIANGLE_FAN -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN;
            case QUADS -> com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
        };
        return () -> mode;
    }

}
