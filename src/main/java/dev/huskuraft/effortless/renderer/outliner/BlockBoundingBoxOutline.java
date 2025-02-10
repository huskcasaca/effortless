package dev.huskuraft.effortless.renderer.outliner;

import java.awt.*;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.renderer.Renderer;

public class BlockBoundingBoxOutline extends Outline {

    protected BoundingBox3d bb;

    public BlockBoundingBoxOutline(BoundingBox3d bb) {
        this.setBounds(bb);
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        renderBB(renderer, bb);
    }

    public void renderBB(Renderer renderer, BoundingBox3d bb) {
        var projectedView = renderer.getCamera().position();
        var noCull = bb.contains(projectedView);
        bb = bb.inflate(noCull ? -1 / 128d : 1 / 128d);
        noCull |= params.disableCull;

        var xyz = new Vector3d(bb.minX(), bb.minY(), bb.minZ());
        var Xyz = new Vector3d(bb.maxX(), bb.minY(), bb.minZ());
        var xYz = new Vector3d(bb.minX(), bb.maxY(), bb.minZ());
        var XYz = new Vector3d(bb.maxX(), bb.maxY(), bb.minZ());
        var xyZ = new Vector3d(bb.minX(), bb.minY(), bb.maxZ());
        var XyZ = new Vector3d(bb.maxX(), bb.minY(), bb.maxZ());
        var xYZ = new Vector3d(bb.minX(), bb.maxY(), bb.maxZ());
        var XYZ = new Vector3d(bb.maxX(), bb.maxY(), bb.maxZ());

        var start = xyz;
        renderAACuboidLine(renderer, start, Xyz);
        renderAACuboidLine(renderer, start, xYz);
        renderAACuboidLine(renderer, start, xyZ);

        start = XyZ;
        renderAACuboidLine(renderer, start, xyZ);
        renderAACuboidLine(renderer, start, XYZ);
        renderAACuboidLine(renderer, start, Xyz);

        start = XYz;
        renderAACuboidLine(renderer, start, xYz);
        renderAACuboidLine(renderer, start, Xyz);
        renderAACuboidLine(renderer, start, XYZ);

        start = xYZ;
        renderAACuboidLine(renderer, start, XYZ);
        renderAACuboidLine(renderer, start, xyZ);
        renderAACuboidLine(renderer, start, xYz);

        renderFace(renderer, Direction.NORTH, xYz, XYz, Xyz, xyz, noCull);
        renderFace(renderer, Direction.SOUTH, XYZ, xYZ, xyZ, XyZ, noCull);
        renderFace(renderer, Direction.EAST, XYz, XYZ, XyZ, Xyz, noCull);
        renderFace(renderer, Direction.WEST, xYZ, xYz, xyz, xyZ, noCull);
        renderFace(renderer, Direction.UP, xYZ, XYZ, XYz, xYz, noCull);
        renderFace(renderer, Direction.DOWN, xyz, Xyz, XyZ, xyZ, noCull);

    }

    protected void renderFace(Renderer renderer, Direction direction, Vector3d p1, Vector3d p2,
                              Vector3d p3, Vector3d p4, boolean noCull) {
        if (!params.faceTexture.isPresent())
            return;

        var faceTexture = params.faceTexture.get();
        var alphaBefore = params.alpha;
        params.alpha =
                direction == params.getHighlightedFace() && params.highlightedFaceTexture.isPresent() ? 1 : 0.5f;

        var renderLayer = OutlineRenderLayers.outlineTranslucent(faceTexture, !noCull);

        var axis = direction.getAxis();
        var uDiff = p2.sub(p1);
        var vDiff = p4.sub(p1);
        var maxU = (float) MathUtils.abs(axis == Axis.X ? uDiff.z() : uDiff.x());
        var maxV = (float) MathUtils.abs(axis == Axis.Y ? vDiff.z() : vDiff.y());
        renderer.renderQuadUV(renderLayer, p1, p2, p3, p4, 0, 0, maxU, maxV, getParams().getLightMap(), new Color(0, 0, 0, 200).getRGB(), Direction.UP);

        params.alpha = alphaBefore;

    }

    public void setBounds(BoundingBox3d bb) {
        this.bb = bb;
    }

}
