package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.renderer.VertexBuffer;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.renderer.texture.BlockRenderLayers;
import dev.huskuraft.effortless.api.renderer.texture.OutlineRenderLayers;
import dev.huskuraft.effortless.api.renderer.texture.RenderLayers;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftBlockRenderLayers;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftOutlineRenderLayers;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

public class MinecraftRenderer extends Renderer {

    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    private static final RenderLayers RENDER_TEXTURES = new MinecraftRenderLayers();
    private static final BlockRenderLayers BLOCK_RENDER_TEXTURES = new MinecraftBlockRenderLayers();
    private static final OutlineRenderLayers OUTLINE_RENDER_TEXTURES = new MinecraftOutlineRenderLayers();

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

    public static Renderer fromMinecraft(PoseStack matrixStack) {
        return new MinecraftRenderer(matrixStack);
    }

    public static Renderer fromMinecraft(GuiGraphics renderer) {
        return new MinecraftRenderer(renderer.pose());
    }

    @Override
    public Window window() {
        return MinecraftWindow.fromMinecraftCamera(minecraftClient.getWindow());
    }

    @Override
    public Camera camera() {
        return MinecraftCamera.fromMinecraftCamera(minecraftClient.gameRenderer.getMainCamera());
    }

    @Override
    public MatrixStack matrixStack() {
        return MinecraftMatrixStack.fromMinecraftMatrixStack(minecraftMatrixStack);
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
//        minecraft renderer provider flush if managed();
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    @Override
    public VertexBuffer vertexBuffer(RenderLayer renderLayer) {
        return new MinecraftVertexBuffer(minecraftBufferSource.getBuffer(MinecraftRenderLayer.toMinecraftRenderLayer(renderLayer)));
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
        var minecraftTypeface = MinecraftTypeface.toMinecraftTypeface(typeface);
        var minecraftText = MinecraftText.toMinecraftText(text);
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
    public void renderTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShaderTexture(0, MinecraftResource.toMinecraftResource(resource));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        var matrix4f = minecraftMatrixStack.last().pose();
        var bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
        minecraftRendererProvider.blitSprite(BUTTON_SPRITES.get(active, focused), x, y, width, height);
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
        minecraftRendererProvider.renderItem(MinecraftItemStack.toMinecraftItemStack(stack), x, y);
    }

    @Override
    public void renderTooltip(Typeface typeface, List<Text> list, int x, int y) {
        minecraftRendererProvider.renderTooltip(MinecraftTypeface.toMinecraftTypeface(typeface), list.stream().map(MinecraftText::toMinecraftText).toList(), Optional.empty(), x, y);
    }

    @Override
    public void renderBlockInWorld(RenderLayer renderLayer, World world, BlockPosition blockPosition, BlockState blockState) {
        var minecraftBlockRenderer = minecraftClient.getBlockRenderer();
        var minecraftWorld = MinecraftWorld.toMinecraftWorld(world);
        var minecraftRenderLayer = MinecraftRenderLayer.toMinecraftRenderLayer(renderLayer);
        var minecraftBlockState = MinecraftBlockState.toMinecraftBlockState(blockState);
        var minecraftBlockPosition = MinecraftPlayer.toMinecraftBlockPosition(blockPosition);

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

    @Override
    public RenderLayers renderLayers() {
        return RENDER_TEXTURES;
    }

    @Override
    public BlockRenderLayers blockRenderLayers() {
        return BLOCK_RENDER_TEXTURES;
    }

    @Override
    public OutlineRenderLayers outlineRenderLayers() {
        return OUTLINE_RENDER_TEXTURES;
    }

}
