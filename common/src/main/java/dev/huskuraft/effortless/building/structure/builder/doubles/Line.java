package dev.huskuraft.effortless.building.structure.builder.doubles;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.math.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Line extends DoubleClickBuilder {

    public static BlockInteraction traceLine(Player player, Context context) {
        var center = context.firstBlockPosition().getCenter();
        var reach = context.maxReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new NearestLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .reduce((nearest, criteria) -> {
                    if (criteria.distanceToLineSqr() < 2.0 && nearest.distanceToLineSqr() < 2.0) {
                        if (criteria.distanceToEyeSqr() < nearest.distanceToEyeSqr()) return criteria;
                    } else {
                        if (criteria.distanceToLineSqr() < nearest.distanceToLineSqr()) return criteria;
                    }
                    return nearest;
                })
//                .min(Comparator.comparing(NearestLineCriteria::distanceToLineSqr))
                .map(AxisCriteria::traceLine)
                .orElse(null);
    }

    public static Stream<BlockPosition> collectLineBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.secondBlockPosition().getX();
        var y2 = context.secondBlockPosition().getY();
        var z2 = context.secondBlockPosition().getZ();

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
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectLineBlocks(context);
    }

    static class NearestLineCriteria extends AxisCriteria {


        public NearestLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        @Override
        public Vector3d lineVec() {
            var pos = Vector3i.at(center);
            var bound = Vector3i.at(planeVec());
            var size = bound.subtract(pos);

            size = Vector3i.at(MathUtils.abs(size.getX()), MathUtils.abs(size.getY()), MathUtils.abs(size.getZ()));
            int longest = MathUtils.max(size.getX(), MathUtils.max(size.getY(), size.getZ()));
            if (longest == size.getX()) {
                return new Vector3d(bound.getX(), pos.getY(), pos.getZ());
            }
            if (longest == size.getY()) {
                return new Vector3d(pos.getX(), bound.getY(), pos.getZ());
            }
            if (longest == size.getZ()) {
                return new Vector3d(pos.getX(), pos.getY(), bound.getZ());
            }
            return null;
        }

        @Override
        public double distanceToLineSqr() {
            return planeVec().subtract(lineVec()).lengthSq() * (axis == Axis.Y ? 2 : 1);
        }
    }
}
