package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.texture.BlockRenderTextures;
import dev.huskuraft.effortless.renderer.texture.OutlineRenderTextures;
import dev.huskuraft.effortless.renderer.texture.RenderTextures;
import dev.huskuraft.effortless.text.Text;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Renderer {

    public abstract int windowWidth();

    public abstract int windowHeight();

    public abstract int optionColor(float alpha);

    public abstract Camera camera();

    public abstract void pushPose();

    public abstract void popPose();

    public abstract Matrix4f lastPose();

    public abstract Matrix3f lastPoseNormal();

    public void pushLayer() {
    }

    public void popLayer() {
    }

    public final void translate(Vector3d vector) {
        this.translate(vector.x(), vector.y(), vector.z());
    }

    public final void translate(double x, double y, double z) {
        this.translate((float) x, (float) y, (float) z);
    }

    public final void translate(float x, float y, float z) {
        this.lastPose().translate(x, y, z);
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
        this.lastPose().scale(x, y, z);
        if (x == y && y == z) {
            if (x > 0.0F) {
                return;
            }
            this.lastPoseNormal().scale(-1.0F);
        }
        var f = 1.0F / x;
        var g = 1.0F / y;
        var h = 1.0F / z;
        var i = RenderUtils.fastInvCubeRoot(f * g * h);
        this.lastPoseNormal().scale(i * f, i * g, i * h);
    }

    public final void rotate(Quaternionf quaternion) {
        this.lastPose().rotate(quaternion);
        this.lastPoseNormal().rotate(quaternion);
    }

    public final void rotate(Quaternionf quaternion, float x, float y, float z) {
        this.lastPose().rotateAround(quaternion, x, y, z);
        this.lastPoseNormal().rotate(quaternion);
    }

    public final void multiply(Matrix4f matrix) {
        this.lastPose().mul(matrix);
    }

    public abstract void enableScissor(int x1, int y1, int x2, int y2);

    public abstract void disableScissor();

    public abstract void setShaderColor(float red, float green, float blue, float alpha);

    public abstract VertexBuffer vertexBuffer(RenderTexture renderTexture);

    public abstract void flush();

    public final void renderHLine(int x1, int x2, int y, int color) {
        this.renderHLine(this.renderTextures().gui(), x1, x2, y, color);
    }

    public final void renderHLine(RenderTexture renderTexture, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        this.renderRect(renderTexture, x1, y, x2 + 1, y + 1, color);
    }

    public final void renderVLine(int x, int y1, int y2, int color) {
        this.renderVLine(this.renderTextures().gui(), x, y1, y2, color);
    }

    public final void renderVLine(RenderTexture renderTexture, int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        this.renderRect(renderTexture, x, y1 + 1, x + 1, y2, color);
    }

    public final void renderLine(RenderTexture renderTexture, Vector3d v1, Vector3d v2, int uv2, int color) {
        var buffer = this.vertexBuffer(renderTexture);
        buffer.vertex(lastPose(), v1).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
        buffer.vertex(lastPose(), v2).uv(1, 1).uv2(uv2).color(color).overlayCoords(OverlayTexture.NO_OVERLAY).endVertex();
    }


    public final void renderBorder(int x, int y, int width, int height, int color) {
        this.renderRect(x, y, x + width, y + 1, color);
        this.renderRect(x, y + height - 1, x + width, y + height, color);
        this.renderRect(x, y + 1, x + 1, y + height - 1, color);
        this.renderRect(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    public final void renderRect(int x1, int y1, int x2, int y2, int color) {
        this.renderRect(this.renderTextures().gui(), x1, y1, x2, y2, color, 0);
    }

    public final void renderRect(int x1, int y1, int x2, int y2, int color, int z) {
        this.renderRect(this.renderTextures().gui(), x1, y1, x2, y2, color, z);
    }

    public final void renderRect(RenderTexture renderTexture, int x1, int y1, int x2, int y2, int color) {
        this.renderRect(renderTexture, x1, y1, x2, y2, color, 0);
    }

    public final void renderRect(RenderTexture renderTexture, int x1, int y1, int x2, int y2, int color, int z) {
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

        var buffer = this.vertexBuffer(renderTexture);

        buffer.vertex(lastPose(), x1, y1, z).color(color).endVertex();
        buffer.vertex(lastPose(), x1, y2, z).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y2, z).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y1, z).color(color).endVertex();
        this.flush();
    }


    public final void renderGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
        this.renderGradientRect(this.renderTextures().gui(), x1, y1, x2, y2, color1, color2, 0);
    }

    public final void renderGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        this.renderGradientRect(this.renderTextures().gui(), x1, y1, x2, y2, color1, color2, z);
    }

    public final void renderGradientRect(RenderTexture renderTexture, int x1, int y1, int x2, int y2, int color1, int color2) {
        this.renderGradientRect(renderTexture, x1, y1, x2, y2, color1, color2, 0);
    }

    public final void renderGradientRect(RenderTexture renderTexture, int x1, int y1, int x2, int y2, int color1, int color2, int z) {
        var buffer = this.vertexBuffer(renderTexture);
        buffer.vertex(lastPose(), x1, y1, z).color(color1).endVertex();
        buffer.vertex(lastPose(), x1, y2, z).color(color2).endVertex();
        buffer.vertex(lastPose(), x2, y2, z).color(color2).endVertex();
        buffer.vertex(lastPose(), x2, y1, z).color(color1).endVertex();
        flush();
    }

    public final void renderQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        this.renderQuad(this.renderTextures().gui(), x1, y1, x2, y2, x3, y3, x4, y4, offset, color);
    }

    public final void renderQuad(RenderTexture renderTexture, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int offset, int color) {
        var buffer = this.vertexBuffer(renderTexture);

        buffer.vertex(lastPose(), x1, y1, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x2, y2, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x3, y3, offset).color(color).endVertex();
        buffer.vertex(lastPose(), x4, y4, offset).color(color).endVertex();
    }

    public final void renderQuad(RenderTexture renderTexture, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int uv2, int color, Orientation normal) {
        this.drawQuadUV(renderTexture, v1, v2, v3, v4, 0, 0, 1, 1, uv2, color, normal);
    }

    public final void drawQuadUV(RenderTexture renderTexture, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
                                 float minV, float maxU, float maxV, int uv2, int color, Orientation normal) {
        var buffer = this.vertexBuffer(renderTexture);

        buffer.vertex(lastPose(), v1).color(color).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v2).color(color).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v3).color(color).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
        buffer.vertex(lastPose(), v4).color(color).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2).normal(lastPoseNormal(), normal).endVertex();
    }

    public abstract int renderText(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, FontDisplay mode, int lightMap);

    public final int renderText(Typeface typeface, String string, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, Text.text(string), x, y, color, 0, shadow, FontDisplay.NORMAL, LightTexture.FULL_BRIGHT);
    }

    public final int renderText(Typeface typeface, Text text, int x, int y, int color, boolean shadow) {
        return this.renderText(typeface, text, x, y, color, 0, shadow, FontDisplay.NORMAL, LightTexture.FULL_BRIGHT);
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

    public abstract void renderItem(ItemStack stack, int x, int y);

    public final void renderItem(Typeface typeface, ItemStack stack, int x, int y, @Nullable Text text) {
        this.renderItem(stack, x + 1, y + 1);
        this.pushPose();
        this.translate(0, 0, 200F);
        this.renderText(typeface, text, x + 19 - 2 - typeface.measureWidth(text), y + 6 + 3, 16777215, true);
        this.popPose();
    }

    public abstract void renderTooltip(Typeface typeface, List<Text> list, int x, int y);

    public abstract void renderTooltip(Typeface typeface, ItemStack itemStack, int x, int y);


    public abstract void renderBlockInWorld(RenderTexture renderTexture, World world, BlockPosition blockPosition, BlockState blockState);

    public abstract RenderTextures renderTextures();

    public abstract BlockRenderTextures blockRenderTextures();

    public abstract OutlineRenderTextures outlineRenderTextures();

}
