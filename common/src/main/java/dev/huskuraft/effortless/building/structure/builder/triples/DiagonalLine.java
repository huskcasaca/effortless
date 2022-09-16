package dev.huskuraft.effortless.building.structure.builder.triples;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;

import java.util.ArrayList;
import java.util.stream.Stream;

public class DiagonalLine extends TripleClickBuilder {

    public static Stream<BlockPosition> collectPlaneDiagonalLineBlocks(Context context, float sampleMultiplier) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.secondBlockPosition().getX();
        var y2 = context.secondBlockPosition().getY();
        var z2 = context.secondBlockPosition().getZ();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        var iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (double t = 0; t <= 1.0; t += 1.0 / iterations) {
            Vector3d lerp = first.add(second.subtract(first).scale(t));
            BlockPosition candidate = BlockPosition.at(lerp);
            // only add if not equal to the last in the list
            if (list.isEmpty() || !list.get(list.size() - 1).equals(candidate))
                list.add(candidate);
        }

        return list.stream();
    }

    public static Stream<BlockPosition> collectDiagonalLineBlocks(Context context, float sampleMultiplier) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.thirdBlockPosition().getX();
        var y2 = context.thirdBlockPosition().getY();
        var z2 = context.thirdBlockPosition().getZ();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        int iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (double t = 0; t <= 1.0; t += 1.0 / iterations) {
            Vector3d lerp = first.add(second.subtract(first).scale(t));
            BlockPosition candidate = BlockPosition.at(lerp);
            // only add if not equal to the last in the list
            if (list.isEmpty() || !list.get(list.size() - 1).equals(candidate))
                list.add(candidate);
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
        return collectPlaneDiagonalLineBlocks(context, 10);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectDiagonalLineBlocks(context, 10);
    }
}
