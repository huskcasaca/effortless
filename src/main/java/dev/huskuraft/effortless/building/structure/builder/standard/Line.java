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
import dev.huskuraft.effortless.building.structure.LineDirection;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Line(
        LineDirection lineDirection
) implements BlockStructure {

    public Line() {
        this(LineDirection.ALL);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case LINE_DIRECTION -> new Line((LineDirection) feature);
            default -> this;
        };
    }

    public static BlockInteraction traceLineOnPlane(Player player, Context context, PlaneFacing planeFacing) {
        return traceLineOnPlane(player, context.getPosition(0), context.getPosition(1), planeFacing);
    }

    public static BlockInteraction traceLineOnPlane(Player player, BlockPosition pos1, BlockPosition pos2, PlaneFacing planeFacing) {
        return switch (BlockStructure.getShape(pos1, pos2)) {
            case PLANE_X -> switch (planeFacing) {
                case BOTH, VERTICAL -> Line.traceLineX(player, pos2);
                case HORIZONTAL -> null;
            };
            case PLANE_Y -> switch (planeFacing) {
                case BOTH, HORIZONTAL -> Line.traceLineY(player, pos2);
                case VERTICAL -> null;
            };
            case PLANE_Z -> switch (planeFacing) {
                case BOTH, VERTICAL -> Line.traceLineZ(player, pos2);
                case HORIZONTAL -> null;
            };
            default -> null;
        };
    }

    public static BlockInteraction traceLineOnPlane(Player player, BlockPosition pos1, BlockPosition pos2) {
        return switch (BlockStructure.getShape(pos1, pos2)) {
            case PLANE_X -> Line.traceLineX(player, pos2);
            case PLANE_Y -> Line.traceLineY(player, pos2);
            case PLANE_Z -> Line.traceLineZ(player, pos2);
            default -> null;
        };
    }

    public static BlockInteraction traceLineOnVerticalPlane(Player player, BlockPosition pos1, BlockPosition pos2) {
        return switch (BlockStructure.getShape(pos1, pos2)) {
            case PLANE_X -> Line.traceLineX(player, pos2);
            case PLANE_Z -> Line.traceLineZ(player, pos2);
            default -> null;
        };
    }

    public static BlockInteraction traceLineOnHorizontalPlane(Player player, BlockPosition pos1, BlockPosition pos2) {
        return switch (BlockStructure.getShape(pos1, pos2)) {
            case PLANE_Y -> Line.traceLineY(player, pos2);
            default -> null;
        };
    }

    public static BlockInteraction traceLine(Player player, Context context, LineDirection lineDirection) {
        return traceLine(player, context.getPosition(0), lineDirection.getAxes());
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
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        addLineBlocks(set, x1, y1, z1, x2, y2, z2);

        return set.stream();
    }

    public static void addLineBlocks(Set<BlockPosition> set, int x1, int y1, int z1, int x2, int y2, int z2) {
        switch (BlockStructure.getShape(x1, y1, z1, x2, y2, z2)) {
            case SINGLE -> Single.addSingleBlock(set, x1, y1, z1);
            case LINE_X -> addXLineBlocks(set, x1, x2, y1, z1);
            case LINE_Y -> addYLineBlocks(set, y1, y2, x1, z1);
            case LINE_Z -> addZLineBlocks(set, z1, z2, x1, y1);
        }
    }

    public static void addXLineBlocks(Set<BlockPosition> set, int x1, int x2, int y, int z) {
        for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
            set.add(new BlockPosition(x, y, z));
        }
    }

    public static void addYLineBlocks(Set<BlockPosition> set, int y1, int y2, int x, int z) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            set.add(new BlockPosition(x, y, z));
        }
    }

    public static void addZLineBlocks(Set<BlockPosition> set, int z1, int z2, int x, int y) {
        for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
            set.add(new BlockPosition(x, y, z));
        }
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Line.traceLine(player, context, lineDirection);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
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

    @Override
    public BuildMode getMode() {
        return BuildMode.LINE;
    }
}
