package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Cone extends AbstractBlockStructure {

    protected static Stream<BlockPosition> collectConeBlocks(Context context) {

        var list = new ArrayList<BlockPosition>();

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();

        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        var x3 = context.getPosition(2).x();
        var y3 = context.getPosition(2).y();
        var z3 = context.getPosition(2).z();

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
                        list.add(new BlockPosition(x, y, z));
                    }
                }
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
        return Circle.collectCircleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectThirdBlocks(Context context) {
        return Cone.collectConeBlocks(context);
    }

    @Override
    public int totalInteractions(Context context) {
        return 3;
    }
}
