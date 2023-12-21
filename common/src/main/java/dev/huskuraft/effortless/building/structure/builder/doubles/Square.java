package dev.huskuraft.effortless.building.structure.builder.doubles;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Square extends DoubleClickBuilder {

    public static void addFullSquareBlocksX(List<BlockPosition> list, int x, int y1, int y2, int z1, int z2) {
        for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
            for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
                list.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addFullSquareBlocksY(List<BlockPosition> list, int x1, int x2, int y, int z1, int z2) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
                list.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addFullSquareBlocksZ(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
                list.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addHollowSquareBlocksX(List<BlockPosition> list, int x, int y1, int y2, int z1, int z2) {
        Line.addZLineBlocks(list, z1, z2, x, y1);
        Line.addZLineBlocks(list, z1, z2, x, y2);
        Line.addYLineBlocks(list, y1, y2, x, z1);
        Line.addYLineBlocks(list, y1, y2, x, z2);
    }

    public static void addHollowSquareBlocksY(List<BlockPosition> list, int x1, int x2, int y, int z1, int z2) {
        Line.addXLineBlocks(list, x1, x2, y, z1);
        Line.addXLineBlocks(list, x1, x2, y, z2);
        Line.addZLineBlocks(list, z1, z2, x1, y);
        Line.addZLineBlocks(list, z1, z2, x2, y);
    }

    public static void addHollowSquareBlocksZ(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z) {
        Line.addXLineBlocks(list, x1, x2, y1, z);
        Line.addXLineBlocks(list, x1, x2, y2, z);
        Line.addYLineBlocks(list, y1, y2, x1, z);
        Line.addYLineBlocks(list, y1, y2, x2, z);
    }

    public static void addFullSquareBlocks(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z1, int z2) {
        if (y1 == y2) {
            addFullSquareBlocksY(list, x1, x2, y1, z1, z2);
        } else if (x1 == x2) {
            addFullSquareBlocksX(list, x1, y1, y2, z1, z2);
        } else if (z1 == z2) {
            addFullSquareBlocksZ(list, x1, x2, y1, y2, z1);
        }
    }

    public static void addHollowSquareBlocks(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z1, int z2) {
        if (y1 == y2) {
            addHollowSquareBlocksY(list, x1, x2, y1, z1, z2);
        } else if (x1 == x2) {
            addHollowSquareBlocksX(list, x1, y1, y2, z1, z2);
        } else if (z1 == z2) {
            addHollowSquareBlocksZ(list, x1, x2, y1, y2, z1);
        }
    }

    public static Stream<BlockPosition> collectSquareBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        if (y1 == y2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> addFullSquareBlocksY(list, x1, x2, y1, z1, z2);
                case PLANE_HOLLOW -> addHollowSquareBlocksY(list, x1, x2, y1, z1, z2);
            }
        } else if (x1 == x2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> addFullSquareBlocksX(list, x1, y1, y2, z1, z2);
                case PLANE_HOLLOW -> addHollowSquareBlocksX(list, x1, y1, y2, z1, z2);
            }
        } else if (z1 == z2) {
            switch (context.planeFilling()) {
                case PLANE_FULL -> addFullSquareBlocksZ(list, x1, x2, y1, y2, z1);
                case PLANE_HOLLOW -> addHollowSquareBlocksZ(list, x1, x2, y1, y2, z1);
            }
        }

        return list.stream();
    }

    public static BlockInteraction traceSquare(Player player, Context context) {
        var center = context.firstBlockPosition().getCenter();
        var reach = context.maxReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        context.planeFacing() != PlaneFacing.HORIZONTAL ? new NearestLineCriteria(Axis.X, player, center, reach, skipRaytrace) : null,
                        context.planeFacing() != PlaneFacing.VERTICAL ? new NearestLineCriteria(Axis.Y, player, center, reach, skipRaytrace) : null,
                        context.planeFacing() != PlaneFacing.HORIZONTAL ? new NearestLineCriteria(Axis.Z, player, center, reach, skipRaytrace) : null)
                .filter(Objects::nonNull)
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(NearestLineCriteria::distanceToLineSqr))
                .map(AxisCriteria::tracePlane)
                .orElse(null);
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return traceSquare(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectSquareBlocks(context);
    }

    public static class NearestLineCriteria extends Line.NearestLineCriteria {

        public NearestLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }
    }

}
