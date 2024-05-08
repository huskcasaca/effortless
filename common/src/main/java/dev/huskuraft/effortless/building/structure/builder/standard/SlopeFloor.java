package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class SlopeFloor extends AbstractBlockStructure {

    public static Stream<BlockPosition> collectSlopeFloorBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);
        var pos3 = context.getPosition(2);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();

        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        var x3 = pos3.x();
        var y3 = pos3.y();
        var z3 = pos3.z();

        var axisLimit = context.axisLimitation();

        var xLength = MathUtils.abs(x2 - x1);
        var zLength = MathUtils.abs(z2 - z1);

        var axis = switch (context.raisedEdge()) {
            case RAISE_SHORT_EDGE -> {
                if (zLength > xLength) {
                    yield Axis.Z;
                } else {
                    yield Axis.X;
                }
            }
            case RAISE_LONG_EDGE -> {
                if (zLength > xLength) {
                    yield Axis.X;
                } else {
                    yield Axis.Z;
                }
            }
        };

        switch (axis) {
            case X -> {
                var line = DiagonalLine.collectDiagonalLine(new BlockPosition(x1, y1, z1), new BlockPosition(x2, y3, z1), 0, false).toList();
                var lowest = MathUtils.min(z1, z2);
                var highest = MathUtils.max(z1, z2);
                if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

                for (var z = lowest; z <= highest; z++) {
                    for (var blockPosition : line) {
                        list.add(new BlockPosition(blockPosition.x(), blockPosition.y(), z));
                    }
                }
            }
            case Z -> {
                var line = DiagonalLine.collectDiagonalLine(new BlockPosition(x1, y1, z1), new BlockPosition(x1, y3, z2), 0, false).toList();
                var lowest = MathUtils.min(x1, x2);
                var highest = MathUtils.max(x1, x2);
                if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

                for (var x = lowest; x <= highest; x++) {
                    for (var blockPosition : line) {
                        list.add(new BlockPosition(x, blockPosition.y(), blockPosition.z()));
                    }
                }
            }
        }

        return list.stream();
    }

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Floor.traceFloor(player, context);
            case 2 -> Line.traceLineY(player, context.getPosition(1));
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Floor.collectFloorBlocks(context);
            case 3 -> SlopeFloor.collectSlopeFloorBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }
}
