package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;

public abstract class AbstractClickBuilder implements Builder {

    private static final double LOOK_VEC_TOLERANCE = 0.01;

    public static int axisSign(int value) {
        return MathUtils.sign(value) == 0 ? 1 : (int) MathUtils.sign(value);
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

}
