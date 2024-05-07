package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Floor extends AbstractBlockStructure {

    protected static BlockInteraction traceFloor(Player player, Context context) {
        return traceFloor(player, context.getInteraction(0), context.structureParams().planeLength() == PlaneLength.EQUAL);
    }

    protected static BlockInteraction traceFloor(Player player, BlockInteraction start, boolean uniformLength) {
        var center = start.getBlockPosition().getCenter();
        var reach = 1024;
        var skipRaytrace = false;

        var result = Stream.of(
                        new Line.NearestLineCriteria(Axis.Y, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .findAny()
                .map(AxisCriteria::tracePlane)
                .orElse(null);

        return transformUniformLengthInteraction(start, result, uniformLength);
    }

    public static Stream<BlockPosition> collectFloorBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

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
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return collectFloorBlocks(context);
    }

    @Override
    public int totalInteractions(Context context) {
        return 2;
    }

}
