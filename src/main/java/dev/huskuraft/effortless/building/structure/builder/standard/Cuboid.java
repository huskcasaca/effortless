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
import dev.huskuraft.effortless.building.structure.CubeFilling;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Cuboid(
        CubeFilling cubeFilling,
        PlaneFacing planeFacing,
        PlaneLength planeLength
) implements BlockStructure {

    public Cuboid() {
        this(CubeFilling.FILLED, PlaneFacing.BOTH, PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case CUBE_FILLING -> new Cuboid((CubeFilling) feature, planeFacing, planeLength);
            case PLANE_FACING -> new Cuboid(cubeFilling, (PlaneFacing) feature, planeLength);
            case PLANE_LENGTH -> new Cuboid(cubeFilling, planeFacing, (PlaneLength) feature);
            default -> this;
        };
    }

    public static void addFullCubeBlocks(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z1, int z2) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {
            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {
                for (int m = y1; y1 < y2 ? m <= y2 : m >= y2; m += y1 < y2 ? 1 : -1) {
                    set.add(new BlockPosition(l, m, n));
                }
            }
        }
    }

    public static void addHollowCubeBlocks(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z1, int z2) {
        Square.addFullSquareBlocksX(set, x1, y1, y2, z1, z2);
        Square.addFullSquareBlocksX(set, x2, y1, y2, z1, z2);

        Square.addFullSquareBlocksZ(set, x1, x2, y1, y2, z1);
        Square.addFullSquareBlocksZ(set, x1, x2, y1, y2, z2);

        Square.addFullSquareBlocksY(set, x1, x2, y1, z1, z2);
        Square.addFullSquareBlocksY(set, x1, x2, y2, z1, z2);
    }

    public static void addSkeletonCubeBlocks(Set<BlockPosition> set, int x1, int x2, int y1, int y2, int z1, int z2) {
        Line.addXLineBlocks(set, x1, x2, y1, z1);
        Line.addXLineBlocks(set, x1, x2, y1, z2);
        Line.addXLineBlocks(set, x1, x2, y2, z1);
        Line.addXLineBlocks(set, x1, x2, y2, z2);

        Line.addYLineBlocks(set, y1, y2, x1, z1);
        Line.addYLineBlocks(set, y1, y2, x1, z2);
        Line.addYLineBlocks(set, y1, y2, x2, z1);
        Line.addYLineBlocks(set, y1, y2, x2, z2);

        Line.addZLineBlocks(set, z1, z2, x1, y1);
        Line.addZLineBlocks(set, z1, z2, x1, y2);
        Line.addZLineBlocks(set, z1, z2, x2, y1);
        Line.addZLineBlocks(set, z1, z2, x2, y2);
    }


    public static Stream<BlockPosition> collectCubePlaneBlocks(Context context, CubeFilling cubeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();

        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        switch (cubeFilling) {
            case SKELETON -> Square.addHollowSquareBlocks(set, x1, x2, y1, y2, z1, z2);
            case FILLED -> Square.addFullSquareBlocks(set, x1, x2, y1, y2, z1, z2);
            case HOLLOW -> Square.addFullSquareBlocks(set, x1, x2, y1, y2, z1, z2);
        }

        return set.stream();
    }

    public static Stream<BlockPosition> collectCubeBlocks(Context context, CubeFilling cubeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos3 = context.getPosition(2);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x3 = pos3.x();
        var y3 = pos3.y();
        var z3 = pos3.z();

        switch (cubeFilling) {
            case FILLED -> addFullCubeBlocks(set, x1, x3, y1, y3, z1, z3);
            case HOLLOW -> addHollowCubeBlocks(set, x1, x3, y1, y3, z1, z3);
            case SKELETON -> addSkeletonCubeBlocks(set, x1, x3, y1, y3, z1, z3);
        }

        return set.stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context, planeFacing, planeLength);
            case 2 -> Line.traceLineOnPlane(player, context, planeFacing);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Cuboid.collectCubePlaneBlocks(context, cubeFilling);
            case 3 -> Cuboid.collectCubeBlocks(context, cubeFilling);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.CUBOID;
    }
}
