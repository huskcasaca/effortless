package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.huskuraft.effortless.renderer.RenderLayer;
import dev.huskuraft.effortless.renderer.texture.RenderLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderLayer.fromMinecraftRenderLayer;

public class MinecraftRenderLayers extends RenderLayers {

    @Override
    public RenderLayer gui() {
        return fromMinecraftRenderLayer(BuildInRenderType.GUI);
    }

    @Override
    public RenderLayer guiOverlay() {
        return fromMinecraftRenderLayer(BuildInRenderType.GUI_OVERLAY);
    }

    @Override
    public RenderLayer guiTextHighlight() {
        return fromMinecraftRenderLayer(BuildInRenderType.GUI_TEXT_HIGHLIGHT);
    }

    private static class BuildInRenderType extends RenderType {

        protected static final ShaderStateShard RENDERTYPE_GUI_SHADER = new ShaderStateShard(Shaders::getRendertypeGuiShader);
        protected static final ShaderStateShard RENDERTYPE_GUI_OVERLAY_SHADER = new ShaderStateShard(Shaders::getRendertypeGuiOverlayShader);
        protected static final ShaderStateShard RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER = new ShaderStateShard(Shaders::getRendertypeGuiTextHighlightShader);

        protected static final RenderType GUI = create("gui",DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(false));
        protected static final RenderType GUI_OVERLAY = create("gui_overlay",DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_OVERLAY_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
        protected static final RenderType GUI_TEXT_HIGHLIGHT = create("gui_text_highlight",DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST)/*.setColorLogicState(OR_REVERSE_COLOR_LOGIC)*/.createCompositeState(false));

        public BuildInRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7) {
            super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
        }
    }

    public static final class Shaders {
        private static final Logger LOGGER = Logger.getLogger("Effortless");

        @Nullable
        private static ShaderInstance rendertypeGuiShader;
        @Nullable
        private static ShaderInstance rendertypeGuiOverlayShader;
        @Nullable
        private static ShaderInstance rendertypeGuiTextHighlightShader;

        @Nullable
        public static ShaderInstance getRendertypeGuiShader() {
            return rendertypeGuiShader;
        }

        @Nullable
        public static ShaderInstance getRendertypeGuiOverlayShader() {
            return rendertypeGuiOverlayShader;
        }

        @Nullable
        public static ShaderInstance getRendertypeGuiTextHighlightShader() {
            return rendertypeGuiTextHighlightShader;
        }

        public static void registerShaders(ResourceProvider provider, BiConsumer<ShaderInstance, Consumer<ShaderInstance>> sink) {
            try {
                sink.accept(new ShaderInstance(provider, "rendertype_gui", DefaultVertexFormat.POSITION_COLOR), shaderInstance -> rendertypeGuiShader = shaderInstance);
                sink.accept(new ShaderInstance(provider, "rendertype_gui_overlay", DefaultVertexFormat.POSITION_COLOR), shaderInstance -> rendertypeGuiOverlayShader = shaderInstance);
                sink.accept(new ShaderInstance(provider, "rendertype_gui_text_highlight", DefaultVertexFormat.POSITION_COLOR), shaderInstance -> rendertypeGuiTextHighlightShader = shaderInstance);

            } catch (IOException e) {
                LOGGER.info("Failed to register vanilla shaders");
                e.printStackTrace();
            }
        }

    }
}
