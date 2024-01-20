package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.core.MinecraftClient;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class MinecraftRenderer extends Renderer {

    private static final RandomSource RAND = RandomSource.create();
    private final Minecraft minecraftClient;
    private final PoseStack minecraftMatrixStack;
    private final MultiBufferSource.BufferSource minecraftBufferSource;
    private final GuiGraphics minecraftRendererProvider;

    public MinecraftRenderer(PoseStack minecraftMatrixStack) {
        this.minecraftClient = Minecraft.getInstance();
        this.minecraftMatrixStack = minecraftMatrixStack;
        this.minecraftBufferSource = minecraftClient.renderBuffers().bufferSource();
        this.minecraftRendererProvider = new GuiGraphics(Minecraft.getInstance(), this.minecraftMatrixStack, minecraftBufferSource);
    }

    @Override
    public Client client() {
        return new MinecraftClient(minecraftClient);
    }

    @Override
    public MatrixStack matrixStack() {
        return new MinecraftMatrixStack(minecraftMatrixStack);
    }

    @Override
    protected void enableScissorInternal(int x1, int y1, int x2, int y2) {
        RenderSystem.enableScissor(x1, y1, x2, y2);
    }

    @Override
    protected void disableScissorInternal() {
        RenderSystem.disableScissor();
    }

    @Override
    public void setRsShaderColor(float red, float green, float blue, float alpha) {
//        minecraft renderer provider flush if managed();
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    @Override
    public BufferSource bufferSource() {
        return new MinecraftBufferSource(minecraftBufferSource);
    }

    @Override
    public void flush() {
        RenderSystem.disableDepthTest();
        minecraftBufferSource.endBatch();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected int renderTextInternal(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, boolean seeThrough, int lightMap) {
        var minecraftTypeface = (Font) typeface.reference();
        var minecraftText = (Component) text.reference();
        return minecraftTypeface.drawInBatch(minecraftText,
                x,
                y,
                color,
                shadow,
                minecraftMatrixStack.last().pose(),
                minecraftBufferSource,
                seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL,
                backgroundColor,
                lightMap);
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
        minecraftRendererProvider.renderItem(stack.reference(), x, y);
    }

    @Override
    public void renderTooltip(Typeface typeface, List<Text> list, int x, int y) {
        minecraftRendererProvider.renderTooltip(typeface.reference(), list.stream().map(text -> (Component) text.reference()).toList(), Optional.empty(), x, y);
    }

    @Override
    public void renderBlockInWorld(RenderLayer renderLayer, World world, BlockPosition blockPosition, BlockState blockState) {
        var minecraftBlockRenderer = minecraftClient.getBlockRenderer();
        var minecraftWorld = (Level) world.reference();
        var minecraftRenderLayer = (RenderType) renderLayer.reference();
        var minecraftBlockState = (net.minecraft.world.level.block.state.BlockState) blockState.reference();
        var minecraftBlockPosition = MinecraftConvertor.toPlatformBlockPosition(blockPosition);

        minecraftBlockRenderer.getModelRenderer().tesselateBlock(
                minecraftWorld,
                minecraftBlockRenderer.getBlockModel(minecraftBlockState),
                minecraftBlockState,
                minecraftBlockPosition,
                minecraftMatrixStack,
                minecraftBufferSource.getBuffer(minecraftRenderLayer),
                false,
                RAND,
                minecraftBlockState.getSeed(minecraftBlockPosition),
                OverlayTexture.NO_OVERLAY);
    }

}
