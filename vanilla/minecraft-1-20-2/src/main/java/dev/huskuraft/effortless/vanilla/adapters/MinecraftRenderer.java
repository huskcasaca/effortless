package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.*;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
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
    public Camera camera() {
        return new Camera() {
            @Override
            public Vector3d position() {
                return MinecraftClientAdapter.adapt(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
            }

            @Override
            public Quaternionf rotation() {
                return Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
            }

            @Override
            public float eyeHeight() {
                return Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().getEyeHeight();
            }
        };
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
    public Matrix4f lastPose() {
        return proxy.pose().last().pose();
    }

    @Override
    public Matrix3f lastPoseNormal() {
        return proxy.pose().last().normal();
    }

    @Override
    public void pushLayer() {
    }

    @Override
    public void popLayer() {
    }

    @Override
    public void translate(double x, double y, double z) {
        proxy.pose().translate(x, y, z);
    }

    @Override
    public void translate(float x, float y, float z) {
        proxy.pose().translate(x, y, z);
    }

    @Override
    public void scale(double x, double y, double z) {
        proxy.pose().scale((float) x, (float) y, (float) z);
    }

    @Override
    public void scale(float x, float y, float z) {
        proxy.pose().scale(x, y, z);
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
    public VertexBuffer vertexBuffer(RenderType renderType) {
        return new MinecraftVertexBuffer(proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderType)));
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
    public void drawHorizontalLine(RenderType renderType, int x1, int x2, int y, int color) {
        proxy.hLine(MinecraftClientAdapter.adapt(renderType), x1, x2, y, color);
    }

    @Override
    public void drawBorder(int i, int j, int k, int l, int m) {
        proxy.renderOutline(i, j, k, l, m);
    }

    @Override
    public void drawVerticalLine(RenderType renderType, int x, int y1, int y2, int color) {
        proxy.vLine(MinecraftClientAdapter.adapt(renderType), x, y1, y2, color);
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
    public void drawRect(RenderType renderType, int x1, int y1, int x2, int y2, int color) {
        drawRect(renderType, x1, y1, x2, y2, color, 0);
    }

    @Override
    public void drawRect(RenderType renderType, int x1, int y1, int x2, int y2, int color, int z) {
        proxy.fill(MinecraftClientAdapter.adapt(renderType), x1, y1, x2, y2, z, color);
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
    public void drawGradientRect(RenderType renderType, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        proxy.fillGradient(MinecraftClientAdapter.adapt(renderType), startX, startY, endX, endY, colorStart, colorEnd, z); // notice params order
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
        drawItem(stack, x + 1, y + 1);
        pushPose();
        translate(0, 0, 200F);
        proxy.renderItemDecorations(MinecraftClientAdapter.adapt(typeface), MinecraftClientAdapter.adapt(stack), x, y, MinecraftClientAdapter.adapt(count).getString());
        popPose();
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
    public void drawBlockInWorld(World world, BlockPosition blockPosition, BlockData blockData, int color) {
        var scale = 129 / 128f;
        var camera = camera().position();
        var dispatcher = Minecraft.getInstance().getBlockRenderer();
        var buffer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(getStyleProvider().solid(color)));
        var worldRef = MinecraftClientAdapter.adapt(world);
        var blockStateRef = MinecraftClientAdapter.adapt(blockData);
        var blockPosRef = MinecraftClientAdapter.adapt(blockPosition);

        pushPose();
        translate(blockPosRef.getX() - camera.getX(), blockPosRef.getY() - camera.getY(), blockPosRef.getZ() - camera.getZ());
        translate((scale - 1) / -2, (scale - 1) / -2, (scale - 1) / -2);
        scale(scale, scale, scale);
        dispatcher.getModelRenderer().tesselateBlock(
                worldRef,
                dispatcher.getBlockModel(blockStateRef),
                blockStateRef,
                blockPosRef,
                proxy.pose(),
                buffer,
                false,
                RAND,
                blockStateRef.getSeed(blockPosRef),
                OverlayTexture.NO_OVERLAY);
        popPose();
    }

    public void drawNameTag(Typeface typeface, Text text) {

        var distanceToSqr = 100;
        var component = MinecraftClientAdapter.adapt(text);
        if (distanceToSqr > 4096.0) {
            return;
        }
        var i = 15728880;
        PoseStack poseStack = proxy.pose();
        var bl = false; // !entity.isDiscrete();
        var f = 0f; //entity.getNameTagOffsetY();
        poseStack.pushPose();
        poseStack.translate(0.0F, f, 0.0F);
        poseStack.mulPose(camera().rotation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        var matrix4f = poseStack.last().pose();
        var g = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        var k = (int) (g * 255.0F) << 24;
        var font = MinecraftClientAdapter.adapt(typeface);
        var h = (float) (-font.width(component) / 2);
//
        font.drawInBatch(component, h, 0f, -1, false, matrix4f, proxy.bufferSource(), Font.DisplayMode.NORMAL, 0, i);

//        drawTextFromCenter(typeface, text, 0, 0, 0xffffff, false);
//        drawText(typeface, Text.text("Testing Text"), 0, 0, 0xffffff, false);
//        font.drawInBatch(component, h, 0f, 553648127, false, matrix4f, proxy.bufferSource(), bl ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, k, i);
//        if (bl) {
//            font.drawInBatch(component, h, 0f, -1, false, matrix4f, proxy.bufferSource(), Font.DisplayMode.NORMAL, 0, i);
//        }
//
        poseStack.popPose();
    }


    @Override
    public RenderStyleProvider getStyleProvider() {
        return RENDER_STYLE_PROVIDER;
    }
}
