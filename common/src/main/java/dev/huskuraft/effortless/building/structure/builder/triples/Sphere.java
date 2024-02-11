package dev.huskuraft.effortless.building.structure.builder.triples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Circle;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.doubles.Wall;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

public class Sphere extends TripleClickBuilder {

    private static boolean isPosInSphere(float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, int x, int y, int z, boolean fill) {

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

    private static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
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

        var isHorizontal = context.buildFeatures().contains(PlaneFacing.HORIZONTAL);
        var isCenter = context.circleStart() == CircleStart.CIRCLE_START_CENTER;
        var isFull = context.planeFilling() == PlaneFilling.PLANE_FULL;

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();

        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        var x3 = context.thirdBlockPosition().x();
        var y3 = context.thirdBlockPosition().y();
        var z3 = context.thirdBlockPosition().z();

        if (isCenter) {
            x1 = (x2 - x1) / 2 + x1;
            y1 = (y2 - y1) / 2 + y1;
            z1 = (z2 - z1) / 2 + z1;
        }

        var minX = isHorizontal ? Math.min(x1, x2) : Math.min(x1, Math.min(x2, x3));
        var minY = isHorizontal ? Math.min(y3, Math.min(y1, y2)) : Math.min(y1, y2);
        var minZ = isHorizontal ? Math.min(z1, z2) : Math.min(z1, Math.min(z2, z3));

        var maxX = isHorizontal ? Math.max(x1, x2) : Math.max(x1, Math.max(x2, x3));
        var maxY = isHorizontal ? Math.max(y3, Math.max(y1, y2)) : Math.max(y1, y2);
        var maxZ = isHorizontal ? Math.max(z1, z2) : Math.max(z1, Math.max(z2, z3));

        var centerX = (minX + maxX) / 2f;
        var centerY = (minY + maxY) / 2f;
        var centerZ = (minZ + maxZ) / 2f;

        var radiusX = (maxX - minX) / 2f;
        var radiusY = (maxY - minY) / 2f;
        var radiusZ = (maxZ - minZ) / 2f;

        addSphereBlocks(list, minX, minY, minZ, maxX, maxY, maxZ, centerX, centerY, centerZ, radiusX, radiusY, radiusZ, isFull);

        return list.stream();
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        if (context.planeFacing() == PlaneFacing.HORIZONTAL) {
            return Floor.traceFloor(player, context);
        } else {
            return Wall.traceWall(player, context);
        }
    }

    @Override
    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        if (context.planeFacing() == PlaneFacing.HORIZONTAL) {
            return traceLineY(player, context);
        } else {
            if (context.firstBlockPosition().x() == context.secondBlockPosition().x()) {
                return tracePlaneZ(player, context);
            } else {
                return tracePlaneX(player, context);
            }
        }
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
        return collectSphereBlocks(context);
    }
}
