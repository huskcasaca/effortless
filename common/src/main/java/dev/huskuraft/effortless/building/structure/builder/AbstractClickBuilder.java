package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;

public abstract class AbstractClickBuilder implements Builder {

    private static final double LOOK_VEC_TOLERANCE = 0.01;

    // TODO: 13/10/23 entity
    protected static Vector3d getEntityLookAngleGap(Player player) {
        var look = player.getEyeDirection();
        var x = look.getX();
        var y = look.getY();
        var z = look.getZ();

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
                    MathUtils.round(getAxisBound(start.getX(), eye.getX(), look.getX())),
                    MathUtils.round(getAxisBound(start.getY(), eye.getY(), look.getY())),
                    MathUtils.round(getAxisBound(start.getZ(), eye.getZ(), look.getZ()))
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
            double y = (bound.getX() - eye.getX()) / look.getX() * look.getY() + eye.getY();
            double z = (bound.getX() - eye.getX()) / look.getX() * look.getZ() + eye.getZ();

            return new Vector3d(bound.getX(), y, z);
        }

        protected static Vector3d findYBound(Vector3d start, Vector3d eye, Vector3d look) {
            var bound = getBound(start, eye, look);
            // then x and z are
            double x = (bound.getY() - eye.getY()) / look.getY() * look.getX() + eye.getX();
            double z = (bound.getY() - eye.getY()) / look.getY() * look.getZ() + eye.getZ();

            return new Vector3d(x, bound.getY(), z);
        }

        protected static Vector3d findZBound(Vector3d start, Vector3d eye, Vector3d look) {
            var bound = getBound(start, eye, look);
            // then x and y are
            double x = (bound.getZ() - eye.getZ()) / look.getZ() * look.getX() + eye.getX();
            double y = (bound.getZ() - eye.getZ()) / look.getZ() * look.getY() + eye.getY();

            return new Vector3d(x, y, bound.getZ());
        }

        protected static boolean isCriteriaValid(Vector3d start, Vector3d look, int reach, Player player, boolean skipRaytrace, Vector3d lineBound, Vector3d planeBound, double distToPlayerSq) {
//            boolean intersects = false;
//            if (!skipRaytrace) {
//                // collision within a 1 block radius to selected is fine
//                var rayTraceContext = new ClipContext(start, lineBound, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
//                var rayTraceResult = entity.getLevel().clip(rayTraceContext);
//                intersects = rayTraceResult != null && rayTraceResult.getType() == HitResult.Type.BLOCK && planeBound.subtract(rayTraceResult.getLocation()).lengthSqr() > 4;
//            }
            return planeBound.subtract(start).dot(look) > 0 && distToPlayerSq > 2 && distToPlayerSq < reach * reach; // !intersects;
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
            return planeVec().subtract(eye).lengthSq();
        }

        public double distanceToLineSqr() {
            return planeVec().subtract(lineVec()).lengthSq();
        }

        public boolean isInRange() {
            return isCriteriaValid(eye, look, reach, player, skipRaytrace, lineVec(), planeVec(), distanceToEyeSqr());
        }

        public BlockInteraction tracePlane() {
            var offset = startVec().subtract(center);
            var found = BlockPosition.at(planeVec().subtract(axis == Axis.X ? offset.getX() : 0, axis == Axis.Y ? offset.getY() : 0, axis == Axis.Z ? offset.getZ() : 0));
            return convert(found);
        }

        public BlockInteraction traceLine() {
            return convert(BlockPosition.at(lineVec()));
        }

        protected BlockInteraction convert(BlockPosition blockPosition) {
            var look = getEntityLookAngleGap(player);
            var vec3 = player.getEyePosition().add(look.scale(0.001));
            return new BlockInteraction(vec3, Orientation.getNearest(look.getX(), look.getY(), look.getZ()).getOpposite(), blockPosition, true);
        }

    }

}
