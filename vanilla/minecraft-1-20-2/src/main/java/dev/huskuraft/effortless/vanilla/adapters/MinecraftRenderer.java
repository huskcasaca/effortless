package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.RenderStyle;
import dev.huskuraft.effortless.renderer.RenderStyleProvider;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;

class MinecraftRenderer extends Renderer {

    @Deprecated
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("textures/gui/options_background.png");
    @Deprecated
    private static final ResourceLocation STATS_ICONS_LOCATION = new ResourceLocation("textures/gui/container/stats_icons.png");

    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    private static final RenderStyleProvider RENDER_STYLE_PROVIDER = new MinecraftRenderStyleProvider();
    private static final RandomSource RAND = RandomSource.create();
    private final GuiGraphics proxy;

    MinecraftRenderer(GuiGraphics proxy) {
        this.proxy = proxy;
    }

    @Override
    public int getWindowWidth() {
        return proxy.guiWidth();
    }

    @Override
    public int getWindowHeight() {
        return proxy.guiHeight();
    }

    @Override
    public int getOptionColor(float alpha) {
        return Minecraft.getInstance().options.getBackgroundColor(alpha);
    }

    @Override
    public Vector3d getCameraPosition() {
        return MinecraftClientAdapter.adapt(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
    }

    @Override
    public void pushPose() {
        proxy.pose().pushPose();
    }

    @Override
    public void popPose() {
        proxy.pose().popPose();
    }

    @Override
    public void pushLayer() {
    }

    @Override
    public void popLayer() {
    }

    @Override
    public void translate(double d, double e, double f) {
        proxy.pose().translate(d, e, f);
    }

    @Override
    public void translate(float d, float e, float f) {
        proxy.pose().translate(d, e, f);
    }

    @Override
    public void scale(double d, double e, double f) {
        proxy.pose().scale((float) d, (float) e, (float) f);
    }

    @Override
    public void scale(float d, float e, float f) {
        proxy.pose().scale(d, e, f);
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        proxy.enableScissor(x1, y1, x2, y2);
    }

    @Override
    public void disableScissor() {
        proxy.disableScissor();
    }

    @Override
    public void setShaderColor(float red, float green, float blue, float alpha) {
        proxy.setColor(red, green, blue, alpha);
    }

    @Override
    public void draw() {
        proxy.flush();
    }

    @Override
    public void drawHorizontalLine(int x1, int x2, int y, int color) {
        proxy.hLine(x1, x2, y, color);
    }

    @Override
    public void drawVerticalLine(int x, int y1, int y2, int color) {
        proxy.vLine(x, y1, y2, color);
    }

    @Override
    public void drawHorizontalLine(RenderStyle renderStyle, int x1, int x2, int y, int color) {
        proxy.hLine(MinecraftClientAdapter.adapt(renderStyle), x1, x2, y, color);
    }

    @Override
    public void drawBorder(int i, int j, int k, int l, int m) {
        proxy.renderOutline(i, j, k, l, m);
    }

    @Override
    public void drawVerticalLine(RenderStyle renderStyle, int x, int y1, int y2, int color) {
        proxy.vLine(MinecraftClientAdapter.adapt(renderStyle), x, y1, y2, color);
    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2, int color) {
        drawRect(getStyleProvider().gui(), x1, y1, x2, y2, color, 0);
    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2, int color, int z) {
        drawRect(getStyleProvider().gui(), x1, y1, x2, y2, color, z);
    }

    @Override
    public void drawRect(RenderStyle renderStyle, int x1, int y1, int x2, int y2, int color) {
        drawRect(renderStyle, x1, y1, x2, y2, color, 0);
    }

    @Override
    public void drawRect(RenderStyle renderStyle, int x1, int y1, int x2, int y2, int color, int z) {
        proxy.fill(MinecraftClientAdapter.adapt(renderStyle), x1, y1, x2, y2, z, color);
    }

    @Override
    public void drawGradientRect(int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        drawGradientRect(startX, startY, endX, endY, colorStart, colorEnd, 0);
    }

    @Override
    public void drawGradientRect(int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        drawGradientRect(getStyleProvider().gui(), startX, startY, endX, endY, colorStart, colorEnd, z);
    }

    @Override
    public void drawGradientRect(RenderStyle renderStyle, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        proxy.fillGradient(MinecraftClientAdapter.adapt(renderStyle), startX, startY, endX, endY, colorStart, colorEnd, z); // notice params order
    }

//    protected static void fillGradient2(Matrix4f matrix4f, BufferBuilder bufferBuilder, int i, int j, int k, int l, int m, int n, int o) {
//        float f = (float) (n >> 24 & 255) / 255.0F;
//        float g = (float) (n >> 16 & 255) / 255.0F;
//        float h = (float) (n >> 8 & 255) / 255.0F;
//        float p = (float) (n & 255) / 255.0F;
//        float q = (float) (o >> 24 & 255) / 255.0F;
//        float r = (float) (o >> 16 & 255) / 255.0F;
//        float s = (float) (o >> 8 & 255) / 255.0F;
//        float t = (float) (o & 255) / 255.0F;
//        bufferBuilder.vertex(matrix4f, (float) k, (float) j, (float) m).color(r, s, t, q).endVertex();
//        bufferBuilder.vertex(matrix4f, (float) i, (float) j, (float) m).color(g, h, p, f).endVertex();
//        bufferBuilder.vertex(matrix4f, (float) i, (float) l, (float) m).color(g, h, p, f).endVertex();
//        bufferBuilder.vertex(matrix4f, (float) k, (float) l, (float) m).color(r, s, t, q).endVertex();
//    }

    @Override
    public void drawQuad(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int offset, int color) {
        drawQuad(getStyleProvider().gui(), x0, y0, x1, y1, x2, y2, x3, y3, offset, color);
    }

    @Override
    public int drawText(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return proxy.drawString(MinecraftClientAdapter.adapt(typeface), string, x, y, color, shadow);
    }

    @Override
    public int drawText(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return proxy.drawString(MinecraftClientAdapter.adapt(typeface), MinecraftClientAdapter.adapt(text), x, y, color, shadow);
    }

    @Override
    public void drawScrollingText(Typeface typeface, Text text, int x0, int y0, int x1, int y1, int color) {
        var textWidth = MinecraftClientAdapter.adapt(typeface).width(MinecraftClientAdapter.adapt(text));
        int containerHeight = (y0 + y1 - 9) / 2 + 1;
        int containerWidth = x1 - x0;
        if (textWidth > containerWidth) {
            var paddingWidth = 8;
            int extraWidth = textWidth - containerWidth + paddingWidth;
            var d = Util.getMillis() / 1000.0;
            var e = MathUtils.max(extraWidth * 0.5, 3.0);
            var f = MathUtils.sin(1.5707963267948966 * MathUtils.cos(6.283185307179586 * d / e)) / 2.0 + 0.5;
            var x = MathUtils.lerp(f, 0.0, extraWidth);
            proxy.enableScissor(x0, y0, x1, y1);
            proxy.drawString(MinecraftClientAdapter.adapt(typeface), MinecraftClientAdapter.adapt(text), x0 - (int) x + paddingWidth / 2, containerHeight, color, true);
            proxy.disableScissor();
        } else {
            drawTextFromCenter(typeface, text, (x0 + x1) / 2, containerHeight, color, true);
        }
    }

    @Override
    public void drawTexture(Resource texture, int x, int y, int x0, int y0, int width, int height) {
        proxy.blit(MinecraftClientAdapter.adapt(texture), x, y, x0, y0, width, height);
    }

    @Override
    public void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int textureWidth, int textureHeight) {
        proxy.blit(MinecraftClientAdapter.adapt(texture), x, y, x0, y0, width, height, textureWidth, textureHeight);
    }

    @Override
    public void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int textureWidth, int textureHeight, int z) {
        proxy.blit(MinecraftClientAdapter.adapt(texture), x, y, z, x0, y0, width, height, textureWidth, textureHeight);
    }

    @Override
    public void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        proxy.blit(MinecraftClientAdapter.adapt(texture), x, y, width, height, x0, y0, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    @Override
    public void drawPanelBackgroundTexture(int x, int y, int width, int height, float x0, float y0) {
        proxy.blit(BACKGROUND_LOCATION, x, y, 0, x0, y0, width, height, 32, 32);
    }

    @Override
    public void drawWidgetBackgroundTexture(int x, int y, int width, int height, boolean active, boolean focused) {
        proxy.blitSprite(BUTTON_SPRITES.get(active, focused), x, y, width, height);
    }

    @Override
    public void drawItemSlotBackgroundTexture(int x, int y) {
        proxy.blit(STATS_ICONS_LOCATION, x, y, 0, 0F, 0F, 18, 18, 128, 128);
    }

    @Override
    public void drawItem(ItemStack stack, int x, int y) {
        proxy.renderItem(MinecraftClientAdapter.adapt(stack), x, y);
    }

    @Override
    public void drawItem(Typeface typeface, ItemStack stack, int x, int y, @Nullable Text count) {
        proxy.renderItem(MinecraftClientAdapter.adapt(stack), x + 1, y + 1);
        proxy.pose().pushPose();
        proxy.pose().translate(0, 0, 200F);
        proxy.renderItemDecorations(MinecraftClientAdapter.adapt(typeface), MinecraftClientAdapter.adapt(stack), x, y, MinecraftClientAdapter.adapt(count).getString());
        proxy.pose().popPose();
    }

    @Override
    public void drawTooltip(Typeface typeface, List<Text> list, int x, int y) {
        proxy.renderTooltip(MinecraftClientAdapter.adapt(typeface), list.stream().map(MinecraftAdapter::adapt).toList(), Optional.empty(), x, y);
    }

    @Override
    public void drawTooltip(Typeface typeface, ItemStack itemStack, int x, int y) {
        proxy.renderTooltip(MinecraftClientAdapter.adapt(typeface), MinecraftClientAdapter.adapt(itemStack), x, y);
    }

    @Override
    public void drawQuad(RenderStyle renderStyle, int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int offset, int color) {
        var matrix4f = proxy.pose().last().pose();
        var f = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        var g = (float) FastColor.ARGB32.red(color) / 255.0F;
        var h = (float) FastColor.ARGB32.green(color) / 255.0F;
        var p = (float) FastColor.ARGB32.blue(color) / 255.0F;
        VertexConsumer vertexConsumer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderStyle));
        vertexConsumer.vertex(matrix4f, (float) x0, (float) y0, (float) offset).color(g, h, p, f).endVertex();
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, (float) offset).color(g, h, p, f).endVertex();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, (float) offset).color(g, h, p, f).endVertex();
        vertexConsumer.vertex(matrix4f, (float) x3, (float) y3, (float) offset).color(g, h, p, f).endVertex();
    }

    @Override
    public void drawBlockModelByCamera(World world, BlockPosition blockPosition, BlockData blockData, Color color) {
        var SCALE = 129 / 128f;
        var camera = getCameraPosition();
        var dispatcher = Minecraft.getInstance().getBlockRenderer();
        var renderLayer = getStyleProvider().solid(color);
        var buffer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderLayer));
        var worldRef = MinecraftClientAdapter.adapt(world);
        var blockStateRef = MinecraftClientAdapter.adapt(blockData);
        var blockPosRef = MinecraftClientAdapter.adapt(blockPosition);
        var model = dispatcher.getBlockModel(blockStateRef);
        var seed = blockStateRef.getSeed(blockPosRef);

        pushPose();
        translate(blockPosRef.getX() - camera.getX(), blockPosRef.getY() - camera.getY(), blockPosRef.getZ() - camera.getZ());
        translate((SCALE - 1) / -2, (SCALE - 1) / -2, (SCALE - 1) / -2);
        scale(SCALE, SCALE, SCALE);
        dispatcher.getModelRenderer().tesselateBlock(worldRef, model, blockStateRef, blockPosRef, proxy.pose(), buffer, false, RAND, seed, OverlayTexture.NO_OVERLAY);
        popPose();
    }

    @Override
    public void drawQuad(RenderStyle renderStyle, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4,
                         int uv2, int color, Orientation normal) {
        drawQuadUV(renderStyle, v1, v2, v3, v4, 0, 0, 1, 1, uv2, color, normal);
    }

    private void putVertex(VertexConsumer vertexConsumer, Vector3d pos, float u, float v, int uv2, int color, Orientation normal) {
        putVertex(vertexConsumer, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ(), u, v, uv2, color, normal);
    }

    @Override
    public void drawQuadUV(RenderStyle renderStyle, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
                           float minV, float maxU, float maxV, int uv2, int color, Orientation normal) {
        var vertexConsumer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderStyle));
        putVertex(vertexConsumer, v1, minU, minV, uv2, color, normal);
        putVertex(vertexConsumer, v2, maxU, minV, uv2, color, normal);
        putVertex(vertexConsumer, v3, maxU, maxV, uv2, color, normal);
        putVertex(vertexConsumer, v4, minU, maxV, uv2, color, normal);
    }

    private void putVertex(VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int uv2, int color, Orientation normal) {
        var xOffset = 0;
        var yOffset = 0;
        var zOffset = 0;

        if (normal != null) {
            xOffset = normal.getStepX();
            yOffset = normal.getStepY();
            zOffset = normal.getStepZ();
        }
        var pose = proxy.pose().last();
        vertexConsumer.vertex(pose.pose(), x, y, z)
                .color(color)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(uv2)
                .normal(pose.normal(), xOffset, yOffset, zOffset)
                .endVertex();

    }

    @Override
    public RenderStyleProvider getStyleProvider() {
        return RENDER_STYLE_PROVIDER;
    }
}
