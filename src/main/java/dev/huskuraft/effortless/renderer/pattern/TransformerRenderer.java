package dev.huskuraft.effortless.renderer.pattern;

import java.awt.*;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.platform.ClientEntrance;
import dev.huskuraft.universal.api.renderer.LightTexture;
import dev.huskuraft.universal.api.renderer.RenderUtils;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderLayers;

public abstract class TransformerRenderer {

    private static final Color COLOR_PLANE = new Color(0, 0, 0, 72);
    private static final Color COLOR_LINE = new Color(0, 0, 0, 200);
    private static final Vector3d EPSILON = new Vector3d(0.001, 0.001, 0.001);

    private final ClientEntrance entrance;

    public EffortlessClient getEntrance() {
        return (EffortlessClient) entrance;
    }

    protected TransformerRenderer(ClientEntrance entrance) {
        this.entrance = entrance;
    }

    public abstract void render(Renderer renderer, float deltaTick);

    protected void renderRadialFloor(Renderer renderer, Vector3d center, Vector3d pos1, Vector3d pos2, int color) {
        var cam = renderer.getCamera().position();
        pos1 = pos1.sub(center);
        pos2 = pos2.sub(center);
        renderer.pushPose();
        renderer.translate(center.sub(cam));
        renderer.renderQuad(BlockRenderLayers.planes(), pos1, pos2, Vector3d.ZERO, Vector3d.ZERO, 0, color, null);
        renderer.popPose();
    }


    protected void renderRadialPlane(Renderer renderer, Vector3d pos1, Vector3d pos2, int length, Axis axis, int color) {
        var cam = renderer.getCamera().position();
        var min = switch (axis) {
            case X -> Vector3d.ZERO.withX(-length);
            case Y -> Vector3d.ZERO.withY(-length);
            case Z -> Vector3d.ZERO.withZ(-length);

        };
        var max = switch (axis) {
            case X -> pos2.sub(pos1).withX(+length);
            case Y -> pos2.sub(pos1).withY(+length);
            case Z -> pos2.sub(pos1).withZ(+length);
        };

        renderer.pushPose();
        renderer.translate(pos1.sub(cam));
        var cen = Vector3d.ZERO;

        var v1 = Vector3d.ZERO;
        var v2 = Vector3d.ZERO;
        var v3 = Vector3d.ZERO;
        var v4 = Vector3d.ZERO;

        switch (axis) {
            case X -> {
                v1 = new Vector3d(max.x(), max.y(), max.z());
                v2 = new Vector3d(min.x(), max.y(), max.z());
                v3 = new Vector3d(min.x(), min.y(), min.z());
                v4 = new Vector3d(max.x(), min.y(), min.z());
            }
            case Y -> {
                v1 = new Vector3d(max.x(), max.y(), max.z());
                v2 = new Vector3d(max.x(), min.y(), max.z());
                v3 = new Vector3d(min.x(), min.y(), min.z());
                v4 = new Vector3d(min.x(), max.y(), min.z());
            }
            case Z -> {
                v1 = new Vector3d(max.x(), max.y(), max.z());
                v2 = new Vector3d(max.x(), max.y(), min.z());
                v3 = new Vector3d(min.x(), min.y(), min.z());
                v4 = new Vector3d(min.x(), min.y(), max.z());
            }
        }
        renderer.renderQuad(BlockRenderLayers.planes(), v1, v2, v3, v4, 0, color, null);
        renderer.popPose();
    }

    protected void renderPlaneByAxis(Renderer renderer, Vector3d pos, Integer range, Axis axis, Color color) {
        var cam = renderer.getCamera().position();
        var min = new Vector3d(-range, -range, -range);
        var max = new Vector3d(range, range, range);

        renderer.pushPose();
        renderer.translate(pos.sub(cam));
        var cen = Vector3d.ZERO;

        var v1 = Vector3d.ZERO;
        var v2 = Vector3d.ZERO;
        var v3 = Vector3d.ZERO;
        var v4 = Vector3d.ZERO;

        switch (axis) {
            case Y -> {
                v1 = new Vector3d(max.x(), cen.y(), max.z());
                v2 = new Vector3d(min.x(), cen.y(), max.z());
                v3 = new Vector3d(min.x(), cen.y(), min.z());
                v4 = new Vector3d(max.x(), cen.y(), min.z());
            }
            case Z -> {
                v1 = new Vector3d(max.x(), min.y(), cen.z());
                v2 = new Vector3d(min.x(), min.y(), cen.z());
                v3 = new Vector3d(min.x(), max.y(), cen.z());
                v4 = new Vector3d(max.x(), max.y(), cen.z());
            }
            case X -> {
                v1 = new Vector3d(cen.x(), min.y(), min.z());
                v2 = new Vector3d(cen.x(), min.y(), max.z());
                v3 = new Vector3d(cen.x(), max.y(), max.z());
                v4 = new Vector3d(cen.x(), max.y(), min.z());
            }
        }
        renderer.renderQuad(BlockRenderLayers.planes(), v1, v2, v3, v4, 0, color.getRGB(), null);
        renderer.popPose();
    }

    protected void renderFace(Renderer renderer, Direction direction, Vector3d p1, Vector3d p2, Vector3d p3, Vector3d p4, int color) {
        renderer.renderQuad(BlockRenderLayers.planes(), p1, p2, p3, p4, 0, color, null);
    }

    protected void renderBoundingBox(Renderer renderer, BoundingBox3d boundingBox3d, int color) {
        var cam = renderer.getCamera().position();
        var center = boundingBox3d.getCenter();
        var box = boundingBox3d.move(center.mul(-1));

        var xyz = new Vector3d(box.minX(), box.minY(), box.minZ());
        var Xyz = new Vector3d(box.maxX(), box.minY(), box.minZ());
        var xYz = new Vector3d(box.minX(), box.maxY(), box.minZ());
        var XYz = new Vector3d(box.maxX(), box.maxY(), box.minZ());
        var xyZ = new Vector3d(box.minX(), box.minY(), box.maxZ());
        var XyZ = new Vector3d(box.maxX(), box.minY(), box.maxZ());
        var xYZ = new Vector3d(box.minX(), box.maxY(), box.maxZ());
        var XYZ = new Vector3d(box.maxX(), box.maxY(), box.maxZ());


        renderer.pushPose();
        renderer.translate(center.sub(cam));
        renderFace(renderer, Direction.NORTH, xYz, XYz, Xyz, xyz, color);
        renderFace(renderer, Direction.SOUTH, XYZ, xYZ, xyZ, XyZ, color);
        renderFace(renderer, Direction.EAST, XYz, XYZ, XyZ, Xyz, color);
        renderFace(renderer, Direction.WEST, xYZ, xYz, xyz, xyZ, color);
        renderFace(renderer, Direction.UP, xYZ, XYZ, XYz, xYz, color);
        renderFace(renderer, Direction.DOWN, xyz, Xyz, XyZ, xyZ, color);

        renderer.popPose();

    }

    protected void renderLine(Renderer renderer, Vector3d pos1, Vector3d pos2) {
        renderAACuboidLine(renderer, pos1, pos2, 1 / 32f, 0xFFFFFF, true);
    }

    protected void renderLineByAxis(Renderer renderer, Vector3d pos, Integer range, Axis axis) {

        var min = pos.sub(range, range, range);
        var max = pos.add(range, range, range);

        var v1 = Vector3d.ZERO;
        var v2 = Vector3d.ZERO;
        switch (axis) {
            case Y -> {
                v1 = new Vector3d(pos.x(), min.y(), pos.z());
                v2 = new Vector3d(pos.x(), max.y(), pos.z());
            }
            case Z -> {
                v1 = new Vector3d(pos.x(), pos.y(), min.z());
                v2 = new Vector3d(pos.x(), pos.y(), max.z());
            }
            case X -> {
                v1 = new Vector3d(min.x(), pos.y(), pos.z());
                v2 = new Vector3d(max.x(), pos.y(), pos.z());
            }
        }
        renderAACuboidLine(renderer, v1, v2, 1 / 32f, 0xFFFFFF, true);
    }

    protected void renderAACuboidLine(Renderer renderer, Vector3d start, Vector3d end, float width, int color, boolean disableNormals) {
        var camera = renderer.getCamera().position();
        start = start.sub(camera);
        end = end.sub(camera);
        if (width == 0) {
            return;
        }

        var renderLayer = OutlineRenderLayers.outlineSolid();

        var diff = end.sub(start);
        if (diff.x() + diff.y() + diff.z() < 0) {
            var temp = start;
            start = end;
            end = temp;
            diff = diff.mul(-1);
        }

        var extension = diff.normalize().mul(width / 2);
        var plane = RenderUtils.calculateAxisAlignedPlane(diff);
        var face = Direction.getNearest(diff.x(), diff.y(), diff.z());
        var axis = face.getAxis();

        start = start.sub(extension);
        end = end.add(extension);
        plane = plane.mul(width / 2);

        var a1 = plane.add(start);
        var b1 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a2 = plane.add(start);
        var b2 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a3 = plane.add(start);
        var b3 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a4 = plane.add(start);
        var b4 = plane.add(end);

        if (disableNormals) {
            face = Direction.UP;
            renderer.renderQuad(renderLayer, b4, b3, b2, b1, LightTexture.FULL_BLOCK, color, face);
            renderer.renderQuad(renderLayer, a1, a2, a3, a4, LightTexture.FULL_BLOCK, color, face);
            renderer.renderQuad(renderLayer, a1, b1, b2, a2, LightTexture.FULL_BLOCK, color, face);
            renderer.renderQuad(renderLayer, a2, b2, b3, a3, LightTexture.FULL_BLOCK, color, face);
            renderer.renderQuad(renderLayer, a3, b3, b4, a4, LightTexture.FULL_BLOCK, color, face);
            renderer.renderQuad(renderLayer, a4, b4, b1, a1, LightTexture.FULL_BLOCK, color, face);
            return;
        }

        renderer.renderQuad(renderLayer, b4, b3, b2, b1, LightTexture.FULL_BLOCK, color, face);
        renderer.renderQuad(renderLayer, a1, a2, a3, a4, LightTexture.FULL_BLOCK, color, face.getOpposite());
        var vec = a1.sub(a4);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a1, b1, b2, a2, LightTexture.FULL_BLOCK, color, face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a2, b2, b3, a3, LightTexture.FULL_BLOCK, color, face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a3, b3, b4, a4, LightTexture.FULL_BLOCK, color, face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a4, b4, b1, a1, LightTexture.FULL_BLOCK, color, face);
    }

}

