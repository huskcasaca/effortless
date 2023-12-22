package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class BlockRenderType extends RenderType {

    public static final ShaderStateShard RENDERTYPE_TINTED_SOLID_SHADER = new ShaderStateShard(Shaders::tintedSolidShader);
    public static final RenderType EF_LINES = create("ef_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.DEBUG_LINES,
            256,
            CompositeState.builder()
                    .setLineState(new LineStateShard(OptionalDouble.empty()))
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false));
    public static final RenderType EF_PLANES = create("ef_planes",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            CompositeState.builder()
                    .setLineState(new LineStateShard(OptionalDouble.empty()))
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
//                .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false));
    private static final Logger LOGGER = Logger.getLogger("Effortless");

    private BlockRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static RenderType blockPreview(int color) {
        var name = Integer.toString(color);
        var texture = new TexturingStateShard("ef_block_texturing_" + name, () -> {
            var colorUniform = Shaders.tintedSolidShader().getUniform("TintColor");
            if (colorUniform != null) {
                colorUniform.set((color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f, (color & 255) / 255f, (color >>> 24) / 255f);
            }
        }, () -> {
        });
        var renderState = CompositeState.builder()
                .setShaderState(RENDERTYPE_TINTED_SOLID_SHADER)
                .setTexturingState(texture)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setTextureState(BLOCK_SHEET_MIPPED)
                .setLightmapState(LIGHTMAP)
                .setCullState(NO_CULL)
                .createCompositeState(false);
        return create("ef_block_previews_" + name, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, renderState);
    }

    public static final class Shaders {

        @Nullable
        private static ShaderInstance tintedSolidShader;

        public static ShaderInstance tintedSolidShader() {
            return tintedSolidShader;
        }

        public static void registerShaders(ResourceProvider provider, BiConsumer<ShaderInstance, Consumer<ShaderInstance>> sink) {
            try {
                sink.accept(
                        new ShaderInstance(provider, "rendertype_tinted_solid", DefaultVertexFormat.BLOCK),
                        shaderInstance -> tintedSolidShader = shaderInstance
                );
            } catch (IOException e) {
                LOGGER.info("Failed to register block shaders");
                e.printStackTrace();
            }
        }

    }
}
