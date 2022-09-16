package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public abstract class Renderer {

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public abstract int getOptionColor(float alpha);

    public abstract Vector3d getCameraPosition();

    public abstract void pushPose();

    public abstract void popPose();

    public abstract void pushLayer();

    public abstract void popLayer();

    public abstract void translate(double d, double e, double f);

    public abstract void translate(float d, float e, float f);

    public abstract void scale(double d, double e, double f);

    public abstract void scale(float d, float e, float f);

    public abstract void enableScissor(int x1, int y1, int x2, int y2);

    public abstract void disableScissor();

    public abstract void setShaderColor(float red, float green, float blue, float alpha);

    public abstract void draw();

    public abstract void drawHorizontalLine(int x1, int x2, int y, int color);

    public abstract void drawHorizontalLine(RenderStyle renderStyle, int x1, int x2, int y, int color);

    public abstract void drawVerticalLine(int x, int y1, int y2, int color);

    public abstract void drawVerticalLine(RenderStyle renderStyle, int x, int y1, int y2, int color);

    public abstract void drawBorder(int i, int j, int k, int l, int m);

    public abstract void drawRect(int x1, int y1, int x2, int y2, int color);

    public abstract void drawRect(int x1, int y1, int x2, int y2, int color, int z);

    public abstract void drawRect(RenderStyle renderStyle, int x1, int y1, int x2, int y2, int color);

    public abstract void drawRect(RenderStyle renderStyle, int x1, int y1, int x2, int y2, int color, int z);

    public abstract void drawGradientRect(int startX, int startY, int endX, int endY, int colorStart, int colorEnd);

    public abstract void drawGradientRect(int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z);

    public abstract void drawGradientRect(RenderStyle renderStyle, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z);

    public abstract void drawQuad(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int offset, int color);

    public abstract void drawQuad(RenderStyle renderStyle, int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int offset, int color);

    // TODO: 9/10/23 return void
    public abstract int drawText(Typeface typeface, String string, int x, int y, int color, boolean shadow);

    public abstract int drawText(Typeface typeface, Text text, int x, int y, int color, boolean shadow);

    public abstract int drawCenteredText(Typeface typeface, String string, int x, int y, int color, boolean shadow);

    public abstract int drawCenteredText(Typeface typeface, Text text, int x, int y, int color, boolean shadow);

    public abstract void drawScrollingText(Typeface typeface, Text text, int x0, int y0, int x1, int y1, int color);

    public abstract void drawTexture(Resource texture, int x, int y, int x0, int y0, int width, int height);

    public abstract void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int textureWidth, int textureHeight);

    public abstract void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int textureWidth, int textureHeight, int z);

    public abstract void drawTexture(Resource texture, int x, int y, float x0, float y0, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight);

    public abstract void drawPanelBackgroundTexture(int x, int y, int width, int height, float x0, float y0);

    public abstract void drawWidgetBackgroundTexture(int x, int y, int width, int height, boolean active, boolean focused);

    public abstract void drawItemSlotBackgroundTexture(int x, int y);

    public abstract void drawItem(ItemStack stack, int x, int y);

    public abstract void drawItem(Typeface typeface, ItemStack stack, int x, int y, @Nullable Text count);

    public abstract void drawTooltip(Typeface typeface, List<Text> list, int x, int y);

    public abstract void drawTooltip(Typeface typeface, ItemStack itemStack, int x, int y);

    public abstract void drawBlockModelByCamera(World world, BlockPosition blockPosition, BlockData blockData, Color color);

    public abstract void drawQuad(RenderStyle renderStyle, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, int uv2, int color, Orientation normal);

    public abstract void drawQuadUV(RenderStyle renderStyle, Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, float minU,
                                    float minV, float maxU, float maxV, int uv2, int color, Orientation normal);

    public abstract RenderStyleProvider getStyleProvider();

}
