package dev.huskuraft.effortless.building.structure.builder.triples;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

public class SlopeFloor extends TripleClickBuilder {

    // add slope floor from first to second
    public static Stream<BlockPosition> collectSlopeFloorBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();

        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        var x3 = context.thirdBlockPosition().x();
        var y3 = context.thirdBlockPosition().y();
        var z3 = context.thirdBlockPosition().z();

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
            var diagonalLineBlocks = DiagonalLine.collectPlaneDiagonalLineBlocks(context.withEmptyInteractions().withNextInteraction(context.firstBlockInteraction().withPosition(new BlockPosition(x1, y1, z1))).withNextInteraction(context.firstBlockInteraction().withPosition(new BlockPosition(x2, y3, z1))), 1f).toList();

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
            var diagonalLineBlocks = DiagonalLine.collectPlaneDiagonalLineBlocks(context.withEmptyInteractions().withNextInteraction(context.firstBlockInteraction().withPosition(new BlockPosition(x1, y1, z1))).withNextInteraction(context.firstBlockInteraction().withPosition(new BlockPosition(x1, y3, z2))), 1f).toList();

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

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return Floor.traceFloor(player, context);
    }

    @Override
    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        return traceLineY(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectStartBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectInterBlocks(Context context) {
        return Floor.collectFloorBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectSlopeFloorBlocks(context);
    }
}
