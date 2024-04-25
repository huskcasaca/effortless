package dev.huskuraft.effortless.building.structure.builder;

import java.util.Comparator;
import java.util.stream.Stream;

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

    public static int axisSign(int value) {
        return MathUtils.sign(value) == 0 ? 1 : (int) MathUtils.sign(value);
    }


    protected BlockInteraction traceInteraction(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> traceFirstInteraction(player, context);
            case 1 -> traceSecondInteraction(player, context);
            case 2 -> traceThirdInteraction(player, context);
            default -> null;
        };
    }

    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return null;
    }

    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return null;
    }

    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        return null;
    }


    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Stream.of(context.firstBlockPosition());
    }

    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return Stream.of();
    }

    protected Stream<BlockPosition> collectThirdBlocks(Context context) {
        return Stream.of();
    }

    @Override
    public BlockInteraction trace(Player player, Context context) {
        if (context.interactionsSize() >= totalClicks(context)) {
            return null;
        }
        return traceInteraction(player, context, context.interactionsSize());
    }

    @Override
    public Stream<BlockPosition> collect(Context context) {
        if (context.interactionsSize() > totalClicks(context)) {
            return null;
        }
        return switch (context.interactionsSize()) {
            case 1 -> collectFirstBlocks(context);
            case 2 -> collectSecondBlocks(context);
            case 3 -> collectThirdBlocks(context);
            default -> Stream.empty();
        };
    }

    public BuildFeature[] getSupportedFeatures() {
        return new BuildFeature[] {};
    }

    public static BlockInteraction transformUniformLengthInteraction(BlockInteraction start, BlockInteraction end, boolean limit) {
        if (start == null || end == null) {
            return null;
        }
        if (!limit) {
            return end;
        }
        var firstBlockPos = start.getBlockPosition();
        var secondBlockPos = end.getBlockPosition();
        var diff = secondBlockPos.sub(firstBlockPos);

        if (diff.x() == 0 && diff.y() == 0 && diff.z() == 0) {
            return end;
        }
        if ((diff.x() == 0 && diff.y() == 0) || (diff.x() == 0 && diff.z() == 0) || (diff.y() == 0 && diff.z() == 0)) {
            return end;
        }
        if (diff.x() == 0) {
            return end.withPosition(firstBlockPos.add(diff.withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z())))));
        }
        if (diff.z() == 0) {
            return end.withPosition(firstBlockPos.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y()))).withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y())))));
        }
        if (diff.y() == 0) {
            return end.withPosition(firstBlockPos.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z())))));
        }
        return end;
    }


    private static BlockInteraction traceLineByAxis(Player player, Context context, Axis axis) {
        var center = context.secondBlockPosition().getCenter();
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
        var center = context.secondBlockPosition().getCenter();
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

    public abstract static class AxisCriteria {
        protected final Player player;
        protected final Vector3d center;
        protected final Vector3d eye;
        protected final Vector3d look;
        protected final int reach;
        protected final boolean skipRaytrace;
        protected final Axis axis;

        public AxisCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            this.axis = axis;
            this.player = player;
            this.look = getEntityLookAngleGap(player);
            this.eye = player.getEyePosition();
            this.center = center;
            this.reach = reach;
            this.skipRaytrace = skipRaytrace;
        }

        public Axis getAxis() {
            return axis;
        }

        protected static Vector3d getBound(Vector3d start, Vector3d eye, Vector3d look) {
            return new Vector3d(
                    MathUtils.round(getAxisBound(start.x(), eye.x(), look.x())),
                    MathUtils.round(getAxisBound(start.y(), eye.y(), look.y())),
                    MathUtils.round(getAxisBound(start.z(), eye.z(), look.z()))
            );
        }

        protected static double getAxisBound(double start, double eye, double look) {
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
            var bound = getBound(start, eye, look);
            // then y and z are
            double y = (bound.x() - eye.x()) / look.x() * look.y() + eye.y();
            double z = (bound.x() - eye.x()) / look.x() * look.z() + eye.z();

            return new Vector3d(bound.x(), y, z);
        }

        protected static Vector3d findYBound(Vector3d start, Vector3d eye, Vector3d look) {
            var bound = getBound(start, eye, look);
            // then x and z are
            double x = (bound.y() - eye.y()) / look.y() * look.x() + eye.x();
            double z = (bound.y() - eye.y()) / look.y() * look.z() + eye.z();

            return new Vector3d(x, bound.y(), z);
        }

        protected static Vector3d findZBound(Vector3d start, Vector3d eye, Vector3d look) {
            var bound = getBound(start, eye, look);
            // then x and y are
            double x = (bound.z() - eye.z()) / look.z() * look.x() + eye.x();
            double y = (bound.z() - eye.z()) / look.z() * look.y() + eye.y();

            return new Vector3d(x, y, bound.z());
        }

        protected static boolean isCriteriaValid(Vector3d start, Vector3d look, int reach, Player player, boolean skipRaytrace, Vector3d lineBound, Vector3d planeBound, double distToPlayerSq) {
//            boolean intersects = false;
//            if (!skipRaytrace) {
//                // collision within a 1 block radius to selected is fine
//                var rayTraceContext = new ClipContext(start, lineBound, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
//                var rayTraceResult = entity.getLevel().clip(rayTraceContext);
//                intersects = rayTraceResult != null && rayTraceResult.getType() == HitResult.Type.BLOCK && planeBound.subtract(rayTraceResult.getLocation()).lengthSqr() > 4;
//            }
            return planeBound.sub(start).dot(look) > 0 && distToPlayerSq > 2 && distToPlayerSq < reach * reach; // !intersects;
        }

        public Vector3d startVec() {
            return getBound(center, eye, look);
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
            return isCriteriaValid(eye, look, reach, player, skipRaytrace, lineVec(), planeVec(), distanceToEyeSqr());
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
            var look = getEntityLookAngleGap(player);
            var vec3 = player.getEyePosition().add(look.mul(0.001));
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

}
