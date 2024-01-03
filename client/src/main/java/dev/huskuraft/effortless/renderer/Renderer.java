package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.*;
import dev.huskuraft.effortless.renderer.texture.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.texture.OutlineRenderLayers;
import dev.huskuraft.effortless.renderer.texture.RenderLayers;
import dev.huskuraft.effortless.text.Text;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public abstract class Renderer {

    public int optionColor(float alpha) {
//        return Minecraft.getInstance().options.getBackgroundColor(alpha);
        return new Color(0f, 0f, 0f, 0.95f * alpha).getRGB();
    }

    public abstract Window window();

    public abstract Camera camera();

    public abstract MatrixStack matrixStack();

    public abstract BufferSource bufferSource();

    public void pushLayer() {
    }

    public void popLayer() {
    }

    public final void pushPose() {
        matrixStack().push();
    }

    public final void popPose() {
        matrixStack().pop();
    }

    public final Matrix4f lastMatrixPose() {
        return matrixStack().last().pose();
    }

    public final Matrix3f lastMatrixNormal() {
        return matrixStack().last().normal();
    }

    public final void translate(Vector3d vector) {
        this.translate(vector.x(), vector.y(), vector.z());
    }

    public final void translate(double x, double y, double z) {
        this.translate((float) x, (float) y, (float) z);
    }

    public final void translate(float x, float y, float z) {
        this.matrixStack().translate(x, y, z);
    }

    public final void scale(double n) {
        this.scale(n, n, n);
    }

    public final void scale(Vector3d vector) {
        this.scale(vector.x(), vector.y(), vector.z());
    }

    public final void scale(double x, double y, double z) {
        this.scale((float) x, (float) y, (float) z);
    }

    public final void scale(float x, float y, float z) {
        this.matrixStack().scale(x, y, z);
    }

    public final void rotate(Quaternionf quaternion) {
        this.matrixStack().rotate(quaternion);
    }

    public final void rotate(Quaternionf quaternion, float x, float y, float z) {
        this.matrixStack().rotate(quaternion, x, y, z);
    }


    //    public final void rotate(Quaternionf quaternion, float x, float y, float z) {
//        this.lastPose().rotateAround(quaternion, x, y, z);
//        this.lastPoseNormal().rotate(quaternion);
//    }
//
    public final void multiply(Matrix4f matrix) {
        this.matrixStack().multiply(matrix);
    }

    protected abstract void enableScissorInternal(int x1, int y1, int x2, int y2);

    protected abstract void disableScissorInternal();

    private final ScissorStack scissorStack = new ScissorStack();

    public final void enableScissor(int x1, int y1, int x2, int y2) {
        this.applyScissor(this.scissorStack.push(new ScreenRect(x1, y1, x2 - x1, y2 - y1)));
    }

    public final void enableScissor(ScreenRect rect) {
        this.applyScissor(this.scissorStack.push(rect));
    }

    public final void disableScissor() {
        this.applyScissor(this.scissorStack.pop());
    }

    private void applyScissor(ScreenRect rect) {
//        this.flushIfManaged();
        if (rect == null) {
            disableScissorInternal();
        } else {
            var height = window().getHeight();
            var scale = window().getGuiScaledFactor();
            var d1 = rect.left() * scale;
            var d2 = height - rect.bottom() * scale;
            var d3 = rect.width() * scale;
            var d4 = rect.height() * scale;
            enableScissorInternal((int) d1, (int) d2, Math.max(0, (int) d3), Math.max(0, (int) d4));
        }
    }

    private static class ScissorStack {

        private final Deque<ScreenRect> stack = new ArrayDeque<>();

        public ScreenRect push(ScreenRect pScissor) {
            var rect = this.stack.peekLast();
            if (rect != null) {
                var rect1 = Objects.requireNonNullElse(pScissor.intersection(rect), ScreenRect.empty());
                this.stack.addLast(rect1);
                return rect1;
            } else {
                this.stack.addLast(pScissor);
                return pScissor;
            }
        }

        @Nullable
        public ScreenRect pop() {
            if (this.stack.isEmpty()) {
                throw new IllegalStateException("Scissor stack underflow");
            } else {
                this.stack.removeLast();
                return this.stack.peekLast();
            }
        }
    }

    public abstract void setShaderColor(float red, float green, float blue, float alpha);

    public VertexBuffer vertexBuffer(RenderLayer renderLayer) {
        return bufferSource().getBuffer(renderLayer);
    }

    public abstract void flush();

    public final void renderHLine(int x1, int x2, int y, int color) {
        this.renderHLine(this.renderLayers().gui(), x1, x2, y, color);
    }

    public final void renderHLine(RenderLayer renderLayer, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        this.renderRect(renderLayer, x1, y, x2 + 1, y + 1, color);
    }

    public final void renderVLine(int x, int y1, int y2, int color) {
        this.renderVLine(this.renderLayers().gui(), x, y1, y2, color);
    }

    public final void renderVLine(RenderLayer renderLayer, int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        this.renderRect(renderLayer, x, y1 + 1, x + 1, y2, color);
    }

    public final void renderLine(RenderLayer renderLayer, Vector3d v1, Vector3d v2, int uv2, int color) {
        var buffer = this.vertexBuffer(renderLayer);
        buffer.vertex(lastMatrixPose(), v1).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        buffer.vertex(lastMatrixPose(), v2).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
    }


    public final void renderBorder(int x, int y, int width, int height, int color) {
        this.renderRect(x, y, x + width, y + 1, color);
        this.renderRect(x, y + height - 1, x + width, y + height, color);
        this.renderRect(x, y + 1, x + 1, y + height - 1, color);
        this.renderRect(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    public final void renderRect(int x1, int y1, int x2, int y2, int color) {
        this.renderRect(this.renderLayers().gui(), x1, y1, x2, y2, color, 0);
    }

    public final void renderRect(int x1, int y1, int x2, int y2, int color, int z) {
        this.renderRect(this.renderLayers().gui(), x1, y1, x2, y2, color, z);
    }

    public final void renderRect(RenderLayer renderLayer, int x1, int y1, int x2, int y2, int color) {
        this.renderRect(renderLayer, x1, y1, x2, y2, color, 0);
    }

    public final void renderRect(RenderLayer renderLayer, int x1, int y1, int x2, int y2, int color, int z) {
        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        var buffer = this.vertexBuffer(renderLayer);

        buffer.vertex(lastMatrixPose(), x1, y1, z).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x1, y2, z).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x2, y2, z).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x2, y1, z).color(color).endVertex();
        this.flush();
    }

    public final void renderGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
        this.renderGradientRect(this.renderLayers().gui(), x1, y1, x2, y2, color1, color2, 0);
    }

    public final void renderGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        this.renderGradientRect(this.renderLayers().gui(), x1, y1, x2, y2, color1, color2, z);
    }

    public final void renderGradientRect(RenderLayer renderLayer, int x1, int y1, int x2, int y2, int color1, int color2) {
        this.renderGradientRect(renderLayer, x1, y1, x2, y2, color1, color2, 0);
    }

    public final void renderGradientRect(RenderLayer renderLayer, int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        var buffer = this.vertexBuffer(renderLayer);
        buffer.vertex(lastMatrixPose(), x1, y1, z).color(color1).endVertex();
        buffer.vertex(lastMatrixPose(), x1, y2, z).color(color2).endVertex();
        buffer.vertex(lastMatrixPose(), x2, y2, z).color(color2).endVertex();
        buffer.vertex(lastMatrixPose(), x2, y1, z).color(color1).endVertex();
        flush();
    }

    public final void renderQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        this.renderQuad(this.renderLayers().gui(), x1, y1, x2, y2, x3, y3, x4, y4, offset, color);
    }

    public final void renderQuad(RenderLayer renderLayer, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        var buffer = this.vertexBuffer(renderLayer);

        buffer.vertex(lastMatrixPose(), x1, y1, offset).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x2, y2, offset).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x3, y3, offset).color(color).endVertex();
        buffer.vertex(lastMatrixPose(), x4, y4, offset).color(color).endVertex();
    }

    public final void renderQuad(RenderLayer renderLayer, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int uv2, int color, Orientation normal) {
        this.drawQuadUV(renderLayer, v1, v2, v3, v4, 0, 0, 1, 1, uv2, color, normal);
    }

    public final void drawQuadUV(RenderLayer renderLayer, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
                                 float minV, float maxU, float maxV, int uv2, int color, Orientation normal) {
        var buffer = this.vertexBuffer(renderLayer);

        buffer.vertex(lastMatrixPose(), v1).color(color).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastMatrixNormal(), normal).endVertex();
        buffer.vertex(lastMatrixPose(), v2).color(color).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastMatrixNormal(), normal).endVertex();
        buffer.vertex(lastMatrixPose(), v3).color(color).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastMatrixNormal(), normal).endVertex();
        buffer.vertex(lastMatrixPose(), v4).color(color).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastMatrixNormal(), normal).endVertex();
    }

    protected abstract int renderTextInternal(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, boolean seeThrough, int lightMap);

    public int renderText(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, boolean seeThrough, int lightMap) {
        var width = this.renderTextInternal(typeface, text, x, y, color, backgroundColor, shadow, seeThrough, lightMap);
        flush();
        return width;
    }

    public final int renderText(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, Text.text(string), x, y, color, 0, shadow, false, LightTexture.FULL_BRIGHT);
    }

    public final int renderText(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, text, x, y, color, 0, shadow, false, LightTexture.FULL_BRIGHT);
    }

    public final int renderTextFromStart(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, string, x, y, color, shadow);
    }

    public final int renderTextFromStart(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, text, x, y, color, shadow);
    }

    public final int renderTextFromCenter(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, string, x - typeface.measureWidth(string) / 2, y, color, shadow);
    }

    public final int renderTextFromCenter(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, text, x - typeface.measureWidth(text) / 2, y, color, shadow);
    }

    public final int renderTextFromEnd(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, string, x - typeface.measureWidth(string), y, color, shadow);
    }

    public final int renderTextFromEnd(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, text, x - typeface.measureWidth(text), y, color, shadow);
    }

    public final void renderScrollingText(Typeface typeface, Text text, int x1, int y1, int x2, int y2, int color) {
        var textWidth = typeface.measureWidth(text);
        int containerHeight = (y1 + y2 - 9) / 2 + 1;
        int containerWidth = x2 - x1;
        if (textWidth > containerWidth) {
            var paddingWidth = 8;
            int extraWidth = textWidth - containerWidth + paddingWidth;
            var d = System.currentTimeMillis() / 1000.0;
            var e = MathUtils.max(extraWidth * 0.5, 3.0);
            var f = MathUtils.sin(1.5707963267948966 * MathUtils.cos(6.283185307179586 * d / e)) / 2.0 + 0.5;
            var x = MathUtils.lerp(f, 0.0, extraWidth);
            this.enableScissor(x1, y1, x2, y2);
            this.renderText(typeface, text, x1 - (int) x + paddingWidth / 2, containerHeight, color, true);
            this.disableScissor();
        } else {
            this.renderTextFromCenter(typeface, text, (x1 + x2) / 2, containerHeight, color, true);
        }
    }

    public final void renderTexture(Resource resource, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.renderTexture(resource, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    protected final void renderTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        this.renderTexture(resource, x1, x2, y1, y2, blitOffset, uOffset / textureWidth, (uOffset + uWidth) / textureWidth, vOffset / textureHeight, (vOffset + vHeight) / textureHeight);
    }

    protected abstract void renderTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV);

    public abstract void renderPanelBackgroundTexture(int x, int y, float uOffset, float vOffset, int uWidth, int vHeight);

    public abstract void renderButtonTexture(int x, int y, int width, int height, boolean active, boolean focused);

//    @Override
//    protected void renderItemInternal(ItemStack itemStack, int seed, boolean fake)
//    {
//        var minecraftItemStack = MinecraftItemStack.toMinecraftItemStack(itemStack);
//        var minecraftPlayer = fake ? null : minecraftClient.player;
//        var minecraftWorld = minecraftClient.level;
//        var minecraftModel = minecraftClient.getItemRenderer().getModel(minecraftItemStack, minecraftWorld, minecraftPlayer, seed);
//        if (!minecraftModel.usesBlockLight()) Lighting.setupForFlatItems();
//        minecraftClient.getItemRenderer().render(minecraftItemStack, ItemDisplayContext.GUI, false, minecraftMatrixStack, minecraftBufferSource, 15728880, OverlayTexture.NO_OVERLAY, minecraftModel);
//        this.flush();
//        if (!minecraftModel.usesBlockLight()) Lighting.setupFor3DItems();
//
//        minecraftClient.getItemRenderer().renderStatic();
//    }
//    {
//        pushPose();
//        translate(x + 8, y + 8, 150);
//        multiply((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
//        scale(16.0F, 16.0F, 16.0F);
//        renderItemInternal(stack, 0, false);
//        popPose();
//    }

    public abstract void renderItem(ItemStack stack, int x, int y);

    public final void renderItem(Typeface typeface, ItemStack stack, int x, int y, @Nullable Text text) {
        this.renderItem(stack, x + 1, y + 1);
        this.pushPose();
        this.translate(0, 0, 200F);
        this.renderText(typeface, text, x + 19 - 2 - typeface.measureWidth(text), y + 6 + 3, 16777215, true);
        this.popPose();
    }

    public abstract void renderTooltip(Typeface typeface, List<Text> list, int x, int y);

    public abstract void renderBlockInWorld(RenderLayer renderLayer, World world, BlockPosition blockPosition, BlockState blockState);

    public abstract RenderLayers renderLayers();

    public abstract BlockRenderLayers blockRenderLayers();

    public abstract OutlineRenderLayers outlineRenderLayers();

}
