package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.DepthTestState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import dev.huskuraft.effortless.api.renderer.programs.ShaderState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;

public class MinecraftRenderFactory extends RenderType implements RenderFactory {

    public MinecraftRenderFactory() {
        super(null, null, null, 0, false, false, null, null);
//        super("", new VertexFormat(ImmutableMap.ofEntries()), VertexFormat.Mode.DEBUG_LINE_STRIP, 0, false, false, () -> {}, () -> {});
    }

    @Override
    public RenderState.Builder getRenderStateBuilder() {
        return new RenderState.Builder() {

            private final CompositeState.CompositeStateBuilder builder = CompositeState.builder();

            @Override
            public RenderState.Builder setDepthTestState(DepthTestState shard) {
                builder.setDepthTestState(shard.reference());
                return this;
            }

            @Override
            public RenderState.Builder setShaderState(ShaderState shard) {
                builder.setShaderState(shard.reference());
                return this;
            }

            @Override
            public CompositeRenderState build() {
                var state = builder.createCompositeState(false);
                return () -> state;
            }
        };
    }

    @Override
    public RenderLayer createCompositeRenderLayer(String name, VertexFormat vertexFormat, VertexMode vertexMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeRenderState state) {
        return () -> RenderType.create(name, vertexFormat.reference(), vertexMode.reference(), bufferSize, affectsCrumbling, sortOnUpload, state.reference());
    }

    @Override
    public DepthTestState createDepthTestState(String name, int function) {
        return () -> new DepthTestStateShard(name, function);
    }

    @Override
    public ShaderState createShaderState(String name, Shader shader) {
        return () -> new ShaderStateShard(shader::reference);
    }

    @Override
    public Shader getShader(Shaders shaders) {
        return () -> switch (shaders) {
            case GUI -> GameRenderer.getRendertypeGuiShader();
        };
    }

    @Override
    public VertexFormat getVertexFormat(VertexFormats formats) {
        return () -> switch (formats) {
            case BLIT_SCREEN ->                 DefaultVertexFormat.BLIT_SCREEN;
            case BLOCK ->                       DefaultVertexFormat.BLOCK;
            case NEW_ENTITY ->                  DefaultVertexFormat.NEW_ENTITY;
            case PARTICLE ->                    DefaultVertexFormat.PARTICLE;
            case POSITION ->                    DefaultVertexFormat.POSITION;
            case POSITION_COLOR ->              DefaultVertexFormat.POSITION_COLOR;
            case POSITION_COLOR_NORMAL ->       DefaultVertexFormat.POSITION_COLOR_NORMAL;
            case POSITION_COLOR_LIGHTMAP ->     DefaultVertexFormat.POSITION_COLOR_LIGHTMAP;
            case POSITION_TEX ->                DefaultVertexFormat.POSITION_TEX;
            case POSITION_COLOR_TEX ->          DefaultVertexFormat.POSITION_COLOR_TEX;
            case POSITION_TEX_COLOR ->          DefaultVertexFormat.POSITION_TEX_COLOR;
            case POSITION_COLOR_TEX_LIGHTMAP -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            case POSITION_TEX_LIGHTMAP_COLOR -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR;
            case POSITION_TEX_COLOR_NORMAL ->   DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL;
        };
    }

    @Override
    public VertexMode getVertexMode(VertexModes modes) {
        return () -> switch (modes) {
            case LINES ->               com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES;
            case LINE_STRIP ->          com.mojang.blaze3d.vertex.VertexFormat.Mode.LINE_STRIP;
            case DEBUG_LINES ->         com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES;
            case DEBUG_LINE_STRIP ->    com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINE_STRIP;
            case TRIANGLES ->           com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES;
            case TRIANGLE_STRIP ->      com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_STRIP;
            case TRIANGLE_FAN ->        com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN;
            case QUADS ->               com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
        };
    }

    @Override
    public RenderLayer vanillaGui() {
        return RenderType::gui;
    }

    @Override
    public RenderLayer vanillaGuiOverlay() {
        return RenderType::guiOverlay;
    }

    @Override
    public RenderLayer vanillaGuiTextHighlight() {
        return RenderType::guiTextHighlight;
    }

}
