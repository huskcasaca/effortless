package dev.huskuraft.effortless.building.structure.builder.triples;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.MathUtils;

import java.util.ArrayList;
import java.util.stream.Stream;

public class DiagonalWall extends TripleClickBuilder {

    // add diagonal wall from first to second
    public static Stream<BlockPosition> collectDiagonalWallBlocks(Context context) {
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
    protected Stream<BlockPosition> collectStartBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectInterBlocks(Context context) {
        return DiagonalLine.collectPlaneDiagonalLineBlocks(context, 1);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectDiagonalWallBlocks(context);
    }
}