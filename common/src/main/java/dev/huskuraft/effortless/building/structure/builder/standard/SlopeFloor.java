package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class SlopeFloor extends AbstractBlockStructure {

    // add slope floor from first to second
    public static Stream<BlockPosition> collectSlopeFloorBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();

        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        var x3 = context.getPosition(2).x();
        var y3 = context.getPosition(2).y();
        var z3 = context.getPosition(2).z();

        int axisLimit = context.axisLimitation();

        // determine whether to use x or z axis to slope up
        boolean onXAxis = true;

        int xLength = MathUtils.abs(x2 - x1);
        int zLength = MathUtils.abs(z2 - z1);

        if (context.raisedEdge() == RaisedEdge.RAISE_SHORT_EDGE) {
            // slope along short edge
            if (zLength > xLength) onXAxis = false;
        } else {
            // slope along long edge
            if (zLength <= xLength) onXAxis = false;
        }

        if (onXAxis) {
            // along X goes up

            // get diagonal line blocks
            // FIXME: 8/8/23 block hit location is not correct
            var diagonalLineBlocks = DiagonalLine.collectPlaneDiagonalLineBlocks(context.withEmptyInteractions().withNextInteraction(context.getInteraction(0).withBlockPosition(new BlockPosition(x1, y1, z1))).withNextInteraction(context.getInteraction(0).withBlockPosition(new BlockPosition(x2, y3, z1))), 1f).toList();

            // limit amount of blocks we can place
            int lowest = MathUtils.min(z1, z2);
            int highest = MathUtils.max(z1, z2);

            if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

            // copy diagonal line on x axis
            for (int z = lowest; z <= highest; z++) {
                for (BlockPosition blockPosition : diagonalLineBlocks) {
                    list.add(new BlockPosition(blockPosition.x(), blockPosition.y(), z));
                }
            }

        } else {
            // along Z goes up

            // get diagonal line blocks
            // FIXME: 8/8/23 block hit location is not correct
            var diagonalLineBlocks = DiagonalLine.collectPlaneDiagonalLineBlocks(context.withEmptyInteractions().withNextInteraction(context.getInteraction(0).withBlockPosition(new BlockPosition(x1, y1, z1))).withNextInteraction(context.getInteraction(0).withBlockPosition(new BlockPosition(x1, y3, z2))), 1f).toList();

            // limit amount of blocks we can place
            int lowest = MathUtils.min(x1, x2);
            int highest = MathUtils.max(x1, x2);

            if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

            // copy diagonal line on x axis
            for (int x = lowest; x <= highest; x++) {
                for (BlockPosition blockPosition : diagonalLineBlocks) {
                    list.add(new BlockPosition(x, blockPosition.y(), blockPosition.z()));
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
