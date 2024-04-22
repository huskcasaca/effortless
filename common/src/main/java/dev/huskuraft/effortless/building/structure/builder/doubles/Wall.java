package dev.huskuraft.effortless.building.structure.builder.doubles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.UniformLength;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

public class Wall extends DoubleClickBuilder {

    public static BlockInteraction traceWall(Player player, Context context) {
        var center = context.firstBlockPosition().getCenter();
        var reach = context.maxNextReachDistance();
        var skipRaytrace = context.skipRaytrace();
        var uniformLength = context.structureParams().uniformLength() != UniformLength.DISABLE;

        var result = Stream.of(
                        new WallCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new WallCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(WallCriteria::distanceAngle))
                .map(AxisCriteria::tracePlane)
                .orElse(null);


        if (result != null && uniformLength) {
            var firstBlockPos = context.firstBlockInteraction().getBlockPosition();
            var secondBlockPos = result.getBlockPosition();

            var diff = secondBlockPos.sub(firstBlockPos);
            if (diff.x() == 0) {
                result = result.withPosition(firstBlockPos.add(diff.withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.y()), MathUtils.abs(diff.z())))));
            } else if (diff.z() == 0) {
                result = result.withPosition(firstBlockPos.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y()))).withY(axisSign(diff.y()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.y())))));
            } else if (diff.y() == 0) {
                result = result.withPosition(firstBlockPos.add(diff.withX(axisSign(diff.x()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z()))).withZ(axisSign(diff.z()) * MathUtils.max(MathUtils.abs(diff.x()), MathUtils.abs(diff.z())))));
            }
        }
        return result;
    }


    public static int sign(int a) {
        return ((int) Math.signum(a)) == 0 ? 1 : (int) Math.signum(a);
    }

    public static Stream<BlockPosition> collectWallBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        if (x1 == x2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> Square.addFullSquareBlocksX(list, x1, y1, y2, z1, z2);
                case PLANE_HOLLOW -> Square.addHollowSquareBlocksX(list, x1, y1, y2, z1, z2);
            }
        } else if (z1 == z2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> Square.addFullSquareBlocksZ(list, x1, x2, y1, y2, z1);
                case PLANE_HOLLOW -> Square.addHollowSquareBlocksZ(list, x1, x2, y1, y2, z1);
            }
        }

        return list.stream();
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return traceWall(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectWallBlocks(context);
    }

    public static class WallCriteria extends AxisCriteria {

        public WallCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        public double angle() {
            var wall = planeVec().sub(startVec());
            return wall.x() * look.x() + wall.z() * look.z();
        }

        public double distanceAngle() {
            return distanceToEyeSqr() * angle();
        }

    }

}
