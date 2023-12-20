package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Renderer {

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public abstract int getOptionColor(float alpha);

    public abstract Camera camera();

    public abstract void pushPose();

    public abstract void popPose();

    public abstract Matrix4f lastPose();

    public abstract Matrix3f lastPoseNormal();

    public abstract void pushLayer();

    public abstract void popLayer();

    public final void translate(double x, double y, double z) {
        translate((float) x, (float) y, (float) z);
    }

    public final void translate(float x, float y, float z) {
        lastPose().translate(x, y, z);
    }

    public abstract void scale(double x, double y, double z);

    public abstract void scale(float x, float y, float z);

    public abstract void enableScissor(int x1, int y1, int x2, int y2);

    public abstract void disableScissor();

    public abstract void setShaderColor(float red, float green, float blue, float alpha);

    public abstract VertexBuffer vertexBuffer(RenderType renderType);

    public abstract void draw();

    public void drawHorizontalLine(int x1, int x2, int y, int color) {
        drawHorizontalLine(renderTypes().gui(), x1, x2, y, color);
    }

    public void drawHorizontalLine(RenderType renderType, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        drawRect(renderType, x1, y, x2 + 1, y + 1, color);
    }

    public void drawVerticalLine(int x, int y1, int y2, int color) {
        drawVerticalLine(renderTypes().gui(), x, y1, y2, color);
    }

    public void drawVerticalLine(RenderType renderType, int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        drawRect(renderType, x, y1 + 1, x + 1, y2, color);
    }

    public final void drawLine(RenderType renderType, Vector3d v1, Vector3d v2, int uv2, int color) {
        var buffer = vertexBuffer(renderType);
        buffer.vertex(lastPose(), v1).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        buffer.vertex(lastPose(), v2).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
    }


    public void drawBorder(int x, int y, int width, int height, int color) {
        drawRect(x, y, x + width, y + 1, color);
        drawRect(x, y + height - 1, x + width, y + height, color);
        drawRect(x, y + 1, x + 1, y + height - 1, color);
        drawRect(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    public void drawRect(int x1, int y1, int x2, int y2, int color) {
        drawRect(renderTypes().gui(), x1, y1, x2, y2, color, 0);
    }

    public void drawRect(int x1, int y1, int x2, int y2, int color, int z) {
        drawRect(renderTypes().gui(), x1, y1, x2, y2, color, z);
    }

    public void drawRect(RenderType renderType, int x1, int y1, int x2, int y2, int color) {
        drawRect(renderType, x1, y1, x2, y2, color, 0);
    }

    public void drawRect(RenderType renderType, int x1, int y1, int x2, int y2, int color, int z) {
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

        var buffer = vertexBuffer(renderType);

        buffer.vertex(lastPose(), x1, y1, z).color(color).endVertex();
        buffer.vertex(lastPose(), x1, y2, z).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y2, z).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y1, z).color(color).endVertex();
        draw();
    }


    public final void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
        drawGradientRect(renderTypes().gui(), x1, y1, x2, y2, color1, color2, 0);
    }

    public final void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        drawGradientRect(renderTypes().gui(), x1, y1, x2, y2, color1, color2, z);
    }

    public final void drawGradientRect(RenderType renderType, int x1, int y1, int x2, int y2, int color1, int color2) {
        drawGradientRect(renderType, x1, y1, x2, y2, color1, color2, 0);
    }

    public final void drawGradientRect(RenderType renderType, int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        var buffer = vertexBuffer(renderType);
        buffer.vertex(lastPose(), x1, y1, z).color(color1).endVertex();
        buffer.vertex(lastPose(), x1, y2, z).color(color2).endVertex();
        buffer.vertex(lastPose(), x2, y2, z).color(color2).endVertex();
        buffer.vertex(lastPose(), x2, y1, z).color(color1).endVertex();
        draw();
    }

    public void drawQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        drawQuad(renderTypes().gui(), x1, y1, x2, y2, x3, y3, x4, y4, offset, color);
    }

    public final void drawQuad(RenderType renderType, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        var buffer = vertexBuffer(renderType);

        buffer.vertex(lastPose(), x1, y1, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y2, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x3, y3, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x4, y4, offset).color(color).endVertex();
    }

    public final void drawQuad(RenderType renderType, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int uv2, int color, Orientation normal) {
        drawQuadUV(renderType, v1, v2, v3, v4, 0, 0, 1, 1, uv2, color, normal);
    }

    public final void drawQuadUV(RenderType renderType, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
                                 float minV, float maxU, float maxV, int uv2, int color, Orientation normal) {
        var buffer = vertexBuffer(renderType);

        buffer.vertex(lastPose(), v1).color(color).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v2).color(color).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v3).color(color).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v4).color(color).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
    }

//    public abstract void drawQuad(RenderType renderType, int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int offset, int color);

    public abstract int drawText(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, FontDisplay mode, int lightMap);

    public final int drawText(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return drawText(typeface, Text.text(string), x, y, color, 0, shadow, FontDisplay.NORMAL, LightTexture.FULL_BRIGHT);
    }

    public final int drawText(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return drawText(typeface, text, x, y, color, 0, shadow, FontDisplay.NORMAL, LightTexture.FULL_BRIGHT);
    }

    public final int drawTextFromStart(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return drawText(typeface, string, x, y, color, shadow);
    }

    public final int drawTextFromStart(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return drawText(typeface, text, x, y, color, shadow);
    }

    public final int drawTextFromCenter(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return drawText(typeface, string, x - typeface.measureWidth(string) / 2, y, color, shadow);
    }

    public final int drawTextFromCenter(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return drawText(typeface, text, x - typeface.measureWidth(text) / 2, y, color, shadow);
    }

    public final int drawTextFromEnd(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return drawText(typeface, string, x - typeface.measureWidth(string), y, color, shadow);
    }

    public final int drawTextFromEnd(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return drawText(typeface, text, x - typeface.measureWidth(text), y, color, shadow);
    }

    public final void drawScrollingText(Typeface typeface, Text text, int x1, int y1, int x2, int y2, int color) {
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
            enableScissor(x1, y1, x2, y2);
            drawText(typeface, text, x1 - (int) x + paddingWidth / 2, containerHeight, color, true);
            disableScissor();
        } else {
            drawTextFromCenter(typeface, text, (x1 + x2) / 2, containerHeight, color, true);
        }
    }

    public final void drawTexture(Resource resource, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        drawTexture(resource, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    protected final void drawTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        drawTexture(resource, x1, x2, y1, y2, blitOffset, uOffset / textureWidth, (uOffset + uWidth) / textureWidth, vOffset / textureHeight, (vOffset + vHeight) / textureHeight);
    }

    protected abstract void drawTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV);

    public abstract void drawPanelBackgroundTexture(int x, int y, float uOffset, float vOffset, int uWidth, int vHeight);

    public abstract void drawButtonTexture(int x, int y, int width, int height, boolean active, boolean focused);

    public abstract void drawItem(ItemStack stack, int x, int y);

    public final void drawItem(Typeface typeface, ItemStack stack, int x, int y, @Nullable Text text) {
        drawItem(stack, x + 1, y + 1);
        pushPose();
        translate(0, 0, 200F);
        drawText(typeface, text, x + 19 - 2 - typeface.measureWidth(text), y + 6 + 3, 16777215, true);
        popPose();
    }

    public abstract void drawTooltip(Typeface typeface, List<Text> list, int x, int y);

    public abstract void drawTooltip(Typeface typeface, ItemStack itemStack, int x, int y);


    public abstract void drawBlockInWorld(World world, BlockPosition blockPosition, BlockData blockData, int color);

//    public abstract void drawLine(RenderType renderType, Vector3d v1, Vector3d v2, int uv2, int color);
//
//    public abstract void drawQuad(RenderType renderType, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int uv2, int color, Orientation normal);
//
//    public abstract void drawQuadUV(RenderType renderType, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
//                                    float minV, float maxU, float maxV, int uv2, int color, Orientation normal);

    public abstract void drawNameTag(Typeface typeface, Text text);

    public abstract RenderTypes renderTypes();





}
