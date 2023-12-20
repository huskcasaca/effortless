package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.*;
import dev.huskuraft.effortless.text.Text;
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

import java.util.List;
import java.util.Optional;

class MinecraftRenderer extends Renderer {

    @Deprecated
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("textures/gui/options_background.png");

    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    private static final RenderTypes RENDER_STYLE_PROVIDER = new MinecraftRenderTypes();
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
    public int drawText(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, FontDisplay mode, int lightMap) {
        var width = MinecraftClientAdapter.adapt(typeface).drawInBatch(MinecraftClientAdapter.adapt(text), x, y, color, shadow, lastPose(), proxy.bufferSource(), Font.DisplayMode.values()[mode.ordinal()], backgroundColor, lightMap);
        draw();
        return width;
    }

    @Override
    public void drawTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        proxy.innerBlit(MinecraftClientAdapter.adapt(resource), x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
    }

    @Override
    public void drawPanelBackgroundTexture(int x, int y, float uOffset, float vOffset, int uWidth, int vHeight) {
//        drawTexture(MinecraftClientAdapter.adapt(BACKGROUND_LOCATION), x, y, 0, uOffset, vOffset, uWidth, vHeight, 32, 32);
    }

    @Override
    public void drawButtonTexture(int x, int y, int width, int height, boolean active, boolean focused) {
        proxy.blitSprite(BUTTON_SPRITES.get(active, focused), x, y, width, height);
    }

    @Override
    public void drawItem(ItemStack stack, int x, int y) {
        proxy.renderItem(MinecraftClientAdapter.adapt(stack), x, y);
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
        var buffer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderTypes().solid(color)));
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
    public RenderTypes renderTypes() {
        return RENDER_STYLE_PROVIDER;
    }


}
