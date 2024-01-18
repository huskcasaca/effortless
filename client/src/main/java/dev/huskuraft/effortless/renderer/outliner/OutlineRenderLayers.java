package dev.huskuraft.effortless.renderer.outliner;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.RenderLayers;
import dev.huskuraft.effortless.api.renderer.VertexFormats;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;

import java.util.HashMap;
import java.util.Map;

public abstract class OutlineRenderLayers extends RenderLayers {

    public static final Resource BLANK_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/blank.png");
    public static final Resource CHECKERED_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/checkerboard.png");
    public static final Resource CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/checkerboard_highlight.png");

    public static final Resource CHECKERED_THIN_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/checkerboard_thin_64.png");
    public static final Resource CHECKERED_CUTOUT_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/checkerboard_cutout.png");
    public static final Resource SELECTION_TEXTURE_LOCATION = Resource.of("effortless", "textures/misc/selection_64.png");

    public static final RenderState.TextureState BLANK_TEXTURE_STATE = RenderState.TextureState.create("blank", new RenderState.TextureState.Texture(BLANK_TEXTURE_LOCATION, false, false));

    protected static final RenderLayer OUTLINE_SOLID = RenderLayer.createComposite("outline_solid",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(ENTITY_SOLID_SHADER_STATE)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));
    protected static final RenderLayer GLOWING_SOLID_DEFAULT = glowingSolid(BLOCK_ATLAS_LOCATION);
    protected static final RenderLayer ADDITIVE = RenderLayer.createComposite("additive",
            VertexFormats.BLOCK,
            VertexFormats.Modes.QUADS,
            256,
            true,
            true,
            RenderState.builder()
                    .setShaderState(SOLID_SHADER_STATE)
                    .setTextureState(BLOCK_SHEET_MIPPED_TEXTURE)
                    .setTransparencyState(ADDITIVE_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));
    protected static final RenderLayer GLOWING_TRANSLUCENT_DEFAULT = glowingTranslucent(BLOCK_ATLAS_LOCATION);
    protected static final RenderLayer ITEM_PARTIAL_SOLID = RenderLayer.createComposite("item_partial_solid",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            true,
            false,
            RenderState.builder()
                    .setShaderState(ENTITY_SOLID_SHADER_STATE)
                    .setTextureState(BLOCK_SHEET_TEXTURE)
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));
    protected static final RenderLayer ITEM_PARTIAL_TRANSLUCENT = RenderLayer.createComposite("item_partial_translucent",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            true,
            true,
            RenderState.builder()
                    .setShaderState(ENTITY_TRANSLUCENT_CULL_SHADER_STATE)
                    .setTextureState(BLOCK_SHEET_TEXTURE)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));
    protected static final RenderLayer FLUID = RenderLayer.createComposite("fluid",
            VertexFormats.NEW_ENTITY, VertexFormats.Modes.QUADS, 256, false, true, RenderState.builder()
                    .setShaderState(ENTITY_TRANSLUCENT_CULL_SHADER_STATE)
                    .setTextureState(BLOCK_SHEET_MIPPED_TEXTURE)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));

    public static RenderLayer outlineSolid() {
        return OUTLINE_SOLID;
    }

    protected static final RenderLayer OUTLINE_SOLID_OVERLAP = RenderLayer.createComposite("outline_solid_overlap",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(ENTITY_SOLID_SHADER_STATE)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setDepthTestState(NOTEQUAL_DEPTH_TEST)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));

    protected static final RenderLayer OUTLINE_SOLID_NO_OVERLAP = RenderLayer.createComposite("outline_solid_overlap",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(ENTITY_SOLID_SHADER_STATE)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setDepthTestState(NEVER_DEPTH_TEST)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .create(true));

    public static RenderLayer outlineSolid(boolean overlap) {
        return overlap ? OUTLINE_SOLID_OVERLAP : OUTLINE_SOLID_NO_OVERLAP;
    }

    protected static final Map<String, RenderLayer> OUTLINE_TRANSLUCENT = new HashMap<>();

    public static RenderLayer outlineTranslucent(Resource texture, boolean cull) {
        return OUTLINE_TRANSLUCENT.computeIfAbsent("outline_translucent" + (cull ? "_cull" : "") + "_" + texture.getNamespace() + "_" + texture.getPath(), name -> {
            return RenderLayer.createComposite(name,
                    VertexFormats.NEW_ENTITY,
                    VertexFormats.Modes.QUADS,
                    256,
                    false,
                    true,
                    RenderState.builder()
                            .setShaderState(cull ? ENTITY_TRANSLUCENT_CULL_SHADER_STATE : ENTITY_TRANSLUCENT_SHADER_STATE)
                            .setTextureState(RenderState.TextureState.create("custom", new RenderState.TextureState.Texture(texture, false, false)))
                            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                            .setCullState(cull ? CULL : NO_CULL)
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .setWriteMaskState(COLOR_WRITE)
                            .create(false));
        });
    }

    public static RenderLayer glowingSolid(Resource texture) {
        return RenderLayer.createComposite("glowing_solid",
                VertexFormats.NEW_ENTITY,
                VertexFormats.Modes.QUADS,
                256,
                true,
                false,
                RenderState.builder()
                        .setTextureState(RenderState.TextureState.create("custom", new RenderState.TextureState.Texture(texture, false, false)))
                        .setCullState(CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .create(true));
    }

    public static RenderLayer glowingSolid() {
        return GLOWING_SOLID_DEFAULT;
    }

    public static RenderLayer glowingTranslucent(Resource texture) {
        return RenderLayer.createComposite("glowing_translucent",
                VertexFormats.NEW_ENTITY,
                VertexFormats.Modes.QUADS,
                256,
                true,
                true,
                RenderState.builder()
                        //				.setShaderState(GLOWING_SHADER)
                        .setTextureState(RenderState.TextureState.create("custom", new RenderState.TextureState.Texture(texture, false, false)))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .create(true));
    }

    public static RenderLayer additive() {
        return ADDITIVE;
    }

    public static RenderLayer glowingTranslucent() {
        return GLOWING_TRANSLUCENT_DEFAULT;
    }

    public static RenderLayer itemPartialSolid() {
        return ITEM_PARTIAL_SOLID;
    }

    public static RenderLayer itemPartialTranslucent() {
        return ITEM_PARTIAL_TRANSLUCENT;
    }

    public static RenderLayer fluid() {
        return FLUID;
    }

}