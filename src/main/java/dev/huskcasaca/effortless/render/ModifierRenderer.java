package dev.huskcasaca.effortless.render;

import com.mojang.blaze3d.vertex.*;
import dev.huskcasaca.effortless.buildmodifier.BuildModifierHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModifierRenderer {

    private static final Color COLOR_PLANE = new Color(0, 0, 0, 72);
    private static final Color COLOR_LINE = new Color(0, 0, 0, 200);
    private static final Vec3 EPSILON = new Vec3(0.001, 0.001, 0.001); //prevents z-fighting
    private static final ModifierRenderer INSTANCE = new ModifierRenderer();
    private final Minecraft minecraft;

    private ModifierRenderer() {
        this.minecraft = Minecraft.getInstance();
    }

    public static ModifierRenderer getInstance() {
        return INSTANCE;
    }

    public void render(Player player, PoseStack poseStack, MultiBufferSource.BufferSource multiBufferSource, Camera camera) {
        //Mirror lines and areas
        var mirrorSettings = BuildModifierHelper.getModifierSettings(player).mirrorSettings();
        poseStack.pushPose();
        var campos = camera.getPosition();
        poseStack.translate(-campos.x, -campos.y, -campos.z);

        if (mirrorSettings != null && mirrorSettings.enabled() && (mirrorSettings.mirrorX() || mirrorSettings.mirrorY() || mirrorSettings.mirrorZ())) {
            var pos = mirrorSettings.position();
            renderMirror(multiBufferSource, poseStack, pos, mirrorSettings.radius(), mirrorSettings.getMirrorAxis(), mirrorSettings.drawPlanes(), mirrorSettings.drawLines());
        }

        //Radial mirror lines and areas
        var radialMirrorSettings = BuildModifierHelper.getModifierSettings(player).radialMirrorSettings();
        if (radialMirrorSettings != null && radialMirrorSettings.enabled()) {
            var pos = radialMirrorSettings.position();
            renderRadial(multiBufferSource, poseStack, pos.add(EPSILON), radialMirrorSettings.radius(), radialMirrorSettings.slices(), radialMirrorSettings.drawPlanes(), radialMirrorSettings.drawLines());
        }
        poseStack.popPose();
    }

    private void drawAxisPlane(VertexConsumer buffer, PoseStack poseStack, Vec3 pos, Integer range, Direction.Axis axis, Color color) {

        var min = pos.subtract(range, range, range);
        var max = pos.add(range, range, range);
        var mat = poseStack.last().pose();

        switch (axis) {
            case Y -> {
                buffer.vertex(mat, (float) max.x(), (float) pos.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) min.x(), (float) pos.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) min.x(), (float) pos.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) max.x(), (float) pos.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            }
            case Z -> {
                buffer.vertex(mat, (float) max.x(), (float) min.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) min.x(), (float) min.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) min.x(), (float) max.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) max.x(), (float) max.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            }
            case X -> {
                buffer.vertex(mat, (float) pos.x(), (float) min.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) pos.x(), (float) min.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) pos.x(), (float) max.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                buffer.vertex(mat, (float) pos.x(), (float) max.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            }
        }
    }

    private void drawAxisLine(VertexConsumer buffer, PoseStack poseStack, Vec3 pos, Integer range, Direction.Axis axis, Color color) {
        var min = pos.subtract(range, range, range);
        var max = pos.add(range, range, range);
        var mat = poseStack.last().pose();

        switch (axis) {
            case Y -> {
                buffer.vertex(mat, (float) pos.x, (float) min.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
                buffer.vertex(mat, (float) pos.x, (float) max.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
            }
            case Z -> {
                buffer.vertex(mat, (float) pos.x, (float) pos.y, (float) min.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
                buffer.vertex(mat, (float) pos.x, (float) pos.y, (float) max.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
            }
            case X -> {
                buffer.vertex(mat, (float) min.x, (float) pos.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
                buffer.vertex(mat, (float) max.x, (float) pos.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
            }
        }
    }

    private void drawVerticalPlane(VertexConsumer buffer, PoseStack poseStack, Vec3 posA, Vec3 posB, Color color) {
        var min = posA;
        var max = posB;
        var mat = poseStack.last().pose();

        buffer.vertex(mat, (float) min.x(), (float) min.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.vertex(mat, (float) max.x(), (float) min.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.vertex(mat, (float) max.x(), (float) max.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.vertex(mat, (float) min.x(), (float) max.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    private void drawLine(VertexConsumer buffer, PoseStack poseStack, Vec3 posA, Vec3 posB, Color color) {
        var mat = poseStack.last().pose();

        buffer.vertex(mat, (float) posA.x, (float) posA.y, (float) posA.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
        buffer.vertex(mat, (float) posB.x, (float) posB.y, (float) posB.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
    }

    private void renderMirror(MultiBufferSource.BufferSource multiBufferSource, PoseStack poseStack, Vec3 pos, Integer radius, List<Direction.Axis> axis, boolean drawPlanes, boolean drawLines) {
        if (drawPlanes) {for (Direction.Axis a : axis) {
            VertexConsumer buffer = RenderUtils.beginPlanes(multiBufferSource);
            drawAxisPlane(buffer, poseStack, pos, radius, a, COLOR_PLANE);
            multiBufferSource.endBatch();
        }
        }
        if (drawLines) {
            for (Direction.Axis a : axis) {
                VertexConsumer buffer = RenderUtils.beginLines(multiBufferSource);
                for (Direction.Axis a1 : Direction.Axis.values()) {
                    if (a1 != a) {
                        drawAxisLine(buffer, poseStack, pos, radius, a1, COLOR_LINE);
                    }
                }
                multiBufferSource.endBatch();
            }
        }

    }

    private void renderRadial(MultiBufferSource.BufferSource multiBufferSource, PoseStack poseStack, Vec3 pos, Integer radius, Integer slices, boolean drawPlanes, boolean drawLines) {
        float angle = 2f * ((float) Math.PI) / slices;
        var relStartVec = new Vec3(radius, 0, 0);
        if (slices % 4 == 2) relStartVec = relStartVec.yRot(angle / 2f);

        if (drawPlanes) {
            for (int i = 0; i < slices; i++) {
                var relNewVec = relStartVec.yRot(angle * i);
                var newVec = pos.add(relNewVec);

                var posA = new Vec3(pos.x, pos.y - radius, pos.z);
                var posB = new Vec3(newVec.x, pos.y + radius, newVec.z);

                VertexConsumer buffer = RenderUtils.beginPlanes(multiBufferSource);
                drawVerticalPlane(buffer, poseStack, posA, posB, COLOR_PLANE);
                multiBufferSource.endBatch();
            }
        }
        if (drawLines) {
            for (int i = 0; i < slices; i++) {
                var relNewVec = relStartVec.yRot(angle * i);
                var newVec = pos.add(relNewVec);

                var posA = new Vec3(pos.x, pos.y - radius, pos.z);
                var posB = new Vec3(newVec.x, pos.y + radius, newVec.z);
                VertexConsumer buffer = RenderUtils.beginLines(multiBufferSource);
                drawLine(buffer, poseStack, new Vec3(posA.x(), pos.y(), posA.z()), new Vec3(posB.x(), pos.y(), posB.z()), COLOR_LINE);
                multiBufferSource.endBatch();
            }
            VertexConsumer buffer = RenderUtils.beginLines(multiBufferSource);
            drawLine(buffer, poseStack, new Vec3(pos.x(), pos.y() - radius, pos.z()), new Vec3(pos.x(), pos.y() + radius, pos.z()), COLOR_LINE);
            multiBufferSource.endBatch();
        }
    }

}

