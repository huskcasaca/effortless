package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Line extends AbstractBlockStructure {

    public static BlockInteraction traceLine(Player player, Context context) {
        var center = context.getPosition(0).getCenter();
        var reach = context.maxNextReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new NearestAxisLineCriteria(context.lineDirection().getAxes(), Axis.X, player, center, reach, skipRaytrace),
                        new NearestAxisLineCriteria(context.lineDirection().getAxes(), Axis.Y, player, center, reach, skipRaytrace),
                        new NearestAxisLineCriteria(context.lineDirection().getAxes(), Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(NearestLineCriteria::distanceToLineSqr))
                .map(AxisCriteria::traceLine)
                .orElse(null);
    }

    public static Stream<BlockPosition> collectLineBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        if (x1 != x2) {
            addXLineBlocks(list, x1, x2, y1, z1);
        } else if (y1 != y2) {
            addYLineBlocks(list, y1, y2, x1, z1);
        } else {
            addZLineBlocks(list, z1, z2, x1, y1);
        }

        return list.stream();
    }

    public static void addXLineBlocks(List<BlockPosition> list, int x1, int x2, int y, int z) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            list.add(new BlockPosition(x, y, z));
        }
    }

    public static void addYLineBlocks(List<BlockPosition> list, int y1, int y2, int x, int z) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            list.add(new BlockPosition(x, y, z));
        }
    }

    public static void addZLineBlocks(List<BlockPosition> list, int z1, int z2, int x, int y) {
        for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
            list.add(new BlockPosition(x, y, z));
        }
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return traceLine(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return collectLineBlocks(context);
    }

    @Override
    public int totalInteractions(Context context) {
        return 2;
    }

}
