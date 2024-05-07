package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class DiagonalWall extends AbstractBlockStructure {

    // add diagonal wall from first to second
    public static Stream<BlockPosition> collectDiagonalWallBlocks(Context context) {
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

        // get diagonal line blocks
        var diagonalLineBlocks = DiagonalLine.collectPlaneDiagonalLineBlocks(context, 1).toList();

        int lowest = MathUtils.min(y1, y3);
        int highest = MathUtils.max(y1, y3);

        // copy diagonal line on y axis
        for (int y = lowest; y <= highest; y++) {
            for (BlockPosition blockPosition : diagonalLineBlocks) {
                list.add(new BlockPosition(blockPosition.x(), y, blockPosition.z()));
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
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return DiagonalLine.collectPlaneDiagonalLineBlocks(context, 1);
    }

    @Override
    protected Stream<BlockPosition> collectThirdBlocks(Context context) {
        return collectDiagonalWallBlocks(context);
    }

    @Override
    public int totalClicks(Context context) {
        return 3;
    }
}
