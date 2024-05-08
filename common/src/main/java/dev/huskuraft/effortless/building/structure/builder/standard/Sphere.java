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

public class Sphere extends AbstractBlockStructure {

    public static boolean isPosInSphere(float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, int x, int y, int z, boolean fill) {

        radiusX += 0.5f;
        radiusY += 0.5f;
        radiusZ += 0.5f;

        var xn = Math.abs(x - centerX) / radiusX;
        var yn = Math.abs(y - centerY) / radiusY;
        var zn = Math.abs(z - centerZ) / radiusZ;

        var xn1 = (Math.abs(x - centerX) + 1) / radiusX;
        var yn1 = (Math.abs(y - centerY) + 1) / radiusY;
        var zn1 = (Math.abs(z - centerZ) + 1) / radiusZ;

        if (fill) {
            return lengthSq(xn, yn, zn) < 1;
        } else {
            return lengthSq(xn, yn, zn) < 1 && !(lengthSq(xn1, yn, zn) <= 1 && lengthSq(xn, yn1, zn) <= 1 && lengthSq(xn, yn, zn1) <= 1);
        }

    }

    public static void addSphereBlocks(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2,
                                       float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, boolean fill) {

        for (int x11 = x1; x1 < x2 ? x11 <= x2 : x11 >= x2; x11 += x1 < x2 ? 1 : -1) {
            for (int z11 = z1; z1 < z2 ? z11 <= z2 : z11 >= z2; z11 += z1 < z2 ? 1 : -1) {
                for (int y11 = y1; y1 < y2 ? y11 <= y2 : y11 >= y2; y11 += y1 < y2 ? 1 : -1) {
                    if (isPosInSphere(centerX, centerY, centerZ, radiusX, radiusY, radiusZ, x11, y11, z11, fill))
                        list.add(new BlockPosition(x11, y11, z11));
                }
            }
        }
    }

    public static Stream<BlockPosition> collectSphereBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var isCenter = context.circleStart() == CircleStart.CIRCLE_START_CENTER;
        var isFull = context.planeFilling() == PlaneFilling.PLANE_FULL;

        var x1 = context.getPosition(0).x();
        var y1 = context.getPosition(0).y();
        var z1 = context.getPosition(0).z();

        var x2 = context.getPosition(1).x();
        var y2 = context.getPosition(1).y();
        var z2 = context.getPosition(1).z();

        var x3 = context.getPosition(2).x();
        var y3 = context.getPosition(2).y();
        var z3 = context.getPosition(2).z();

        if (isCenter) {
            // FIXME: 8/5/24 3
            x1 = (x1 - x2) * 2 + x1;
            y1 = (y1 - y2) * 2 + y1;
            z1 = (z1 - z2) * 2 + z1;
        }

        var minX = MathUtils.min(x1, MathUtils.min(x2, x3));
        var minY = MathUtils.min(y1, MathUtils.min(y2, y3));
        var minZ = MathUtils.min(z1, MathUtils.min(z2, z3));

        var maxX = MathUtils.max(x1, MathUtils.max(x2, x3));
        var maxY = MathUtils.max(y1, MathUtils.max(y2, y3));
        var maxZ = MathUtils.max(z1, MathUtils.max(z2, z3));

        var centerX = (minX + maxX) / 2f;
        var centerY = (minY + maxY) / 2f;
        var centerZ = (minZ + maxZ) / 2f;

        var radiusX = (maxX - minX) / 2f;
        var radiusY = (maxY - minY) / 2f;
        var radiusZ = (maxZ - minZ) / 2f;

        addSphereBlocks(list, minX, minY, minZ, maxX, maxY, maxZ, centerX, centerY, centerZ, radiusX, radiusY, radiusZ, isFull);

        return list.stream();
    }

    protected BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> switch (context.planeFacing()) {
                case BOTH -> Square.traceSquare(player, context);
                case VERTICAL -> Wall.traceWall(player, context);
                case HORIZONTAL -> Floor.traceFloor(player, context);
            };
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
            case 3 -> Sphere.collectSphereBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }
}
