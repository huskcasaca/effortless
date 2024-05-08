package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

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
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        switch (getShape(pos1, pos2)) {
            case PLANE_Y -> {
                switch (context.planeFilling()) {
                    case PLANE_FULL -> Square.addFullSquareBlocksY(set, x1, x2, y1, z1, z2);
                    case PLANE_HOLLOW -> Square.addHollowSquareBlocksY(set, x1, x2, y1, z1, z2);
                }
            }
        }

        return set.stream();
    }

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Floor.traceFloor(player, context);
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Floor.collectFloorBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 2;
    }

}
