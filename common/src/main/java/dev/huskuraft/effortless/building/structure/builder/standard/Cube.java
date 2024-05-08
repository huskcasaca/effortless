package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Cube extends AbstractBlockStructure {

    public static void addFullCubeBlocks(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z1, int z2) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {
            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {
                for (int m = y1; y1 < y2 ? m <= y2 : m >= y2; m += y1 < y2 ? 1 : -1) {
                    list.add(new BlockPosition(l, m, n));
                }
            }
        }
    }

    public static void addHollowCubeBlocks(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z1, int z2) {
        Square.addFullSquareBlocksX(list, x1, y1, y2, z1, z2);
        Square.addFullSquareBlocksX(list, x2, y1, y2, z1, z2);

        Square.addFullSquareBlocksZ(list, x1, x2, y1, y2, z1);
        Square.addFullSquareBlocksZ(list, x1, x2, y1, y2, z2);

        Square.addFullSquareBlocksY(list, x1, x2, y1, z1, z2);
        Square.addFullSquareBlocksY(list, x1, x2, y2, z1, z2);
    }

    public static void addSkeletonCubeBlocks(List<BlockPosition> list, int x1, int x2, int y1, int y2, int z1, int z2) {
        Line.addXLineBlocks(list, x1, x2, y1, z1);
        Line.addXLineBlocks(list, x1, x2, y1, z2);
        Line.addXLineBlocks(list, x1, x2, y2, z1);
        Line.addXLineBlocks(list, x1, x2, y2, z2);

        Line.addYLineBlocks(list, y1, y2, x1, z1);
        Line.addYLineBlocks(list, y1, y2, x1, z2);
        Line.addYLineBlocks(list, y1, y2, x2, z1);
        Line.addYLineBlocks(list, y1, y2, x2, z2);

        Line.addZLineBlocks(list, z1, z2, x1, y1);
        Line.addZLineBlocks(list, z1, z2, x1, y2);
        Line.addZLineBlocks(list, z1, z2, x2, y1);
        Line.addZLineBlocks(list, z1, z2, x2, y2);
    }


    public static Stream<BlockPosition> collectCubePlaneBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        switch (context.cubeFilling()) {
            case CUBE_SKELETON -> Square.addHollowSquareBlocks(list, x1, x2, y1, y2, z1, z2);
            case CUBE_FULL -> Square.addFullSquareBlocks(list, x1, x2, y1, y2, z1, z2);
            case CUBE_HOLLOW -> Square.addFullSquareBlocks(list, x1, x2, y1, y2, z1, z2);
        }

        return list.stream();
    }

    public static Stream<BlockPosition> collectCubeBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();
        var x3 = context.getPosition(2).x();
        var y3 = context.getPosition(2).y();
        var z3 = context.getPosition(2).z();

        switch (context.cubeFilling()) {
            case CUBE_FULL -> addFullCubeBlocks(list, x1, x3, y1, y3, z1, z3);
            case CUBE_HOLLOW -> addHollowCubeBlocks(list, x1, x3, y1, y3, z1, z3);
            case CUBE_SKELETON -> addSkeletonCubeBlocks(list, x1, x3, y1, y3, z1, z3);
        }

        return list.stream();
    }

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context);
            case 2 -> Line.traceLineOnPlane(player, context);
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Cube.collectCubePlaneBlocks(context);
            case 3 -> Cube.collectCubeBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }
}
