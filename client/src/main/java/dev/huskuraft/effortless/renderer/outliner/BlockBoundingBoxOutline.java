package dev.huskuraft.effortless.renderer.outliner;

import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.math.BoundingBox3d;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.Renderer;

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
        var projectedView = renderer.camera().position();
        var noCull = bb.contains(projectedView);
        bb = bb.inflate(noCull ? -1 / 128d : 1 / 128d);
        noCull |= params.disableCull;

        var xyz = new Vector3d(bb.minX, bb.minY, bb.minZ);
        var Xyz = new Vector3d(bb.maxX, bb.minY, bb.minZ);
        var xYz = new Vector3d(bb.minX, bb.maxY, bb.minZ);
        var XYz = new Vector3d(bb.maxX, bb.maxY, bb.minZ);
        var xyZ = new Vector3d(bb.minX, bb.minY, bb.maxZ);
        var XyZ = new Vector3d(bb.maxX, bb.minY, bb.maxZ);
        var xYZ = new Vector3d(bb.minX, bb.maxY, bb.maxZ);
        var XYZ = new Vector3d(bb.maxX, bb.maxY, bb.maxZ);

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

        renderFace(renderer, Orientation.NORTH, xYz, XYz, Xyz, xyz, noCull);
        renderFace(renderer, Orientation.SOUTH, XYZ, xYZ, xyZ, XyZ, noCull);
        renderFace(renderer, Orientation.EAST, XYz, XYZ, XyZ, Xyz, noCull);
        renderFace(renderer, Orientation.WEST, xYZ, xYz, xyz, xyZ, noCull);
        renderFace(renderer, Orientation.UP, xYZ, XYZ, XYz, xYz, noCull);
        renderFace(renderer, Orientation.DOWN, xyz, Xyz, XyZ, xyZ, noCull);

    }

    protected void renderFace(Renderer renderer, Orientation orientation, Vector3d p1, Vector3d p2,
                              Vector3d p3, Vector3d p4, boolean noCull) {
        if (!params.faceTexture.isPresent())
            return;

        var faceTexture = params.faceTexture.get();
        var alphaBefore = params.alpha;
        params.alpha =
                orientation == params.getHighlightedFace() && params.highlightedFaceTexture.isPresent() ? 1 : 0.5f;

        var renderType = renderer.renderTypes().outlineTranslucent(faceTexture, !noCull);
        renderer.pushLayer();

        var axis = orientation.getAxis();
        var uDiff = p2.subtract(p1);
        var vDiff = p4.subtract(p1);
        var maxU = (float) MathUtils.abs(axis == Axis.X ? uDiff.getZ() : uDiff.getX());
        var maxV = (float) MathUtils.abs(axis == Axis.Y ? vDiff.getZ() : vDiff.getY());
        renderer.drawQuadUV(renderType, p1, p2, p3, p4, 0, 0, maxU, maxV, getParams().getLightMap(), getParams().getColor().getRGB(), Orientation.UP);

        params.alpha = alphaBefore;

        renderer.popLayer();
    }

    public void setBounds(BoundingBox3d bb) {
        this.bb = bb;
    }

}
