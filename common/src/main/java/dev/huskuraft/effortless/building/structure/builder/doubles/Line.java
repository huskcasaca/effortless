package dev.huskuraft.effortless.building.structure.builder.doubles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

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

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

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
            var size = bound.sub(pos);

            size = Vector3i.at(MathUtils.abs(size.x()), MathUtils.abs(size.y()), MathUtils.abs(size.z()));
            int longest = MathUtils.max(size.x(), MathUtils.max(size.y(), size.z()));
            if (longest == size.x()) {
                return new Vector3d(bound.x(), pos.y(), pos.z());
            }
            if (longest == size.y()) {
                return new Vector3d(pos.x(), bound.y(), pos.z());
            }
            if (longest == size.z()) {
                return new Vector3d(pos.x(), pos.y(), bound.z());
            }
            return null;
        }

        @Override
        public double distanceToLineSqr() {
            return planeVec().sub(lineVec()).lengthSq() * (axis == Axis.Y ? 2 : 1);
        }
    }
}
