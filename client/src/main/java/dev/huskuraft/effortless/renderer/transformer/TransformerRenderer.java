package dev.huskuraft.effortless.renderer.transformer;

import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.Renderer;

import java.awt.*;

public class TransformerRenderer {

    private static final Color COLOR_PLANE = new Color(0, 0, 0, 72);
    private static final Color COLOR_LINE = new Color(0, 0, 0, 200);
    private static final Vector3d EPSILON = new Vector3d(0.001, 0.001, 0.001); //prevents z-fighting

    public void tick() {

    }

    public void render(Renderer renderer, float deltaTick) {
//        //Mirror lines and areas
//        var player = minecraft.player;
//        var mirrorSettings = EffortlessBuilder.getInstance().getTransformerSettings(player).mirrorSettings();
//
//        if (mirrorSettings != null && mirrorSettings.enabled() && (mirrorSettings.mirrorX() || mirrorSettings.mirrorY() || mirrorSettings.mirrorZ())) {
//            var pos = mirrorSettings.position().subtract(camera.getPosition());
//            renderMirror(multiBufferSource, pos, mirrorSettings.radius(), mirrorSettings.getMirrorAxis(), mirrorSettings.drawPlanes(), mirrorSettings.drawLines());
//        }
//
//        // radial mirror lines and areas
//        var radialMirrorSettings = EffortlessBuilder.getInstance().getTransformerSettings(player).radialMirrorSettings();
//        if (radialMirrorSettings != null && radialMirrorSettings.enabled()) {
//            var pos = radialMirrorSettings.position().subtract(camera.getPosition());
//            renderRadial(multiBufferSource, pos.add(EPSILON), radialMirrorSettings.radius(), radialMirrorSettings.slices(), radialMirrorSettings.drawPlanes(), radialMirrorSettings.drawLines());
//        }
    }

//    private void drawAxisPlane(VertexConsumer buffer, Vec3 pos, Integer range, Axis axis, Color color) {
//
//        var min = pos.subtract(range, range, range);
//        var max = pos.add(range, range, range);
//
//        switch (axis) {
//            case Y -> {
//                buffer.vertex((float) max.x(), (float) pos.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) min.x(), (float) pos.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) min.x(), (float) pos.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) max.x(), (float) pos.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//            case Z -> {
//                buffer.vertex((float) max.x(), (float) min.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) min.x(), (float) min.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) min.x(), (float) max.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) max.x(), (float) max.y(), (float) pos.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//            case X -> {
//                buffer.vertex((float) pos.x(), (float) min.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) pos.x(), (float) min.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) pos.x(), (float) max.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex((float) pos.x(), (float) max.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//        }
//    }
//
//    private void drawAxisLine(VertexConsumer buffer, Vec3 pos, Integer range, Axis axis, Color color) {
//        var min = pos.subtract(range, range, range);
//        var max = pos.add(range, range, range);
//
//        switch (axis) {
//            case Y -> {
//                buffer.vertex((float) pos.x, (float) min.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//                buffer.vertex((float) pos.x, (float) max.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//            }
//            case Z -> {
//                buffer.vertex((float) pos.x, (float) pos.y, (float) min.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//                buffer.vertex((float) pos.x, (float) pos.y, (float) max.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//            }
//            case X -> {
//                buffer.vertex((float) min.x, (float) pos.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//                buffer.vertex((float) max.x, (float) pos.y, (float) pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//            }
//        }
//    }
//
//    private void drawVerticalPlane(VertexConsumer buffer, Vec3 posA, Vec3 posB, Color color) {
//        var min = posA;
//        var max = posB;
//
//        buffer.vertex((float) min.x(), (float) min.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//        buffer.vertex((float) max.x(), (float) min.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//        buffer.vertex((float) max.x(), (float) max.y(), (float) max.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//        buffer.vertex((float) min.x(), (float) max.y(), (float) min.z()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//    }
//
//    private void drawLine(VertexConsumer buffer, Vec3 posA, Vec3 posB, Color color) {
//        buffer.vertex((float) posA.x, (float) posA.y, (float) posA.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//        buffer.vertex((float) posB.x, (float) posB.y, (float) posB.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).normal(1F, 1F, 1F).endVertex();
//    }
//
//    private void renderMirror(MultiBufferSource.BufferSource multiBufferSource, Vec3 pos, Integer radius, List<Axis> axis, boolean drawPlanes, boolean drawLines) {
//        if (drawPlanes) {
//            for (Axis a : axis) {
//                VertexConsumer buffer = ((MultiBufferSource) multiBufferSource).getBuffer(FabricRenderLayers.planes());
//                drawAxisPlane(buffer, pos, radius, a, COLOR_PLANE);
//                multiBufferSource.endBatch();
//            }
//        }
//        if (drawLines) {
//            for (Axis a : axis) {
//                VertexConsumer buffer = multiBufferSource.getBuffer(FabricRenderLayers.lines());
//                for (Axis a1 : Axis.values()) {
//                    if (a1 != a) {
//                        drawAxisLine(buffer, pos, radius, a1, COLOR_LINE);
//                    }
//                }
//                multiBufferSource.endBatch();
//            }
//        }
//
//    }
//
//    private void renderRadial(MultiBufferSource.BufferSource multiBufferSource, Vec3 pos, Integer radius, Integer slices, boolean drawPlanes, boolean drawLines) {
//        float angle = 2f * (float) BaseMth.PI / slices;
//        var relStartVec = new Vec3(radius, 0, 0);
//        if (slices % 4 == 2) relStartVec = relStartVec.yRot(angle / 2f);
//
//        if (drawPlanes) {
//            for (int i = 0; i < slices; i++) {
//                var relNewVec = relStartVec.yRot(angle * i);
//                var newVec = pos.add(relNewVec);
//
//                var posA = new Vec3(pos.x, pos.y - radius, pos.z);
//                var posB = new Vec3(newVec.x, pos.y + radius, newVec.z);
//
//                VertexConsumer buffer = ((MultiBufferSource) multiBufferSource).getBuffer(FabricRenderLayers.planes());
//                drawVerticalPlane(buffer, posA, posB, COLOR_PLANE);
//                multiBufferSource.endBatch();
//            }
//        }
//        if (drawLines) {
//            for (int i = 0; i < slices; i++) {
//                var relNewVec = relStartVec.yRot(angle * i);
//                var newVec = pos.add(relNewVec);
//
//                var posA = new Vec3(pos.x, pos.y - radius, pos.z);
//                var posB = new Vec3(newVec.x, pos.y + radius, newVec.z);
//                VertexConsumer buffer = multiBufferSource.getBuffer(FabricRenderLayers.lines());
//                drawLine(buffer, new Vec3(posA.x(), pos.y(), posA.z()), new Vec3(posB.x(), pos.y(), posB.z()), COLOR_LINE);
//                multiBufferSource.endBatch();
//            }
//            VertexConsumer buffer = multiBufferSource.getBuffer(FabricRenderLayers.lines());
//            drawLine(buffer, new Vec3(pos.x(), pos.y() - radius, pos.z()), new Vec3(pos.x(), pos.y() + radius, pos.z()), COLOR_LINE);
//            multiBufferSource.endBatch();
//        }
//    }

}

