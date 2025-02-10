package dev.huskuraft.effortless.renderer.outliner;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.AxisDirection;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.renderer.RenderLayer;
import dev.huskuraft.universal.api.renderer.Renderer;

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
            var direction = Direction.get(AxisDirection.POSITIVE, edge.axis);
            renderAACuboidLine(renderer, start, edge.blockPosition.relative(direction).toVector3d());
        });

        var faceTexture = params.faceTexture;
        if (!faceTexture.isPresent())
            return;

        var renderLayer = OutlineRenderLayers.outlineTranslucent(faceTexture.get(), true);
        cluster.visibleFaces.forEach((face, axisDirection) -> {
            var direction = Direction.get(axisDirection, face.axis);
            var pos = face.blockPosition;
            if (axisDirection == AxisDirection.POSITIVE)
                pos = pos.relative(direction.getOpposite());
//            var buffer = renderer.buffer().getBuffer(renderLayer);
            renderBlockFace(renderer, renderLayer, pos, direction);
        });
    }

    protected void renderBlockFace(Renderer renderer, RenderLayer renderLayer, BlockPosition blockPosition, Direction face) {
        var camera = renderer.getCamera().position();
        var center = blockPosition.getCenter();
        var offset = face.getNormal().toVector3d();
        offset = offset.mul(1 / 128d);
        center = center.sub(camera).add(offset);

        renderer.pushPose();
        renderer.translate(center);

        switch (face) {
            case DOWN ->
                    renderer.renderQuad(renderLayer, xyz, Xyz, XyZ, xyZ, params.getLightMap(), params.getColor().getRGB(), face);
            case EAST ->
                    renderer.renderQuad(renderLayer, XYz, XYZ, XyZ, Xyz, params.getLightMap(), params.getColor().getRGB(), face);
            case NORTH ->
                    renderer.renderQuad(renderLayer, xYz, XYz, Xyz, xyz, params.getLightMap(), params.getColor().getRGB(), face);
            case SOUTH ->
                    renderer.renderQuad(renderLayer, XYZ, xYZ, xyZ, XyZ, params.getLightMap(), params.getColor().getRGB(), face);
            case UP ->
                    renderer.renderQuad(renderLayer, xYZ, XYZ, XYz, xYz, params.getLightMap(), params.getColor().getRGB(), face);
            case WEST ->
                    renderer.renderQuad(renderLayer, xYZ, xYz, xyz, xyZ, params.getLightMap(), params.getColor().getRGB(), face);
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
                var direction = Direction.get(AxisDirection.POSITIVE, axis);
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

                        var direction = Direction.get(AxisDirection.POSITIVE, axis2);
                        var direction2 = Direction.get(AxisDirection.POSITIVE, axis3);

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
        public static final Direction[] DIRECTIONS = Direction.values();
        public static final Direction[] HORIZONTAL_DIRECTION = getHorizontals();
        public static final Axis[] axes = Axis.values();
        public static final EnumSet<Axis> axisSet = EnumSet.allOf(Axis.class);

        private static Direction[] getHorizontals() {
            Direction[] directions = new Direction[4];
            for (int i = 0; i < 4; i++)
                directions[i] = Direction.from2DDataValue(i);
            return directions;
        }

        public static Direction[] directionsInAxis(Axis axis) {
            return switch (axis) {
                case X -> new Direction[]{Direction.EAST, Direction.WEST};
                case Y -> new Direction[]{Direction.UP, Direction.DOWN};
                default -> new Direction[]{Direction.SOUTH, Direction.NORTH};
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
