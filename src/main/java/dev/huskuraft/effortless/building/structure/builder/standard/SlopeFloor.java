package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record SlopeFloor(
        PlaneLength planeLength, RaisedEdge raisedEdge
) implements BlockStructure {

    public SlopeFloor() {
        this(PlaneLength.VARIABLE, RaisedEdge.SHORT);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case PLANE_LENGTH -> new SlopeFloor((PlaneLength) feature, raisedEdge);
            case RAISED_EDGE -> new SlopeFloor(planeLength, (RaisedEdge) feature);
            default -> this;
        };
    }

    public static Stream<BlockPosition> collectSlopeFloorBlocks(Context context, RaisedEdge raisedEdge) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);
        var pos3 = context.getPosition(2);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();

        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        var x3 = pos3.x();
        var y3 = pos3.y();
        var z3 = pos3.z();

        var axisLimit = context.axisLimitation();

        var xLength = MathUtils.abs(x2 - x1);
        var zLength = MathUtils.abs(z2 - z1);

        var axis = switch (raisedEdge) {
            case SHORT -> {
                if (zLength > xLength) {
                    yield Axis.Z;
                } else {
                    yield Axis.X;
                }
            }
            case LONG -> {
                if (zLength > xLength) {
                    yield Axis.X;
                } else {
                    yield Axis.Z;
                }
            }
        };

        switch (axis) {
            case X -> {
                var line = DiagonalLine.collectDiagonalLine(new BlockPosition(x1, y1, z1), new BlockPosition(x2, y3, z1), 0, false).toList();
                var lowest = MathUtils.min(z1, z2);
                var highest = MathUtils.max(z1, z2);
                if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

                for (var z = lowest; z <= highest; z++) {
                    for (var blockPosition : line) {
                        set.add(new BlockPosition(blockPosition.x(), blockPosition.y(), z));
                    }
                }
            }
            case Z -> {
                var line = DiagonalLine.collectDiagonalLine(new BlockPosition(x1, y1, z1), new BlockPosition(x1, y3, z2), 0, false).toList();
                var lowest = MathUtils.min(x1, x2);
                var highest = MathUtils.max(x1, x2);
                if (highest - lowest >= axisLimit) highest = lowest + axisLimit - 1;

                for (var x = lowest; x <= highest; x++) {
                    for (var blockPosition : line) {
                        set.add(new BlockPosition(x, blockPosition.y(), blockPosition.z()));
                    }
                }
            }
        }

        return set.stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Floor.traceFloor(player, context, planeLength);
            case 2 -> Line.traceLineY(player, context.getPosition(1));
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Floor.collectFloorBlocks(context, PlaneFilling.FILLED);
            case 3 -> SlopeFloor.collectSlopeFloorBlocks(context, raisedEdge);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.SLOPE_FLOOR;
    }
}
