package dev.huskuraft.effortless.renderer.outliner;

import java.util.HashMap;
import java.util.Map;

import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.renderer.RenderLayer;
import dev.huskuraft.universal.api.renderer.RenderLayers;
import dev.huskuraft.universal.api.renderer.VertexFormats;
import dev.huskuraft.universal.api.renderer.programs.RenderState;

public abstract class OutlineRenderLayers extends RenderLayers {

    public static final ResourceLocation BLANK_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/blank.png");
    public static final ResourceLocation CHECKERED_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/checkerboard.png");
    public static final ResourceLocation CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/checkerboard_highlight.png");

    public static final ResourceLocation CHECKERED_THIN_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/checkerboard_thin_64.png");
    public static final ResourceLocation CHECKERED_CUTOUT_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/checkerboard_cutout.png");
    public static final ResourceLocation SELECTION_TEXTURE_LOCATION = ResourceLocation.of("effortless", "textures/misc/selection_64.png");

    public static final RenderState.TextureState BLANK_TEXTURE_STATE = RenderState.TextureState.create("blank", BLANK_TEXTURE_LOCATION, false, false);

    protected static final RenderLayer OUTLINE_SOLID = RenderLayer.createComposite("outline_solid",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_SOLID_SHADER_STATE)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(RenderLayers.CULL)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer OUTLINE_SOLID_OVERLAP = RenderLayer.createComposite("outline_solid_overlap",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_SOLID_SHADER_STATE)
                    .setTransparencyState(RenderLayers.NO_TRANSPARENCY)
                    .setDepthTestState(RenderLayers.NOTEQUAL_DEPTH_TEST)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(RenderLayers.CULL)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer OUTLINE_SOLID_NO_OVERLAP = RenderLayer.createComposite("outline_solid_overlap",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            false,
            false,
            RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_SOLID_SHADER_STATE)
                    .setTransparencyState(RenderLayers.NO_TRANSPARENCY)
                    .setDepthTestState(RenderLayers.NEVER_DEPTH_TEST)
                    .setTextureState(BLANK_TEXTURE_STATE)
                    .setCullState(RenderLayers.CULL)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer GLOWING_SOLID_DEFAULT = glowingSolid(RenderLayers.BLOCK_ATLAS_LOCATION);
    protected static final RenderLayer ADDITIVE = RenderLayer.createComposite("additive",
            VertexFormats.BLOCK,
            VertexFormats.Modes.QUADS,
            256,
            true,
            true,
            RenderState.builder()
                    .setShaderState(RenderLayers.SOLID_SHADER_STATE)
                    .setTextureState(RenderLayers.BLOCK_SHEET_MIPPED_TEXTURE)
                    .setTransparencyState(RenderLayers.ADDITIVE_TRANSPARENCY)
                    .setCullState(RenderLayers.NO_CULL)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer GLOWING_TRANSLUCENT_DEFAULT = glowingTranslucent(RenderLayers.BLOCK_ATLAS_LOCATION);
    protected static final RenderLayer ITEM_PARTIAL_SOLID = RenderLayer.createComposite("item_partial_solid",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            true,
            false,
            RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_SOLID_SHADER_STATE)
                    .setTextureState(RenderLayers.BLOCK_SHEET_TEXTURE)
                    .setCullState(RenderLayers.CULL)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer ITEM_PARTIAL_TRANSLUCENT = RenderLayer.createComposite("item_partial_translucent",
            VertexFormats.NEW_ENTITY,
            VertexFormats.Modes.QUADS,
            256,
            true,
            true,
            RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_TRANSLUCENT_CULL_SHADER_STATE)
                    .setCullState(RenderLayers.CULL)
                    .setTextureState(RenderLayers.BLOCK_SHEET_TEXTURE)
                    .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final RenderLayer FLUID = RenderLayer.createComposite("fluid",
            VertexFormats.NEW_ENTITY, VertexFormats.Modes.QUADS, 256, false, true, RenderState.builder()
                    .setShaderState(RenderLayers.ENTITY_TRANSLUCENT_CULL_SHADER_STATE)
                    .setCullState(RenderLayers.CULL)
                    .setTextureState(RenderLayers.BLOCK_SHEET_MIPPED_TEXTURE)
                    .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setOverlayState(RenderLayers.OVERLAY)
                    .create(true));
    protected static final Map<String, RenderLayer> OUTLINE_TRANSLUCENT = new HashMap<>();

    public static RenderLayer outlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderLayer outlineSolid(boolean overlap) {
        return overlap ? OUTLINE_SOLID_OVERLAP : OUTLINE_SOLID_NO_OVERLAP;
    }

    public static RenderLayer outlineTranslucent(ResourceLocation texture, boolean cull) {
        return OUTLINE_TRANSLUCENT.computeIfAbsent("outline_translucent" + (cull ? "_cull" : "") + "_" + texture.getNamespace() + "_" + texture.getPath(), name -> {
            return RenderLayer.createComposite(name,
                    VertexFormats.NEW_ENTITY,
                    VertexFormats.Modes.QUADS,
                    256,
                    false,
                    true,
                    RenderState.builder()
                            .setShaderState(cull ? RenderLayers.ENTITY_TRANSLUCENT_CULL_SHADER_STATE : RenderLayers.ENTITY_TRANSLUCENT_SHADER_STATE)
                            .setTextureState(RenderState.TextureState.create("custom", texture, false, false))
                            .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                            .setCullState(cull ? RenderLayers.CULL : RenderLayers.NO_CULL)
                            .setLightmapState(RenderLayers.LIGHTMAP)
                            .setOverlayState(RenderLayers.OVERLAY)
                            .setWriteMaskState(RenderLayers.COLOR_WRITE)
                            .create(false));
        });
    }

    public static RenderLayer glowingSolid(ResourceLocation texture) {
        return RenderLayer.createComposite("glowing_solid",
                VertexFormats.NEW_ENTITY,
                VertexFormats.Modes.QUADS,
                256,
                true,
                false,
                RenderState.builder()
                        .setTextureState(RenderState.TextureState.create("custom", texture, false, false))
                        .setCullState(RenderLayers.CULL)
                        .setLightmapState(RenderLayers.LIGHTMAP)
                        .setOverlayState(RenderLayers.OVERLAY)
                        .create(true));
    }

    public static RenderLayer glowingSolid() {
        return GLOWING_SOLID_DEFAULT;
    }

    public static RenderLayer glowingTranslucent(ResourceLocation texture) {
        return RenderLayer.createComposite("glowing_translucent",
                VertexFormats.NEW_ENTITY,
                VertexFormats.Modes.QUADS,
                256,
                true,
                true,
                RenderState.builder()
                        //				.setShaderState(GLOWING_SHADER)
                        .setTextureState(RenderState.TextureState.create("custom", texture, false, false))
                        .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderLayers.LIGHTMAP)
                        .setOverlayState(RenderLayers.OVERLAY)
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
