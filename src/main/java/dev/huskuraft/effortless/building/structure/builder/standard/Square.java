package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Square(
        PlaneFacing planeFacing,
        PlaneFilling planeFilling,
        PlaneLength planeLength
) implements BlockStructure {

    public Square() {
        this(PlaneFacing.BOTH, PlaneFilling.FILLED, PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case PLANE_FACING -> new Square((PlaneFacing) feature, planeFilling, planeLength);
            case PLANE_FILLING -> new Square(planeFacing, (PlaneFilling) feature, planeLength);
            case PLANE_LENGTH -> new Square(planeFacing, planeFilling, (PlaneLength) feature);
            default -> this;
        };
    }

    public static void addFullSquareBlocksX(Set<BlockPosition> set, int x, int y1, int y2, int z1, int z2) {
        for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
            for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
                set.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addFullSquareBlocksY(Set<BlockPosition> set, int x1, int x2, int y, int z1, int z2) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
                set.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addFullSquareBlocksZ(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
                set.add(new BlockPosition(x, y, z));
            }
        }
    }

    public static void addHollowSquareBlocksX(Set<BlockPosition> set, int x, int y1, int y2, int z1, int z2) {
        Line.addZLineBlocks(set, z1, z2, x, y1);
        Line.addZLineBlocks(set, z1, z2, x, y2);
        Line.addYLineBlocks(set, y1, y2, x, z1);
        Line.addYLineBlocks(set, y1, y2, x, z2);
    }

    public static void addHollowSquareBlocksY(Set<BlockPosition> set, int x1, int x2, int y, int z1, int z2) {
        Line.addXLineBlocks(set, x1, x2, y, z1);
        Line.addXLineBlocks(set, x1, x2, y, z2);
        Line.addZLineBlocks(set, z1, z2, x1, y);
        Line.addZLineBlocks(set, z1, z2, x2, y);
    }

    public static void addHollowSquareBlocksZ(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z) {
        Line.addXLineBlocks(set, x1, x2, y1, z);
        Line.addXLineBlocks(set, x1, x2, y2, z);
        Line.addYLineBlocks(set, y1, y2, x1, z);
        Line.addYLineBlocks(set, y1, y2, x2, z);
    }

    public static void addFullSquareBlocks(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z1, int z2) {
        if (y1 == y2) {
            addFullSquareBlocksY(set, x1, x2, y1, z1, z2);
        } else if (x1 == x2) {
            addFullSquareBlocksX(set, x1, y1, y2, z1, z2);
        } else if (z1 == z2) {
            addFullSquareBlocksZ(set, x1, x2, y1, y2, z1);
        }
    }

    public static void addHollowSquareBlocks(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z1, int z2) {
        if (y1 == y2) {
            addHollowSquareBlocksY(set, x1, x2, y1, z1, z2);
        } else if (x1 == x2) {
            addHollowSquareBlocksX(set, x1, y1, y2, z1, z2);
        } else if (z1 == z2) {
            addHollowSquareBlocksZ(set, x1, x2, y1, y2, z1);
        }
    }

    public static Stream<BlockPosition> collectSquareBlocks(Context context, PlaneFilling planeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        switch (BlockStructure.getShape(pos1, pos2)) {
            case SINGLE -> Single.addSingleBlock(set, x1, y1, z1);
            case LINE_X, LINE_Y, LINE_Z -> Line.addLineBlocks(set, x1, y1, z1, x2, y2, z2);
            case PLANE_X -> {
                switch (planeFilling) {
                    case FILLED -> addFullSquareBlocksX(set, x1, y1, y2, z1, z2);
                    case HOLLOW -> addHollowSquareBlocksX(set, x1, y1, y2, z1, z2);
                };
            }
            case PLANE_Y -> {
                switch (planeFilling) {
                    case FILLED -> addFullSquareBlocksY(set, x1, x2, y1, z1, z2);
                    case HOLLOW -> addHollowSquareBlocksY(set, x1, x2, y1, z1, z2);
                }
            }
            case PLANE_Z -> {
                switch (planeFilling) {
                    case FILLED -> addFullSquareBlocksZ(set, x1, x2, y1, y2, z1);
                    case HOLLOW -> addHollowSquareBlocksZ(set, x1, x2, y1, y2, z1);
                }
            }
        }

        return set.stream();
    }

    protected static BlockInteraction traceSquare(Player player, Context context, PlaneFacing planeFacing, PlaneLength planeLength) {
        return traceSquare(player, context.getInteraction(0), planeFacing.getAxes(), planeLength == PlaneLength.EQUAL);
    }

    protected static BlockInteraction traceSquare(Player player, BlockInteraction start, Set<Axis> axes, boolean uniformLength) {
        var center = start.getBlockPosition().getCenter();
        var reach = 1024;
        var skipRaytrace = false;

        var result = Stream.of(
                        new NearestLineCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Y, player, center, reach, skipRaytrace),
                        new NearestLineCriteria(Axis.Z, player, center, reach, skipRaytrace))
                .filter(nearestLineCriteria -> axes.contains(nearestLineCriteria.getAxis()))
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(NearestLineCriteria::distanceToEyeSqr))
                .map(AxisCriteria::tracePlane)
                .orElse(null);

        return BlockStructure.transformUniformLengthInteraction(start, result, uniformLength);
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context, planeFacing, planeLength);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Square.collectSquareBlocks(context, planeFilling);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 2;
    }

    @Override
    public BuildMode getMode() {
        return null;
    }
}
