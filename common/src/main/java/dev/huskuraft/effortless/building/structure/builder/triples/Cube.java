package dev.huskuraft.effortless.building.structure.builder.triples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Line;
import dev.huskuraft.effortless.building.structure.builder.doubles.Square;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

public class Cube extends TripleClickBuilder {

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

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        switch (context.cubeFilling()) {
            case CUBE_SKELETON -> Square.addHollowSquareBlocks(list, x1, x2, y1, y2, z1, z2);
            case CUBE_FULL -> Square.addFullSquareBlocks(list, x1, x2, y1, y2, z1, z2);
            case CUBE_HOLLOW -> Square.addFullSquareBlocks(list, x1, x2, y1, y2, z1, z2);
        }

        return list.stream();
    }

    public static Stream<BlockPosition> collectCubeBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x3 = context.thirdBlockPosition().x();
        var y3 = context.thirdBlockPosition().y();
        var z3 = context.thirdBlockPosition().z();

        switch (context.cubeFilling()) {
            case CUBE_FULL -> addFullCubeBlocks(list, x1, x3, y1, y3, z1, z3);
            case CUBE_HOLLOW -> addHollowCubeBlocks(list, x1, x3, y1, y3, z1, z3);
            case CUBE_SKELETON -> addSkeletonCubeBlocks(list, x1, x3, y1, y3, z1, z3);
        }

        return list.stream();
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return Square.traceSquare(player, context);
    }

    @Override
    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        if (y1 == y2) {
            if (x1 == x2 && z1 == z2) {
                return Line.traceLine(player, context);
            }
            if (x1 == x2) {
                return tracePlaneZ(player, context);
            }
            if (z1 == z2) {
                return tracePlaneX(player, context);
            }
            return traceLineY(player, context);
        } else {
            if (x1 == x2 && z1 == z2) {
                return tracePlaneY(player, context);
            }
            if (x1 == x2) {
                return traceLineX(player, context);
            }
            if (z1 == z2) {
                return traceLineZ(player, context);
            }
        }
        return null;
    }


    @Override
    protected Stream<BlockPosition> collectStartBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectInterBlocks(Context context) {
        return collectCubePlaneBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectCubeBlocks(context);
    }

}
