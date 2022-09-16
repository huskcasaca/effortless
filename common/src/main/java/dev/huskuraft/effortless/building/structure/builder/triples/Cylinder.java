package dev.huskuraft.effortless.building.structure.builder.triples;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Circle;
import dev.huskuraft.effortless.building.structure.builder.doubles.Square;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Cylinder extends TripleClickBuilder {

    public static Stream<BlockPosition> collectCylinderBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.secondBlockPosition().getX();
        var y2 = context.secondBlockPosition().getY();
        var z2 = context.secondBlockPosition().getZ();
        var x3 = context.thirdBlockPosition().getX();
        var y3 = context.thirdBlockPosition().getY();
        var z3 = context.thirdBlockPosition().getZ();

        if (y1 == y2) {
            for (int y = y1; y1 < y3 ? y <= y3 : y >= y3; y += y1 < y3 ? 1 : -1) {
                int y0 = y;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.getX(), y0, blockPosition.getZ())).toList());
            }
        } else if (x1 == x2) {
            for (int x = x1; x1 < x3 ? x <= x3 : x >= x3; x += x1 < x3 ? 1 : -1) {
                int x0 = x;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(x0, blockPosition.getY(), blockPosition.getZ())).toList());
            }
        } else if (z1 == z2) {
            for (int z = z1; z1 < z3 ? z <= z3 : z >= z3; z += z1 < z3 ? 1 : -1) {
                int z0 = z;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.getX(), blockPosition.getY(), z0)).toList());
            }
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

        var x1 = context.firstBlockPosition().getX();
        var y1 = context.firstBlockPosition().getY();
        var z1 = context.firstBlockPosition().getZ();
        var x2 = context.secondBlockPosition().getX();
        var y2 = context.secondBlockPosition().getY();
        var z2 = context.secondBlockPosition().getZ();

        if (y1 == y2) {
            return traceLineY(player, context);
        } else if (x1 == x2) {
            return traceLineX(player, context);
        } else if (z1 == z2) {
            return traceLineZ(player, context);
        }
        return null;
    }

    @Override
    protected Stream<BlockPosition> collectStartBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectInterBlocks(Context context) {
        return Circle.collectCircleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectCylinderBlocks(context);
    }
}