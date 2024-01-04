package dev.huskuraft.effortless.vanilla.renderer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.texture.BlockRenderLayers;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MinecraftBlockRenderLayers extends BlockRenderLayers {

    @Override
    public RenderLayer lines() {
        return MinecraftRenderLayer.fromMinecraftRenderLayer(BlockRenderType.EF_LINES);
    }

    @Override
    public RenderLayer planes() {
        return MinecraftRenderLayer.fromMinecraftRenderLayer(BlockRenderType.EF_PLANES);
    }

    @Override
    public RenderLayer block(int color) {
        return MinecraftRenderLayer.fromMinecraftRenderLayer(BlockRenderType.block(color));
    }

    public static final class BlockRenderType extends RenderType {

        public BlockRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7) {
            super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
        }

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

        private static final Map<String, RenderType> BLOCK_RENDER_TYPES = Maps.newHashMap();

        public static RenderType block(int color) {
            return BLOCK_RENDER_TYPES.computeIfAbsent(Integer.toString(color), k -> {
                var texture = new TexturingStateShard("ef_block_texturing_" + k, () -> {
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
                return create("ef_block_previews_" + k, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, renderState);
            });
        }
    }

    public static final class Shaders {
        private static final Logger LOGGER = Logger.getLogger("Effortless");

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
