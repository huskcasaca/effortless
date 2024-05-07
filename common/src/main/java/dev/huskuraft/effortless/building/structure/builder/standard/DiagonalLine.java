package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        var iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (var t = 0.0; t <= 1.0; t += 1.0 / iterations) {
            var lerp = first.add(second.sub(first).mul(t));
            var candidate = BlockPosition.at(lerp);
            // only add if not equal to the last in the list
            if (list.isEmpty() || !list.get(list.size() - 1).equals(candidate))
                list.add(candidate);
        }

        return list.stream();
    }

    public static Stream<BlockPosition> collectDiagonalLineBlocks(Context context, float sampleMultiplier) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(2).x();
        var y2 = context.getPosition(2).y();
        var z2 = context.getPosition(2).z();

        var first = new Vector3d(x1, y1, z1).add(0.5, 0.5, 0.5);
        var second = new Vector3d(x2, y2, z2).add(0.5, 0.5, 0.5);

        int iterations = (int) MathUtils.ceil(first.distance(second) * sampleMultiplier);
        for (var t = 0.0; t <= 1.0; t += 1.0 / iterations) {
            var lerp = first.add(second.sub(first).mul(t));
            var candidate = BlockPosition.at(lerp);
            // only add if not equal to the last in the list
            if (list.isEmpty() || !list.get(list.size() - 1).equals(candidate))
                list.add(candidate);
        }

        return list.stream();
    }


    public static Set<BlockPosition> getDiagonalLine(List<BlockPosition> positions, int radius, boolean hollow) {
        Set<BlockPosition> result = new LinkedHashSet<>();

        for (int i = 0; !positions.isEmpty() && i < positions.size() - 1; i++) {
            var pos1 = positions.get(i);
            var pos2 = positions.get(i + 1);

            var x1 = pos1.x();
            var y1 = pos1.y();
            var z1 = pos1.z();
            var x2 = pos2.x();
            var y2 = pos2.y();
            var z2 = pos2.z();
            var tipx = x1;
            var tipy = y1;
            var tipz = z1;
            var dx = MathUtils.abs(x2 - x1);
            var dy = MathUtils.abs(y2 - y1);
            var dz = MathUtils.abs(z2 - z1);

            if (dx + dy + dz == 0) {
                result.add(BlockPosition.at(tipx, tipy, tipz));
                continue;
            }

            int dm = MathUtils.max(MathUtils.max(dx, dy), dz);
            if (dm == dx) {
                for (var domstep = 0; domstep <= dx; domstep++) {
                    tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                    tipy = (int) MathUtils.round(y1 + domstep * ((double) dy) / ((double) dx) * (y2 - y1 > 0 ? 1 : -1));
                    tipz = (int) MathUtils.round(z1 + domstep * ((double) dz) / ((double) dx) * (z2 - z1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            } else if (dm == dy) {
                for (var domstep = 0; domstep <= dy; domstep++) {
                    tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                    tipx = (int) MathUtils.round(x1 + domstep * ((double) dx) / ((double) dy) * (x2 - x1 > 0 ? 1 : -1));
                    tipz = (int) MathUtils.round(z1 + domstep * ((double) dz) / ((double) dy) * (z2 - z1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            } else /* if (dm == dz) */ {
                for (var domstep = 0; domstep <= dz; domstep++) {
                    tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                    tipy = (int) MathUtils.round(y1 + domstep * ((double) dy) / ((double) dz) * (y2 - y1 > 0 ? 1 : -1));
                    tipx = (int) MathUtils.round(x1 + domstep * ((double) dx) / ((double) dz) * (x2 - x1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            }
        }

        result = getBallooned(result, radius);
        if (hollow) result = getHollowed(result);
        return result;
    }

    @Override
    public int volume(Context context) {
        return collect(context).toList().size();
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
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
        return getDiagonalLine(context.getPositions(), 0, false).stream();
    }

    @Override
    public int totalInteractions(Context context) {
        return 2;
    }
}
