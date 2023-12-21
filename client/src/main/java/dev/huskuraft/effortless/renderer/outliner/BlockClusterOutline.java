package dev.huskuraft.effortless.renderer.outliner;

import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.AxisDirection;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.RenderType;
import dev.huskuraft.effortless.renderer.Renderer;

import java.util.*;

public class BlockClusterOutline extends Outline {

    private static Vector3d xyz = new Vector3d(-.5, -.5, -.5);
    private static Vector3d Xyz = new Vector3d(.5, -.5, -.5);
    private static Vector3d xYz = new Vector3d(-.5, .5, -.5);
    private static Vector3d XYz = new Vector3d(.5, .5, -.5);
    private static Vector3d xyZ = new Vector3d(-.5, -.5, .5);
    private static Vector3d XyZ = new Vector3d(.5, -.5, .5);
    private static Vector3d xYZ = new Vector3d(-.5, .5, .5);
    private static Vector3d XYZ = new Vector3d(.5, .5, .5);

    private final Cluster cluster;

    public BlockClusterOutline(Iterable<BlockPosition> selection) {
        cluster = new Cluster();
        selection.forEach(cluster::include);
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        cluster.visibleEdges.forEach(edge -> {
            var start = edge.blockPosition.toVector3d();
            var direction = Orientation.get(AxisDirection.POSITIVE, edge.axis);
            renderAACuboidLine(renderer, start, edge.blockPosition.relative(direction).toVector3d());
        });

        var faceTexture = params.faceTexture;
        if (!faceTexture.isPresent())
            return;

        var translucentType = renderer.renderTypes().outlineTranslucent(faceTexture.get(), true);

        renderer.pushLayer();

        cluster.visibleFaces.forEach((face, axisDirection) -> {
            var direction = Orientation.get(axisDirection, face.axis);
            var pos = face.blockPosition;
            if (axisDirection == AxisDirection.POSITIVE)
                pos = pos.relative(direction.getOpposite());
//            var buffer = renderer.buffer().getBuffer(translucentType);
            renderBlockFace(renderer, translucentType, pos, direction);
        });
        renderer.popLayer();
    }

    protected void renderBlockFace(Renderer renderer, RenderType renderType, BlockPosition blockPosition, Orientation face) {
        var camera = renderer.camera().position();
        var center = blockPosition.getCenter();
        var offset = face.getNormal().toVector3d();
        offset = offset.mul(1 / 128d);
        center = center.sub(camera).add(offset);

        renderer.pushPose();
        renderer.translate(center.x(), center.y(), center.z());

        switch (face) {
            case DOWN ->
                    renderer.renderQuad(renderType, xyz, Xyz, XyZ, xyZ, params.getLightMap(), params.getColor().getRGB(), face);
            case EAST ->
                    renderer.renderQuad(renderType, XYz, XYZ, XyZ, Xyz, params.getLightMap(), params.getColor().getRGB(), face);
            case NORTH ->
                    renderer.renderQuad(renderType, xYz, XYz, Xyz, xyz, params.getLightMap(), params.getColor().getRGB(), face);
            case SOUTH ->
                    renderer.renderQuad(renderType, XYZ, xYZ, xyZ, XyZ, params.getLightMap(), params.getColor().getRGB(), face);
            case UP ->
                    renderer.renderQuad(renderType, xYZ, XYZ, XYz, xYz, params.getLightMap(), params.getColor().getRGB(), face);
            case WEST ->
                    renderer.renderQuad(renderType, xYZ, xYz, xyz, xyZ, params.getLightMap(), params.getColor().getRGB(), face);
        }

        renderer.popPose();
    }

    private static class Cluster {

        private final Map<MergeEntry, AxisDirection> visibleFaces;
        private final Set<MergeEntry> visibleEdges;

        public Cluster() {
            visibleEdges = new HashSet<>();
            visibleFaces = new HashMap<>();
        }

        public void include(BlockPosition blockPosition) {

            // 6 FACES
            for (var axis : Iterate.axes) {
                var direction = Orientation.get(AxisDirection.POSITIVE, axis);
                for (var offset : Iterate.zeroAndOne) {
                    var entry = new MergeEntry(axis, blockPosition.relative(direction, offset));
                    if (visibleFaces.remove(entry) == null)
                        visibleFaces.put(entry, offset == 0 ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE);
                }
            }

            // 12 EDGES
            for (var axis : Iterate.axes) {
                for (var axis2 : Iterate.axes) {
                    if (axis == axis2)
                        continue;
                    for (var axis3 : Iterate.axes) {
                        if (axis == axis3)
                            continue;
                        if (axis2 == axis3)
                            continue;

                        var direction = Orientation.get(AxisDirection.POSITIVE, axis2);
                        var direction2 = Orientation.get(AxisDirection.POSITIVE, axis3);

                        for (var offset : Iterate.zeroAndOne) {
                            var entryPos = blockPosition.relative(direction, offset);
                            for (var offset2 : Iterate.zeroAndOne) {
                                entryPos = entryPos.relative(direction2, offset2);
                                var entry = new MergeEntry(axis, entryPos);
                                if (!visibleEdges.remove(entry))
                                    visibleEdges.add(entry);
                            }
                        }
                    }

                    break;
                }
            }

        }

    }

    private static class MergeEntry {

        private final Axis axis;
        private final BlockPosition blockPosition;

        public MergeEntry(Axis axis, BlockPosition blockPosition) {
            this.axis = axis;
            this.blockPosition = blockPosition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof MergeEntry other))
                return false;

            return this.axis == other.axis && this.blockPosition.equals(other.blockPosition);
        }

        @Override
        public int hashCode() {
            return this.blockPosition.hashCode() * 31 + axis.ordinal();
        }
    }

    private static class Iterate {

        public static final boolean[] trueAndFalse = {true, false};
        public static final boolean[] falseAndTrue = {false, true};
        public static final int[] zeroAndOne = {0, 1};
        public static final int[] positiveAndNegative = {1, -1};
        public static final Orientation[] orientations = Orientation.values();
        public static final Orientation[] horizontalOrientation = getHorizontals();
        public static final Axis[] axes = Axis.values();
        public static final EnumSet<Axis> axisSet = EnumSet.allOf(Axis.class);

        private static Orientation[] getHorizontals() {
            Orientation[] orientations = new Orientation[4];
            for (int i = 0; i < 4; i++)
                orientations[i] = Orientation.from2DDataValue(i);
            return orientations;
        }

        public static Orientation[] directionsInAxis(Axis axis) {
            return switch (axis) {
                case X -> new Orientation[]{Orientation.EAST, Orientation.WEST};
                case Y -> new Orientation[]{Orientation.UP, Orientation.DOWN};
                default -> new Orientation[]{Orientation.SOUTH, Orientation.NORTH};
            };
        }

        public static List<BlockPosition> hereAndBelow(BlockPosition blockPosition) {
            return Arrays.asList(blockPosition, blockPosition.below());
        }

        public static List<BlockPosition> hereBelowAndAbove(BlockPosition blockPosition) {
            return Arrays.asList(blockPosition, blockPosition.below(), blockPosition.above());
        }
    }


}
