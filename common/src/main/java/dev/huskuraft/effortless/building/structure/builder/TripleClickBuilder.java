package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;

import java.util.Comparator;
import java.util.stream.Stream;

public abstract class TripleClickBuilder extends AbstractClickBuilder {

    private static BlockInteraction traceLineByAxis(Player player, Context context, Axis axis) {
        var center = context.secondBlockPosition().getCenter();
        var reach = context.maxReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new AxisLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(criteria -> criteria.axis != axis)
                .filter(criteria -> criteria.isInRange())
                .min(Comparator.comparing(axisLineCriteria -> axisLineCriteria.distanceToLineSqr()))
                .map(criteria -> criteria.traceLine(axis))
                .orElse(null);
    }

    public static BlockInteraction traceLineY(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.Y);
    }

    public static BlockInteraction traceLineX(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.X);
    }

    public static BlockInteraction traceLineZ(Player player, Context context) {
        return traceLineByAxis(player, context, Axis.Z);
    }

    protected static BlockInteraction tracePlaneByAxis(Player player, Context context, Axis axis) {
        var center = context.secondBlockPosition().getCenter();
        var reach = context.maxReachDistance();
        var skipRaytrace = context.skipRaytrace();

        return Stream.of(
                        new AxisLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new AxisLineCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(criteria -> criteria.axis == axis)
                .filter(criteria -> criteria.isInRange())
                .min(Comparator.comparing(axisLineCriteria -> axisLineCriteria.distanceToLineSqr()))
                .map(criteria -> criteria.tracePlane())
                .orElse(null);
    }

    public static BlockInteraction tracePlaneY(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.Y);
    }

    public static BlockInteraction tracePlaneX(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.X);
    }

    public static BlockInteraction tracePlaneZ(Player player, Context context) {
        return tracePlaneByAxis(player, context, Axis.Z);
    }

    protected abstract BlockInteraction traceFirstInteraction(Player player, Context context);

    protected abstract BlockInteraction traceSecondInteraction(Player player, Context context);

    protected abstract BlockInteraction traceThirdInteraction(Player player, Context context);

    protected abstract Stream<BlockPosition> collectStartBlocks(Context context);

    protected abstract Stream<BlockPosition> collectInterBlocks(Context context);

    protected abstract Stream<BlockPosition> collectFinalBlocks(Context context);

    @Override
    public BlockInteraction trace(Player player, Context context) {
        return switch (context.interactionsSize()) {
            case 0 -> traceFirstInteraction(player, context);
            case 1 -> traceSecondInteraction(player, context);
            case 2 -> traceThirdInteraction(player, context);
            default -> null;
        };
    }

    @Override
    public Stream<BlockPosition> collect(Context context) {
        return switch (context.interactionsSize()) {
            case 1 -> collectStartBlocks(context);
            case 2 -> collectInterBlocks(context);
            case 3 -> collectFinalBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int totalClicks(Context context) {
        return 3;
    }

    public static class AxisLineCriteria extends AxisCriteria {

        public AxisLineCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        @Override
        public Vector3d lineVec() {
            return lineVec(axis);
        }

        public Vector3d lineVec(Axis axis) {
            var pos = BlockPosition.at(center);
            var bound = BlockPosition.at(planeVec());
            return switch (axis) {
                case X -> new Vector3d(bound.x(), pos.y(), pos.z());
                case Y -> new Vector3d(pos.x(), bound.y(), pos.z());
                case Z -> new Vector3d(pos.x(), pos.y(), bound.z());
            };
        }

        public BlockInteraction traceLine(Axis axis) {
            var found = BlockPosition.at(lineVec(axis));
            return convert(found);
        }

    }
}
