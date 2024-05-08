package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Circle extends AbstractBlockStructure {

    public static boolean isPosInCircle(float centerX, float centerY, float radiusX, float radiusY, int x, int y, boolean fill) {

        radiusX += 0.5f;
        radiusY += 0.5f;

        var xn = Math.abs(x - centerX) / radiusX;
        var yn = Math.abs(y - centerY) / radiusY;

        var xn1 = (Math.abs(x - centerX) + 1) / radiusX;
        var yn1 = (Math.abs(y - centerY) + 1) / radiusY;

        if (fill) {
            return lengthSq(xn, yn) < 1;
        } else {
            return lengthSq(xn, yn) < 1 && !(lengthSq(xn1, yn) <= 1 && lengthSq(xn, yn1) <= 1);
        }

    }

    public static void addFullCircleBlocksX(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerZ, float radiusY, float radiusZ, boolean fill) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
                var radius = isPosInCircle(centerY, centerZ, radiusY, radiusZ, y, z, fill);
                if (radius)
                    list.add(new BlockPosition(x1, y, z));
            }
        }
    }

    public static void addFullCircleBlocksY(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerZ, float radiusX, float radiusZ, boolean fill) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {
            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {
                var radius = isPosInCircle(centerX, centerZ, radiusX, radiusZ, l, n, fill);
                if (radius)
                    list.add(new BlockPosition(l, y1, n));
            }
        }
    }

    public static void addFullCircleBlocksZ(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerX, float radiusY, float radiusX, boolean fill) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
                var radius = isPosInCircle(centerY, centerX, radiusY, radiusX, y, x, fill);
                if (radius)
                    list.add(new BlockPosition(x, y, z1));
            }
        }
    }

    public static Stream<BlockPosition> collectCircleBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var isCenter = context.circleStart() == CircleStart.CIRCLE_START_CENTER;
        var isFill = context.planeFilling() == PlaneFilling.PLANE_FULL;

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        if (isCenter) {
            x1 = (x1 - x2) * 2 + x1;
            y1 = (y1 - y2) * 2 + y1;
            z1 = (z1 - z2) * 2 + z1;
        }

        var centerX = x1 + (x2 - x1) / 2f;
        var centerY = y1 + (y2 - y1) / 2f;
        var centerZ = z1 + (z2 - z1) / 2f;

        var radiusX = MathUtils.abs(x2 - centerX);
        var radiusY = MathUtils.abs(y2 - centerY);
        var radiusZ = MathUtils.abs(z2 - centerZ);

        switch (getShape(pos1, pos2)) {
            case PLANE_X -> addFullCircleBlocksX(list, x1, y1, z1, x2, y2, z2, centerY, centerZ, radiusY, radiusZ, isFill);
            case PLANE_Y -> addFullCircleBlocksY(list, x1, y1, z1, x2, y2, z2, centerX, centerZ, radiusX, radiusZ, isFill);
            case PLANE_Z -> addFullCircleBlocksZ(list, x1, y1, z1, x2, y2, z2, centerY, centerX, radiusY, radiusX, isFill);
        }

        return list.stream();
    }

    @Override
    public int volume(Context context) {
        return collect(context).toList().size();
    }

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Square.traceSquare(player, context);
            default -> null;
        };
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Circle.collectCircleBlocks(context);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 2;
    }
}
