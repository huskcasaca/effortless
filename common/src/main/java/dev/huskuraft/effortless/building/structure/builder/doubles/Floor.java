package dev.huskuraft.effortless.building.structure.builder.doubles;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.Vector3d;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Floor extends DoubleClickBuilder {

    public static BlockInteraction traceFloor(Player player, Context context) {
        var center = context.firstBlockPosition().getCenter();
        var reach = context.maxReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new FloorCriteria(Axis.Y, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .findFirst()
                .map(AxisCriteria::tracePlane)
                .orElse(null);
    }

    public static Stream<BlockPosition> collectFloorBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.secondBlockPosition().getX();
        var y2 = context.secondBlockPosition().getY();
        var z2 = context.secondBlockPosition().getZ();

        if (y1 == y2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> Square.addFullSquareBlocksY(list, x1, x2, y1, z1, z2);
                case PLANE_HOLLOW -> Square.addHollowSquareBlocksY(list, x1, x2, y1, z1, z2);
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
        return traceFloor(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectFloorBlocks(context);
    }

    public static class FloorCriteria extends Line.NearestLineCriteria {

        public FloorCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }
    }

}
