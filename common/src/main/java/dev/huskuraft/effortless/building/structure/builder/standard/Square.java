package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Square extends AbstractBlockStructure {

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

        var x1 = context.firstBlockInteraction().getBlockPosition().x();
        var y1 = context.firstBlockInteraction().getBlockPosition().y();
        var z1 = context.firstBlockInteraction().getBlockPosition().z();
        var x2 = context.secondBlockInteraction().getBlockPosition().x();
        var y2 = context.secondBlockInteraction().getBlockPosition().y();
        var z2 = context.secondBlockInteraction().getBlockPosition().z();

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
        var reach = context.maxNextReachDistance();
        var skipRaytrace = context.skipRaytrace();

        var result = Stream.of(
                        new NearestLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Z, player, center, reach, skipRaytrace))
                .filter(nearestLineCriteria -> context.planeFacing().isInRange(nearestLineCriteria.getAxis()))
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(NearestLineCriteria::distanceToEyeSqr))
                .map(AxisCriteria::tracePlane)
                .orElse(null);

        return transformUniformLengthInteraction(context.firstBlockInteraction(), result, context.structureParams().planeLength() == PlaneLength.EQUAL);
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
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return collectSquareBlocks(context);
    }

    @Override
    public int totalClicks(Context context) {
        return 2;
    }

    public static class NearestLineCriteria extends Line.NearestLineCriteria {

        public NearestLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }
    }

}
