package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Pyramid extends AbstractBlockStructure {

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return Floor.traceFloor(player, context);
    }

    @Override
    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        return traceLineY(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectSecondBlocks(Context context) {
        return Floor.collectFloorBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectThirdBlocks(Context context) {

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

    @Override
    public int totalClicks(Context context) {
        return 3;
    }
}
