package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Cylinder extends AbstractBlockStructure {

    public static Stream<BlockPosition> collectCylinderBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockInteraction().getBlockPosition().x();
        var y1 = context.firstBlockInteraction().getBlockPosition().y();
        var z1 = context.firstBlockInteraction().getBlockPosition().z();
        var x2 = context.secondBlockInteraction().getBlockPosition().x();
        var y2 = context.secondBlockInteraction().getBlockPosition().y();
        var z2 = context.secondBlockInteraction().getBlockPosition().z();
        var x3 = context.thirdBlockInteraction().getBlockPosition().x();
        var y3 = context.thirdBlockInteraction().getBlockPosition().y();
        var z3 = context.thirdBlockInteraction().getBlockPosition().z();

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

        var x1 = context.firstBlockInteraction().getBlockPosition().x();
        var y1 = context.firstBlockInteraction().getBlockPosition().y();
        var z1 = context.firstBlockInteraction().getBlockPosition().z();
        var x2 = context.secondBlockInteraction().getBlockPosition().x();
        var y2 = context.secondBlockInteraction().getBlockPosition().y();
        var z2 = context.secondBlockInteraction().getBlockPosition().z();

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
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return Circle.collectCircleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectThirdBlocks(Context context) {
        return collectCylinderBlocks(context);
    }

    @Override
    public int totalClicks(Context context) {
        return 3;
    }
}
