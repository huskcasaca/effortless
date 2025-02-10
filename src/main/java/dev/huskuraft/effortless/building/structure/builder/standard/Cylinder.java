package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Cylinder(
        CircleStart circleStart,
        PlaneFilling planeFilling,
        PlaneFacing planeFacing,
        PlaneLength planeLength
) implements BlockStructure {

    public Cylinder() {
        this(CircleStart.CORNER, PlaneFilling.FILLED, PlaneFacing.BOTH, PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case CIRCLE_START -> new Cylinder((CircleStart) feature, planeFilling, planeFacing, planeLength);
            case PLANE_FILLING -> new Cylinder(circleStart, (PlaneFilling) feature, planeFacing, planeLength);
            case PLANE_FACING -> new Cylinder(circleStart, planeFilling, (PlaneFacing) feature, planeLength);
            case PLANE_LENGTH -> new Cylinder(circleStart, planeFilling, planeFacing, (PlaneLength) feature);
            default -> this;
        };
    }

    public static Stream<BlockPosition> collectCylinderBlocks(Context context,
                                                              CircleStart circleStart,
                                                              PlaneFilling planeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);
        var pos3 = context.getPosition(2);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x3 = pos3.x();
        var y3 = pos3.y();
        var z3 = pos3.z();

        switch (BlockStructure.getShape(pos1, pos2)) {
            case PLANE_X -> {
                for (int x = x1; x1 < x3 ? x <= x3 : x >= x3; x += x1 < x3 ? 1 : -1) {
                    int x0 = x;
                    set.addAll(Circle.collectCircleBlocks(context, circleStart, planeFilling).map(blockPosition -> new BlockPosition(x0, blockPosition.y(), blockPosition.z())).toList());
                }
            }
            case PLANE_Y -> {
                for (int y = y1; y1 < y3 ? y <= y3 : y >= y3; y += y1 < y3 ? 1 : -1) {
                    int y0 = y;
                    set.addAll(Circle.collectCircleBlocks(context, circleStart, planeFilling).map(blockPosition -> new BlockPosition(blockPosition.x(), y0, blockPosition.z())).toList());
                }
            }
            case PLANE_Z -> {
                for (int z = z1; z1 < z3 ? z <= z3 : z >= z3; z += z1 < z3 ? 1 : -1) {
                    int z0 = z;
                    set.addAll(Circle.collectCircleBlocks(context, circleStart, planeFilling).map(blockPosition -> new BlockPosition(blockPosition.x(), blockPosition.y(), z0)).toList());
                }
            }
        }

        return set.stream();
    }


    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context, planeFacing, planeLength);
            case 2 -> switch (planeFacing) {
                case BOTH -> Line.traceLineOnPlane(player, context.getPosition(0), context.getPosition(1));
                case VERTICAL -> Line.traceLineOnVerticalPlane(player, context.getPosition(0), context.getPosition(1));
                case HORIZONTAL -> Line.traceLineOnHorizontalPlane(player, context.getPosition(0), context.getPosition(1));
            };
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Circle.collectCircleBlocks(context, circleStart, planeFilling);
            case 3 -> Cylinder.collectCylinderBlocks(context, circleStart, planeFilling);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.CYLINDER;
    }
}
