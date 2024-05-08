package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Pyramid extends AbstractBlockStructure {

    protected static Stream<BlockPosition> collectPyramidBlocks(Context context) {

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
                    if (Math.abs(x - centerX) <= radiusX1 && Math.abs(z - centerZ) <= radiusZ1) {
                        list.add(new BlockPosition(x, y, z));
                    }
                }
            }
        }
        return list.stream();
    }


    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Floor.traceFloor(player, context);
            case 2 -> Line.traceLineY(player, context.getPosition(1));
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Floor.collectFloorBlocks(context);
            case 3 -> Pyramid.collectPyramidBlocks(context);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 3;
    }
}
