package dev.huskuraft.effortless.building.structure.builder.standard;

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
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Wall extends AbstractBlockStructure {

    protected static BlockInteraction traceWall(Player player, Context context) {
        return traceWall(player, context.getInteraction(0), context.structureParams().planeLength() == PlaneLength.EQUAL);
    }

    protected static BlockInteraction traceWall(Player player, BlockInteraction start, boolean uniformLength) {
        var center = start.getBlockPosition().getCenter();
        var reach = 1024;
        var skipRaytrace = false;

        var result = Stream.of(
                        new WallCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new WallCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(WallCriteria::angle))
                .map(AxisCriteria::tracePlane)
                .orElse(null);

        return transformUniformLengthInteraction(start, result, uniformLength);
    }


    public static int sign(int a) {
        return ((int) Math.signum(a)) == 0 ? 1 : (int) Math.signum(a);
    }

    public static Stream<BlockPosition> collectWallBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

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
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return collectWallBlocks(context);
    }

    @Override
    public Stream<BlockPosition> collect(Context context) {
        return switch (context.interactionsSize()) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Wall.collectWallBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int totalInteractions(Context context) {
        return 2;
    }

    protected static class WallCriteria extends AxisCriteria {

        public WallCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        public double angle() {
            var wall = planeVec().sub(startVec());
            return MathUtils.abs(wall.x() * look.x()) + Math.abs(wall.z() * look.z());
        }

        public double distanceAngle() {
            return distanceToEyeSqr() * angle();
        }

    }

}
