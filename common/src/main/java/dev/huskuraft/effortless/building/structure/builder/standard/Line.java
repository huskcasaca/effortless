package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Line extends AbstractBlockStructure {


    public static BlockInteraction traceLineOnPlane(Player player, BlockPosition pos0, BlockPosition pos1) {
        return switch (getShape(pos0, pos1)) {
            case PLANE_X -> Line.traceLineX(player, pos1);
            case PLANE_Y -> Line.traceLineY(player, pos1);
            case PLANE_Z -> Line.traceLineZ(player, pos1);
            default -> null;
        };
    }

    public static BlockInteraction traceLineOnVerticalPlane(Player player, BlockPosition pos0, BlockPosition pos1) {
        return switch (getShape(pos0, pos1)) {
            case PLANE_X -> Line.traceLineX(player, pos1);
            case PLANE_Z -> Line.traceLineZ(player, pos1);
            default -> null;
        };
    }

    public static BlockInteraction traceLineOnHorizontalPlane(Player player, BlockPosition pos0, BlockPosition pos1) {
        return switch (getShape(pos0, pos1)) {
            case PLANE_Y -> Line.traceLineY(player, pos1);
            default -> null;
        };
    }

    public static BlockInteraction traceLine(Player player, Context context) {
        return traceLine(player, context.getPosition(0), context.lineDirection().getAxes());
    }

    public static BlockInteraction traceLineX(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.X));
    }

    public static BlockInteraction traceLineY(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.Y));
    }

    public static BlockInteraction traceLineZ(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.Z));
    }

    public static BlockInteraction traceLineXZ(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.X, Axis.Z));
    }

    public static BlockInteraction traceLineYZ(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.Y, Axis.Z));
    }

    public static BlockInteraction traceLineXY(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.X, Axis.Y));
    }

    public static BlockInteraction traceLine(Player player, BlockPosition start) {
        return traceLine(player, start, Set.of(Axis.X, Axis.Y, Axis.Z));
    }

    public static BlockInteraction traceLine(Player player, BlockPosition start, Set<Axis> axes) {
        var center = start.getCenter();
        var reach = 1024;
        var skipRaytrace = false;

        return Stream.of(
                        new NearestAxisLineCriteria(axes, Axis.X, player, center, reach, skipRaytrace),
                        new NearestAxisLineCriteria(axes, Axis.Y, player, center, reach, skipRaytrace),
                        new NearestAxisLineCriteria(axes, Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(NearestLineCriteria::distanceToLineSqr))
                .map(AxisCriteria::traceLine)
                .orElse(null);
    }

    public static Stream<BlockPosition> collectLineBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var pos0 = context.getPosition(0);
        var pos1 = context.getPosition(1);

        var x1 = pos0.x();
        var y1 = pos0.y();
        var z1 = pos0.z();
        var x2 = pos1.x();
        var y2 = pos1.y();
        var z2 = pos1.z();

        switch (getShape(pos0, pos1)) {
            case LINE_X -> addXLineBlocks(list, x1, x2, y1, z1);
            case LINE_Y -> addYLineBlocks(list, y1, y2, x1, z1);
            case LINE_Z -> addZLineBlocks(list, z1, z2, x1, y1);
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

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Line.traceLine(player, context);
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Line.collectLineBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 2;
    }

}
