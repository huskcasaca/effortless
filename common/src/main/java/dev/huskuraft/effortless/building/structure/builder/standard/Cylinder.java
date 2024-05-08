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

        switch (getShape(pos1, pos2)) {
            case PLANE_X -> {
                for (int x = x1; x1 < x3 ? x <= x3 : x >= x3; x += x1 < x3 ? 1 : -1) {
                    int x0 = x;
                    list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(x0, blockPosition.y(), blockPosition.z())).toList());
                }
            }
            case PLANE_Y -> {
                for (int y = y1; y1 < y3 ? y <= y3 : y >= y3; y += y1 < y3 ? 1 : -1) {
                    int y0 = y;
                    list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.x(), y0, blockPosition.z())).toList());
                }
            }
            case PLANE_Z -> {
                for (int z = z1; z1 < z3 ? z <= z3 : z >= z3; z += z1 < z3 ? 1 : -1) {
                    int z0 = z;
                    list.addAll(Circle.collectCircleBlocks(context).map(blockPosition -> new BlockPosition(blockPosition.x(), blockPosition.y(), z0)).toList());
                }
            }
        }

        return list.stream();
    }


    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context);
            case 2 -> switch (context.planeFacing()) {
                case BOTH -> Line.traceLineOnPlane(player, context.getPosition(0), context.getPosition(1));
                case VERTICAL -> Line.traceLineOnVerticalPlane(player, context.getPosition(0), context.getPosition(1));
                case HORIZONTAL -> Line.traceLineOnHorizontalPlane(player, context.getPosition(0), context.getPosition(1));
            };
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Circle.collectCircleBlocks(context);
            case 3 -> Cylinder.collectCylinderBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }
}
