package dev.huskuraft.effortless.building.structure.builder.triples;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Circle;
import dev.huskuraft.effortless.building.structure.builder.doubles.Square;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Cylinder extends TripleClickBuilder {

    public static Stream<BlockPosition> collectCylinderBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();
        var x3 = context.thirdBlockPosition().x();
        var y3 = context.thirdBlockPosition().y();
        var z3 = context.thirdBlockPosition().z();

        if (y1 == y2) {
            for (int y = y1; y1 < y3 ? y <= y3 : y >= y3; y += y1 < y3 ? 1 : -1) {
                int y0 = y;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.x(), y0, blockPosition.z())).toList());
            }
        } else if (x1 == x2) {
            for (int x = x1; x1 < x3 ? x <= x3 : x >= x3; x += x1 < x3 ? 1 : -1) {
                int x0 = x;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(x0, blockPosition.y(), blockPosition.z())).toList());
            }
        } else if (z1 == z2) {
            for (int z = z1; z1 < z3 ? z <= z3 : z >= z3; z += z1 < z3 ? 1 : -1) {
                int z0 = z;
                list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.x(), blockPosition.y(), z0)).toList());
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

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

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