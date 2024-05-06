package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class DiagonalLine extends AbstractBlockStructure {

    public static Stream<BlockPosition> collectPlaneDiagonalLineBlocks(Context context, float sampleMultiplier) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockInteraction().getBlockPosition().x();
        var y1 = context.firstBlockInteraction().getBlockPosition().y();
        var z1 = context.firstBlockInteraction().getBlockPosition().z();
        var x2 = context.secondBlockInteraction().getBlockPosition().x();
        var y2 = context.secondBlockInteraction().getBlockPosition().y();
        var z2 = context.secondBlockInteraction().getBlockPosition().z();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        var iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (double t = 0; t <= 1.0; t += 1.0 / iterations) {
            Vector3d lerp = first.add(second.sub(first).mul(t));
            BlockPosition candidate = BlockPosition.at(lerp);
            // only add if not equal to the last in the list
            if (list.isEmpty() || !list.get(list.size() - 1).equals(candidate))
                list.add(candidate);
        }

        return list.stream();
    }

    public static Stream<BlockPosition> collectDiagonalLineBlocks(Context context, float sampleMultiplier) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockInteraction().getBlockPosition().x();
        var y1 = context.firstBlockInteraction().getBlockPosition().y();
        var z1 = context.firstBlockInteraction().getBlockPosition().z();
        var x2 = context.thirdBlockInteraction().getBlockPosition().x();
        var y2 = context.thirdBlockInteraction().getBlockPosition().y();
        var z2 = context.thirdBlockInteraction().getBlockPosition().z();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        int iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (double t = 0; t <= 1.0; t += 1.0 / iterations) {
            Vector3d lerp = first.add(second.sub(first).mul(t));
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
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return collectPlaneDiagonalLineBlocks(context, 10);
    }

    @Override
    protected Stream<BlockPosition> collectThirdBlocks(Context context) {
        return collectDiagonalLineBlocks(context, 10);
    }

    @Override
    public int totalClicks(Context context) {
        return 3;
    }
}
