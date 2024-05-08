package dev.huskuraft.effortless.building.structure.builder;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;

public abstract class AbstractBlockStructure implements BlockStructure {

    private static final double LOOK_VEC_TOLERANCE = 0.01;

    enum DirectionTraceShape {
        SINGLE,
        LINE_DOWN(Orientation.DOWN),
        LINE_UP(Orientation.UP),
        LINE_NORTH(Orientation.NORTH),
        LINE_SOUTH(Orientation.SOUTH),
        LINE_WEST(Orientation.WEST),
        LINE_EAST(Orientation.EAST),
        PLANE_X(Axis.X),
        PLANE_Y(Axis.Y),
        PLANE_Z(Axis.Z),
        CUBE;

        private final Axis axis;
        private final Orientation orientation;

        DirectionTraceShape() {
            this.axis = null;
            this.orientation = null;
        }

        DirectionTraceShape(Axis axis) {
            this.axis = axis;
            this.orientation = null;
        }

        DirectionTraceShape(Orientation orientation) {
            this.axis = orientation.getAxis();
            this.orientation = orientation;
        }
    }

    enum TraceShape {
        SINGLE,
        LINE_X(Axis.X),
        LINE_Y(Axis.Y),
        LINE_Z(Axis.Z),
        PLANE_X(Axis.X),
        PLANE_Y(Axis.Y),
        PLANE_Z(Axis.Z),
        CUBE;

        private final Axis axis;

        TraceShape() {
            this.axis = null;
        }

        TraceShape(Axis axis) {
            this.axis = axis;
        }

        public static TraceShape fromPosition(BlockPosition pos0, BlockPosition pos1) {
            if (pos0.x() == pos1.x() && pos0.y() == pos1.y() && pos0.z() == pos1.z()) {
                return TraceShape.SINGLE;
            }
            if (pos0.x() == pos1.x() && pos0.z() == pos1.z()) {
                return TraceShape.LINE_Y;
            }
            if (pos0.y() == pos1.y() && pos0.z() == pos1.z()) {
                return TraceShape.LINE_X;
            }
            if (pos0.x() == pos1.x() && pos0.y() == pos1.y()) {
                return TraceShape.LINE_Z;
            }
            if (pos0.x() == pos1.x()) {
                return PLANE_X;
            }
            if (pos0.y() == pos1.y()) {
                return PLANE_Y;
            }
            if (pos0.z() == pos1.z()) {
                return PLANE_Z;
            }
            return TraceShape.CUBE;
        }
    }

    @Override
    public int volume(Context context) {
        return context.getInteractionBox().volume();
    }

    protected static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    protected static double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    protected static Set<BlockPosition> getBallooned(Set<BlockPosition> set, double radius) {
        Set<BlockPosition> result = Sets.newLinkedHashSet();
        var ceilRadius = (int) Math.ceil(radius);
        var radiusSquare = Math.pow(radius, 2);

        for (var v : set) {
            int tipx = v.x();
            int tipy = v.y();
            int tipz = v.z();

            for (var loopx = tipx - ceilRadius; loopx <= tipx + ceilRadius; loopx++) {
                for (var loopy = tipy - ceilRadius; loopy <= tipy + ceilRadius; loopy++) {
                    for (var loopz = tipz - ceilRadius; loopz <= tipz + ceilRadius; loopz++) {
                        if (lengthSq(loopx - tipx, loopy - tipy, loopz - tipz) <= radiusSquare) {
                            result.add(BlockPosition.at(loopx, loopy, loopz));
                        }
                    }
                }
            }
        }
        return result;
    }


    protected static Set<BlockPosition> getHollowed(Set<BlockPosition> set) {
        Set<BlockPosition> result = new LinkedHashSet<>();
        for (var v : set) {
            var x = v.x();
            var y = v.y();
            var z = v.z();
            if (!(set.contains(BlockPosition.at(x + 1, y, z))
                    && set.contains(BlockPosition.at(x - 1, y, z))
                    && set.contains(BlockPosition.at(x, y + 1, z))
                    && set.contains(BlockPosition.at(x, y - 1, z))
                    && set.contains(BlockPosition.at(x, y, z + 1))
                    && set.contains(BlockPosition.at(x, y, z - 1)))) {
                result.add(v);
            }
        }
        return result;
    }

    public static int axisSign(int value) {
        return MathUtils.sign(value) == 0 ? 1 : (int) MathUtils.sign(value);
    }

    public static BlockInteraction transformUniformLengthInteraction(BlockInteraction start, BlockInteraction end, boolean limit) {
        if (start == null || end == null) {
            return null;
        }
        if (!limit) {
            return end;
        }
        var p0 = start.getBlockPosition();
        var p1 = end.getBlockPosition();
        var diff = p1.sub(p0);

        if (diff.x() == 0 && diff.y() == 0 && diff.z() == 0) {
            return end;
        }
        if ((diff.x() == 0 && diff.y() == 0) || (diff.x() == 0 && diff.z() == 0) || (diff.y() == 0 && diff.z() == 0)) {
            return end;
        }
        if (diff.x() == 0) {
            return end.withBlockPosition(p0.add(diff.withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z())))));
        }
        if (diff.z() == 0) {
            return end.withBlockPosition(p0.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y()))).withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y())))));
        }
        if (diff.y() == 0) {
            return end.withBlockPosition(p0.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z())))));
        }
        return end;
    }

    private static BlockInteraction traceLineByAxis(Player player, Context context, Axis axis) {
        var center = context.getPosition(1).getCenter();
        var reach = context.maxNextReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new AxisLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(criteria -> criteria.axis != axis)
                .filter(criteria -> criteria.isInRange())
                .min(Comparator.comparing(axisLineCriteria -> axisLineCriteria.distanceToLineSqr()))
                .map(criteria -> criteria.traceLine(axis))
                .orElse(null);
    }

    public static BlockInteraction traceLineY(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.Y);
    }

    public static BlockInteraction traceLineX(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.X);
    }

    public static BlockInteraction traceLineZ(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.Z);
    }

    protected static BlockInteraction tracePlaneByAxis(Player player, Context context, Axis axis) {
        var center = context.getPosition(1).getCenter();
        var reach = context.maxNextReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new AxisLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(criteria -> criteria.axis == axis)
                .filter(criteria -> criteria.isInRange())
                .min(Comparator.comparing(axisLineCriteria -> axisLineCriteria.distanceToLineSqr()))
                .map(criteria -> criteria.tracePlane())
                .orElse(null);
    }

    public static BlockInteraction tracePlaneY(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.Y);
    }

    public static BlockInteraction tracePlaneX(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.X);
    }

    public static BlockInteraction tracePlaneZ(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.Z);
    }

    // TODO: 13/10/23 entity
    protected static Vector3d getEntityLookAngleGap(Player player) {
        var look = player.getEyeDirection();
        var x = look.x();
        var y = look.y();
        var z = look.z();

        if (MathUtils.abs(x) < LOOK_VEC_TOLERANCE) x = LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(x - 1.0) < LOOK_VEC_TOLERANCE) x = 1 - LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(x + 1.0) < LOOK_VEC_TOLERANCE) x = LOOK_VEC_TOLERANCE - 1;

        if (MathUtils.abs(y) < LOOK_VEC_TOLERANCE) y = LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(y - 1.0) < LOOK_VEC_TOLERANCE) y = 1 - LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(y + 1.0) < LOOK_VEC_TOLERANCE) y = LOOK_VEC_TOLERANCE - 1;

        if (MathUtils.abs(z) < LOOK_VEC_TOLERANCE) z = LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(z - 1.0) < LOOK_VEC_TOLERANCE) z = 1 - LOOK_VEC_TOLERANCE;
        if (MathUtils.abs(z + 1.0) < LOOK_VEC_TOLERANCE) z = LOOK_VEC_TOLERANCE - 1;

        return new Vector3d(x, y, z).normalize();
    }

    protected abstract BlockInteraction trace(Player player, Context context, int index);

    protected abstract Stream<BlockPosition> collect(Context context, int index);

    @Override
    public final BlockInteraction trace(Player player, Context context) {
        var interactionsSize = context.interactionsSize();
        if (interactionsSize >= traceSize(context)) {
            return null;
        }
        return trace(player, context, interactionsSize);
    }


    @Override
    public final Stream<BlockPosition> collect(Context context) {
        var interactionsSize = context.interactionsSize();
        if (interactionsSize > traceSize(context)) {
            return null;
        }
        return collect(context, interactionsSize);
    }

    public BuildFeature[] getSupportedFeatures() {
        return new BuildFeature[]{};
    }


    protected abstract static class Criteria {

        protected final Vector3d center;
        protected final Vector3d eye;
        protected final Vector3d look;
        protected final int reach;
        protected final boolean skipRaytrace;

        Criteria(Player player, Vector3d center, int reach, boolean skipRaytrace) {
            this.look = getEntityLookAngleGap(player);
            this.eye = player.getEyePosition();
            this.center = center;
            this.reach = reach;
            this.skipRaytrace = skipRaytrace;
        }

        public abstract boolean isInRange();

    }

    protected abstract static class AxisCriteria extends Criteria {
        protected final Axis axis;

        public AxisCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(player, center, reach, skipRaytrace);
            this.axis = axis;
        }

        protected static Vector3d getStartVec(Vector3d start, Vector3d eye, Vector3d look) {
            return new Vector3d(
                    MathUtils.round(snapToGrid(start.x(), eye.x(), look.x())),
                    MathUtils.round(snapToGrid(start.y(), eye.y(), look.y())),
                    MathUtils.round(snapToGrid(start.z(), eye.z(), look.z()))
            );
        }

        protected static double snapToGrid(double start, double eye, double look) {
            if (eye >= start + 0.5) {
                return start + 0.5;
            }
            if (eye <= start - 0.5) {
                return start - 0.5;
            }
            if (look > 0) {
                return start + 0.5;
            }
            if (look < 0) {
                return start - 0.5;
            }
            return start;
        }

        // find coordinates on a line bound by a plane
        protected static Vector3d findBound(Vector3d start, Vector3d eye, Vector3d look, Axis axis) {
            return switch (axis) {
                case X -> findXBound(start, eye, look);
                case Y -> findYBound(start, eye, look);
                case Z -> findZBound(start, eye, look);
            };
        }

        // find coordinates on a line bound by a plane
        protected static Vector3d findXBound(Vector3d start, Vector3d eye, Vector3d look) {
            var center = getStartVec(start, eye, look);
            // then y and z are
            double y = (center.x() - eye.x()) / look.x() * look.y() + eye.y();
            double z = (center.x() - eye.x()) / look.x() * look.z() + eye.z();

            return new Vector3d(center.x(), y, z);
        }

        protected static Vector3d findYBound(Vector3d start, Vector3d eye, Vector3d look) {
            var center = getStartVec(start, eye, look);
            // then x and z are
            double x = (center.y() - eye.y()) / look.y() * look.x() + eye.x();
            double z = (center.y() - eye.y()) / look.y() * look.z() + eye.z();

            return new Vector3d(x, center.y(), z);
        }

        protected static Vector3d findZBound(Vector3d start, Vector3d eye, Vector3d look) {
            var center = getStartVec(start, eye, look);
            // then x and y are
            double x = (center.z() - eye.z()) / look.z() * look.x() + eye.x();
            double y = (center.z() - eye.z()) / look.z() * look.y() + eye.y();

            return new Vector3d(x, y, center.z());
        }

        protected boolean isCriteriaValid(Vector3d start, Vector3d look, Vector3d lineBound, Vector3d planeBound, double distToPlayerSq, int reach) {
            return planeBound.sub(start).dot(look) > 0 && distToPlayerSq > getDistToPlayerSqThreshold() && distToPlayerSq < reach * reach; // !intersects;
        }

        protected int getDistToPlayerSqThreshold() {
            return 0;
        }

        public Axis getAxis() {
            return axis;
        }

        public Vector3d startVec() {
            return getStartVec(center, eye, look);
        }

        public Vector3d planeVec() {
            return findBound(center, eye, look, axis);
        }

        public Vector3d lineVec() {
            return planeVec();
        }

        public double distanceToEyeSqr() {
            return planeVec().sub(eye).lengthSq();
        }

        public double distanceToLineSqr() {
            return planeVec().sub(lineVec()).lengthSq();
        }

        public boolean isInRange() {
            return isCriteriaValid(eye, look, lineVec(), planeVec(), distanceToEyeSqr(), reach);
        }

        public BlockInteraction tracePlane() {
            var offset = startVec().sub(center);
            var found = BlockPosition.at(planeVec().sub(axis == Axis.X ? offset.x() : 0, axis == Axis.Y ? offset.y() : 0, axis == Axis.Z ? offset.z() : 0));
            return convert(found);
        }

        public BlockInteraction traceLine() {
            return convert(BlockPosition.at(lineVec()));
        }

        protected BlockInteraction convert(BlockPosition blockPosition) {
            var vec3 = eye.add(look.mul(0.001));
            return new BlockInteraction(vec3, Orientation.getNearest(look.x(), look.y(), look.z()).getOpposite(), blockPosition, true);
        }

    }

    public static class AxisLineCriteria extends AxisCriteria {

        public AxisLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        @Override
        public Vector3d lineVec() {
            return lineVec(axis);
        }

        public Vector3d lineVec(Axis axis) {
            var pos = BlockPosition.at(center);
            var bound = BlockPosition.at(planeVec());
            return switch (axis) {
                case X -> new Vector3d(bound.x(), pos.y(), pos.z());
                case Y -> new Vector3d(pos.x(), bound.y(), pos.z());
                case Z -> new Vector3d(pos.x(), pos.y(), bound.z());
            };
        }

        public BlockInteraction traceLine(Axis axis) {
            var found = BlockPosition.at(lineVec(axis));
            return convert(found);
        }

    }



    protected static class NearestAxisLineCriteria extends NearestLineCriteria {

        private final Set<Axis> axes;

        public NearestAxisLineCriteria(Set<Axis> axes, Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
            this.axes = axes;
        }

        @Override
        public Vector3d lineVec() {
            var pos = center;
            var bound = planeVec();
            var size = bound.sub(pos);

            size = Vector3d.at(MathUtils.abs(size.x()), MathUtils.abs(size.y()), MathUtils.abs(size.z()));
            if (axes.isEmpty()) {
                return pos;
            }
            var longest = MathUtils.max(axes.contains(Axis.X) ? size.x() : Double.MIN_VALUE, MathUtils.max(axes.contains(Axis.Y) ? size.y() : Double.MIN_VALUE, axes.contains(Axis.Z) ? size.z() : Double.MIN_VALUE));
            if (longest == size.x() && axes.contains(Axis.X)) {
                return new Vector3d(bound.x(), pos.y(), pos.z());
            }
            if (longest == size.y() && axes.contains(Axis.Y)) {
                return new Vector3d(pos.x(), bound.y(), pos.z());
            }
            if (longest == size.z() && axes.contains(Axis.Z)) {
                return new Vector3d(pos.x(), pos.y(), bound.z());
            }
            return pos;
        }

    }


    protected static class NearestLineCriteria extends AxisCriteria {

        public NearestLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        @Override
        public Vector3d lineVec() {
            var pos = center;
            var bound = planeVec();
            var size = bound.sub(pos);

            size = Vector3d.at(MathUtils.abs(size.x()), MathUtils.abs(size.y()), MathUtils.abs(size.z()));
            var longest = MathUtils.max(size.x(), MathUtils.max(size.y(), size.z()));
            if (longest == size.x()) {
                return new Vector3d(bound.x(), pos.y(), pos.z());
            }
            if (longest == size.y()) {
                return new Vector3d(pos.x(), bound.y(), pos.z());
            }
            if (longest == size.z()) {
                return new Vector3d(pos.x(), pos.y(), bound.z());
            }
            return pos;
        }

        @Override
        public double distanceToLineSqr() {
            return planeVec().sub(lineVec()).lengthSq();
        }

        @Override
        protected int getDistToPlayerSqThreshold() {
            return switch (getAxis()) {
                case X, Z -> 2;
                case Y -> 0;
            };
        }
    }

}
