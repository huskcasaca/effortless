package dev.huskuraft.effortless.renderer.opertaion;

import java.util.HashMap;
import java.util.Map;

import dev.huskuraft.universal.api.renderer.RenderLayer;
import dev.huskuraft.universal.api.renderer.RenderLayers;
import dev.huskuraft.universal.api.renderer.VertexFormats;
import dev.huskuraft.universal.api.renderer.programs.RenderState;
import dev.huskuraft.effortless.renderer.BlockShaders;

public abstract class BlockRenderLayers extends RenderLayers {

    // TODO: 16/1/24 use func
    public static final RenderLayer EF_PLANES = RenderLayer.createComposite("ef_planes",
            VertexFormats.POSITION_COLOR,
            VertexFormats.Modes.QUADS,
            256,
            RenderState.builder()
                    .setLineState(RenderLayers.NO_WIDTH)
                    .setShaderState(RenderLayers.POSITION_COLOR_SHADER_STATE)
                    .setLayeringState(RenderLayers.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(RenderLayers.COLOR_WRITE)
                    .setCullState(RenderLayers.NO_CULL)
                    .create(false));
    protected static final Map<String, RenderLayer> BLOCK_RENDER_TYPES = new HashMap<>();
    protected static final RenderState.ShaderState TINTED_SOLID_SHADER = RenderState.ShaderState.create("tinted_solid_shader", BlockShaders.TINTED_OUTLINE);
    protected static final RenderLayer EF_LINES = RenderLayer.createComposite("ef_lines",
            VertexFormats.POSITION_COLOR_NORMAL,
            VertexFormats.Modes.DEBUG_LINES,
            256,
            RenderState.builder()
                    .setLineState(RenderLayers.NO_WIDTH)
                    .setShaderState(RenderLayers.LINES_SHADER_STATE)
                    .setLayeringState(RenderLayers.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderLayers.ITEM_ENTITY_TARGET)
                    .setWriteMaskState(RenderLayers.COLOR_DEPTH_WRITE)
                    .setCullState(RenderLayers.NO_CULL)
                    .create(false));

    public static RenderLayer lines() {
        return EF_LINES;
    }

    public static RenderLayer planes() {
        return EF_PLANES;
    }

    public static RenderLayer block(int color) {
        return BLOCK_RENDER_TYPES.computeIfAbsent(Integer.toString(color), k -> {
            var texture = RenderState.TexturingState.create("block_texturing_" + k, () -> {
                var colorUniform = BlockShaders.TINTED_OUTLINE.getUniform("TintColor");
                if (colorUniform != null) {
                    colorUniform.set((color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f, (color & 255) / 255f, (color >>> 24) / 255f);
                }
            }, () -> {
            });
            var renderState = RenderState.builder()
                    .setShaderState(TINTED_SOLID_SHADER)
                    .setTexturingState(texture)
                    .setTransparencyState(RenderLayers.TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(RenderLayers.BLOCK_SHEET_MIPPED_TEXTURE)
                    .setLightmapState(RenderLayers.LIGHTMAP)
                    .setCullState(RenderLayers.NO_CULL)
                    .create(false);
            return RenderLayer.createComposite("ef_block_previews_" + k, VertexFormats.BLOCK, VertexFormats.Modes.QUADS, 2097152, true, false, renderState);
        });
    }
}
