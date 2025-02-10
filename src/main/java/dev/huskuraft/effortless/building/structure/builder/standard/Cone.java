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
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Cone(
        CircleStart circleStart,
        PlaneLength planeLength
) implements BlockStructure {

    public Cone() {
        this(CircleStart.CORNER, PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case CIRCLE_START -> new Cone((CircleStart) feature, planeLength);
            case PLANE_LENGTH -> new Cone(circleStart, (PlaneLength) feature);
            default -> this;
        };
    }

    protected static Stream<BlockPosition> collectConeBlocks(Context context) {
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

        var minX = Math.min(x1, x2);
        var minZ = Math.min(z1, z2);

        var maxX = Math.max(x1, x2);
        var maxZ = Math.max(z1, z2);

        var radiusX = maxX - minX;
        var radiusZ = maxZ - minZ;

        var centerX = (maxX + minX) / 2;
        var centerZ = (maxZ + minZ) / 2;

        var radiusX1 = radiusX;
        var radiusZ1 = radiusZ;

        for (int y = y1; y <= y3; ++y) {
            if (y3 - y1 != 0) {
                radiusX1 = radiusX * (y3 - y) / (y3 - y1) / 2;
                radiusZ1 = radiusZ * (y3 - y) / (y3 - y1) / 2;
            }

            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (Circle.isPosInCircle(centerX, centerZ, radiusX1, radiusZ1, x, z, true)) {
                        set.add(new BlockPosition(x, y, z));
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
            case 2 -> Circle.collectCircleBlocks(context, circleStart, PlaneFilling.FILLED);
            case 3 -> Cone.collectConeBlocks(context);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.CONE;
    }
}
