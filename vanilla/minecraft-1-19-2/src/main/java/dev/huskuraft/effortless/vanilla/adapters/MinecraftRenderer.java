package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

class MinecraftRenderer extends Renderer {

    private static final RandomSource RAND = RandomSource.create();
    private final Minecraft minecraftClient;
    private final PoseStack minecraftMatrixStack;
    private final MultiBufferSource.BufferSource minecraftBufferSource;
    private final Screen minecraftRendererProvider;

    MinecraftRenderer(PoseStack minecraftMatrixStack) {
        this.minecraftClient = Minecraft.getInstance();
        this.minecraftMatrixStack = minecraftMatrixStack;
        this.minecraftBufferSource = minecraftClient.renderBuffers().bufferSource();
        this.minecraftRendererProvider = new Screen(null) {
        };
    }

    public static Renderer fromMinecraft(PoseStack matrixStack) {
        return new MinecraftRenderer(matrixStack);
    }

    @Override
    public Window window() {
        return MinecraftConvertor.fromPlatformWindow(minecraftClient.getWindow());
    }

    @Override
    public Camera camera() {
        return MinecraftConvertor.fromPlatformCamera(minecraftClient.gameRenderer.getMainCamera());
    }

    @Override
    public MatrixStack matrixStack() {
        return MinecraftConvertor.fromPlatformMatrixStack(minecraftMatrixStack);
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
    public void setShaderColor(float red, float green, float blue, float alpha) {
//        minecraftRendererProvider.flushIfManaged();
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    @Override
    public VertexBuffer vertexBuffer(RenderLayer renderLayer) {
        return MinecraftConvertor.fromPlatformVertexBuffer(minecraftBufferSource.getBuffer(renderLayer.reference()));
    }

    @Override
    public BufferSource bufferSource() {
        return new MinecraftBufferSource(minecraftClient.renderBuffers().bufferSource());
    }

    @Override
    public void flush() {
        RenderSystem.disableDepthTest();
        minecraftBufferSource.endBatch();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected int renderTextInternal(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, boolean seeThrough, int lightMap) {
        var minecraftTypeface = MinecraftConvertor.toPlatformTypeface(typeface);
        var minecraftText = MinecraftConvertor.toPlatformText(text);
        return minecraftTypeface.drawInBatch(minecraftText,
                x,
                y,
                color,
                shadow,
                minecraftMatrixStack.last().pose(),
                minecraftBufferSource,
                seeThrough,
                backgroundColor,
                lightMap);
    }

    @Override
    public void renderTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShaderTexture(0, resource.reference());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        var matrix4f = minecraftMatrixStack.last().pose();
        var bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, x1, y1, blitOffset).uv(minU, minV).endVertex();
        bufferbuilder.vertex(matrix4f, x1, y2, blitOffset).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y2, blitOffset).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y1, blitOffset).uv(maxU, minV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    @Override
    public void renderPanelBackgroundTexture(int x, int y, float uOffset, float vOffset, int uWidth, int vHeight) {
//        drawTexture(MinecraftClientAdapter.adapt(BACKGROUND_LOCATION), x, y, 0, uOffset, vOffset, uWidth, vHeight, 32, 32);
    }

    @Override
    public void renderButtonTexture(int x, int y, int width, int height, boolean active, boolean focused) {
//        minecraftRendererProvider.blitSprite(BUTTON_SPRITES.get(active, focused), x, y, width, height);
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
        minecraftClient.getItemRenderer().renderGuiItem(MinecraftConvertor.toPlatformItemStack(stack), x, y);
    }

    @Override
    public void renderTooltip(Typeface typeface, List<Text> list, int x, int y) {
        minecraftRendererProvider.renderTooltip(minecraftMatrixStack, list.stream().map(MinecraftConvertor::toPlatformText).toList(), Optional.empty(), x, y);
    }

    @Override
    public void renderBlockInWorld(RenderLayer renderLayer, World world, BlockPosition blockPosition, BlockState blockState) {
        var minecraftBlockRenderer = minecraftClient.getBlockRenderer();
        var minecraftWorld = MinecraftConvertor.toPlatformWorld(world);
        var minecraftRenderLayer = MinecraftConvertor.toPlatformRenderLayer(renderLayer);
        var minecraftBlockState = MinecraftConvertor.toPlatformBlockState(blockState);
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
