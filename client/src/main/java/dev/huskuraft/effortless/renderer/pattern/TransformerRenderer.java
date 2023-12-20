package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.LightTexture;
import dev.huskuraft.effortless.renderer.Renderer;

import java.awt.*;

import static dev.huskuraft.effortless.renderer.RenderUtils.calculateAxisAlignedPlane;
import static dev.huskuraft.effortless.renderer.RenderUtils.rotate;

public abstract class TransformerRenderer {

    private static final Color COLOR_PLANE = new Color(0, 0, 0, 72);
    private static final Color COLOR_LINE = new Color(0, 0, 0, 200);
    private static final Vector3d EPSILON = new Vector3d(0.001, 0.001, 0.001);

    public abstract void render(Renderer renderer, float deltaTick);

    protected void renderPlaneByAxis(Renderer renderer, Vector3d pos, Integer range, Axis axis, Color color) {
        var renderStyle = renderer.renderTypes().planes();
        var cam = renderer.camera().position();
        var min = new Vector3d(-range, -range, -range);
        var max = new Vector3d(range, range, range);

        renderer.pushPose();
        renderer.translate(-cam.getX() + pos.getX(), -cam.getY() + pos.getY(), -cam.getZ() + pos.getZ());
        var cen = Vector3d.ZERO;

        var v1 = Vector3d.ZERO;
        var v2 = Vector3d.ZERO;
        var v3 = Vector3d.ZERO;
        var v4 = Vector3d.ZERO;

        switch (axis) {
            case Y -> {
                v1 = new Vector3d((float) max.getX(), (float) cen.getY(), (float) max.getZ());
                v2 = new Vector3d((float) min.getX(), (float) cen.getY(), (float) max.getZ());
                v3 = new Vector3d((float) min.getX(), (float) cen.getY(), (float) min.getZ());
                v4 = new Vector3d((float) max.getX(), (float) cen.getY(), (float) min.getZ());
            }
            case Z -> {
                v1 = new Vector3d((float) max.getX(), (float) min.getY(), (float) cen.getZ());
                v2 = new Vector3d((float) min.getX(), (float) min.getY(), (float) cen.getZ());
                v3 = new Vector3d((float) min.getX(), (float) max.getY(), (float) cen.getZ());
                v4 = new Vector3d((float) max.getX(), (float) max.getY(), (float) cen.getZ());
            }
            case X -> {
                v1 = new Vector3d((float) cen.getX(), (float) min.getY(), (float) min.getZ());
                v2 = new Vector3d((float) cen.getX(), (float) min.getY(), (float) max.getZ());
                v3 = new Vector3d((float) cen.getX(), (float) max.getY(), (float) max.getZ());
                v4 = new Vector3d((float) cen.getX(), (float) max.getY(), (float) min.getZ());
            }
        }
        renderer.drawQuad(renderStyle, v1, v2, v3, v4, 0, color.getRGB(), null);
        renderer.popPose();
    }

    protected void renderLineByAxis(Renderer renderer, Vector3d pos, Integer range, Axis axis, Color color) {

        var min = pos.subtract(range, range, range);
        var max = pos.add(range, range, range);

        var v1 = Vector3d.ZERO;
        var v2 = Vector3d.ZERO;
        switch (axis) {
            case Y -> {
                v1 = new Vector3d((float) pos.getX(), (float) min.getY(), (float) pos.getZ());
                v2 = new Vector3d((float) pos.getX(), (float) max.getY(), (float) pos.getZ());
            }
            case Z -> {
                v1 = new Vector3d((float) pos.getX(), (float) pos.getY(), (float) min.getZ());
                v2 = new Vector3d((float) pos.getX(), (float) pos.getY(), (float) max.getZ());
            }
            case X -> {
                v1 = new Vector3d((float) min.getX(), (float) pos.getY(), (float) pos.getZ());
                v2 = new Vector3d((float) max.getX(), (float) pos.getY(), (float) pos.getZ());
            }
        }
        renderAACuboidLine(renderer, v1, v2, 1 / 32f, 0xFFFFFF, true);
    }

    protected void renderAACuboidLine(Renderer renderer, Vector3d start, Vector3d end, float width, int color, boolean disableNormals) {
        var camera = renderer.camera().position();
        start = start.subtract(camera);
        end = end.subtract(camera);
        if (width == 0) {
            return;
        }

        var renderStyle = renderer.renderTypes().outlineSolid();

        var diff = end.subtract(start);
        if (diff.getX() + diff.getY() + diff.getZ() < 0) {
            var temp = start;
            start = end;
            end = temp;
            diff = diff.scale(-1);
        }

        var extension = diff.normalize().scale(width / 2);
        var plane = calculateAxisAlignedPlane(diff);
        var face = Orientation.getNearest(diff.getX(), diff.getY(), diff.getZ());
        var axis = face.getAxis();

        start = start.subtract(extension);
        end = end.add(extension);
        plane = plane.scale(width / 2);

        var a1 = plane.add(start);
        var b1 = plane.add(end);
        plane = rotate(plane, -90, axis);
        var a2 = plane.add(start);
        var b2 = plane.add(end);
        plane = rotate(plane, -90, axis);
        var a3 = plane.add(start);
        var b3 = plane.add(end);
        plane = rotate(plane, -90, axis);
        var a4 = plane.add(start);
        var b4 = plane.add(end);

        if (disableNormals) {
            face = Orientation.UP;
            renderer.drawQuad(renderStyle, b4, b3, b2, b1, LightTexture.FULL_BLOCK, color, face);
            renderer.drawQuad(renderStyle, a1, a2, a3, a4, LightTexture.FULL_BLOCK, color, face);
            renderer.drawQuad(renderStyle, a1, b1, b2, a2, LightTexture.FULL_BLOCK, color, face);
            renderer.drawQuad(renderStyle, a2, b2, b3, a3, LightTexture.FULL_BLOCK, color, face);
            renderer.drawQuad(renderStyle, a3, b3, b4, a4, LightTexture.FULL_BLOCK, color, face);
            renderer.drawQuad(renderStyle, a4, b4, b1, a1, LightTexture.FULL_BLOCK, color, face);
            return;
        }

        renderer.drawQuad(renderStyle, b4, b3, b2, b1, LightTexture.FULL_BLOCK, color, face);
        renderer.drawQuad(renderStyle, a1, a2, a3, a4, LightTexture.FULL_BLOCK, color, face.getOpposite());
        var vec = a1.subtract(a4);
        face = Orientation.getNearest(vec.getX(), vec.getY(), vec.getZ());
        renderer.drawQuad(renderStyle, a1, b1, b2, a2, LightTexture.FULL_BLOCK, color, face);
        vec = rotate(vec, -90, axis);
        face = Orientation.getNearest(vec.getX(), vec.getY(), vec.getZ());
        renderer.drawQuad(renderStyle, a2, b2, b3, a3, LightTexture.FULL_BLOCK, color, face);
        vec = rotate(vec, -90, axis);
        face = Orientation.getNearest(vec.getX(), vec.getY(), vec.getZ());
        renderer.drawQuad(renderStyle, a3, b3, b4, a4, LightTexture.FULL_BLOCK, color, face);
        vec = rotate(vec, -90, axis);
        face = Orientation.getNearest(vec.getX(), vec.getY(), vec.getZ());
        renderer.drawQuad(renderStyle, a4, b4, b1, a1, LightTexture.FULL_BLOCK, color, face);
    }

//    public void renderPlaneByAxis(Vector3d v1, Vector3d v2, Axis axis, Color color) {
//
//        pushPose();
//        translate(-getCameraPosition().getX(), -getCameraPosition().getY(), -getCameraPosition().getZ());
//
//        var buffer = proxy.bufferSource().getBuffer(MinecraftClientAdapter.adapt(renderTypes().planes()));
//        var matrix = proxy.pose().last().pose();
//
//        var min = v1;
//        var max = v2;
//
//        switch (axis) {
//            case Y -> {
//                buffer.vertex(matrix, (float) max.getX(), (float) max.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) max.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) min.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) max.getX(), (float) min.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//            case Z -> {
//                buffer.vertex(matrix, (float) max.getX(), (float) max.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) min.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) min.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) max.getX(), (float) max.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//            case X -> {
//                buffer.vertex(matrix, (float) max.getX(), (float) max.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) max.getX(), (float) min.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) min.getY(), (float) min.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//                buffer.vertex(matrix, (float) min.getX(), (float) max.getY(), (float) max.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
//            }
//        }
//
//        popPose();
//    }


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

