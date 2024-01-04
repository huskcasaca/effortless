package dev.huskuraft.effortless.building.structure.builder.triples;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Circle;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.doubles.Wall;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Sphere extends TripleClickBuilder {

    public static Stream<BlockPosition> collectSphereBlocks(Context context) {
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

        float centerX = x1;
        float centerY = y1;
        float centerZ = z1;

        // adjust for CIRCLE_START

        float radiusX;
        float radiusY;
        float radiusZ;

        if (context.buildFeatures().contains(PlaneFacing.HORIZONTAL)) {
            if (context.circleStart() == CircleStart.CIRCLE_START_CORNER) {
                centerX = x1 + (x2 - x1) / 2f;
                centerY = y1 + (y3 - y1) / 2f;
                centerZ = z1 + (z2 - z1) / 2f;
            } else {
                x1 = (int) (centerX - (x2 - centerX));
                y1 = (int) (centerY - (y3 - centerY));
                z1 = (int) (centerZ - (z2 - centerZ));
            }
            radiusX = MathUtils.abs(x2 - centerX);
            radiusY = MathUtils.abs(y3 - centerY);
            radiusZ = MathUtils.abs(z2 - centerZ);
        } else {
            if (x1 == x2) {
                if (context.circleStart() == CircleStart.CIRCLE_START_CORNER) {
                    centerX = x1 + (x3 - x1) / 2f;
                    centerY = y1 + (y2 - y1) / 2f;
                    centerZ = z1 + (z2 - z1) / 2f;
                } else {
                    x1 = (int) (centerX - (x3 - centerX));
                    y1 = (int) (centerY - (y2 - centerY));
                    z1 = (int) (centerZ - (z2 - centerZ));
                }
                radiusX = MathUtils.abs(x3 - centerX);
                radiusY = MathUtils.abs(y2 - centerY);
                radiusZ = MathUtils.abs(z2 - centerZ);
            } else {
                if (context.circleStart() == CircleStart.CIRCLE_START_CORNER) {
                    centerX = x1 + (x2 - x1) / 2f;
                    centerY = y1 + (y2 - y1) / 2f;
                    centerZ = z1 + (z3 - z1) / 2f;
                } else {
                    x1 = (int) (centerX - (x2 - centerX));
                    y1 = (int) (centerY - (y2 - centerY));
                    z1 = (int) (centerZ - (z3 - centerZ));
                }
                radiusX = MathUtils.abs(x2 - centerX);
                radiusY = MathUtils.abs(y2 - centerY);
                radiusZ = MathUtils.abs(z3 - centerZ);
            }
        }
        if (context.planeFilling() == PlaneFilling.PLANE_FULL) {
            addSphereBlocks(list, x1, y1, z1, x3, y3, z3, centerX, centerY, centerZ, radiusX, radiusY, radiusZ);
        } else {
            addHollowSphereBlocks(list, x1, y1, z1, x3, y3, z3, centerX, centerY, centerZ, radiusX, radiusY, radiusZ);
        }

        return list.stream();
    }

    public static void addSphereBlocks(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2,
                                       float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {

            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {

                for (int m = y1; y1 < y2 ? m <= y2 : m >= y2; m += y1 < y2 ? 1 : -1) {

                    float distance = distance(l, m, n, centerX, centerY, centerZ);
                    float radius = calculateSpheroidRadius(centerX, centerY, centerZ, radiusX, radiusY, radiusZ, l, m, n);
                    if (distance < radius + 0.4f)
                        list.add(new BlockPosition(l, m, n));
                }
            }
        }
    }

    public static void addHollowSphereBlocks(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2,
                                             float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {

            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {

                for (int m = y1; y1 < y2 ? m <= y2 : m >= y2; m += y1 < y2 ? 1 : -1) {

                    float distance = distance(l, m, n, centerX, centerY, centerZ);
                    float radius = calculateSpheroidRadius(centerX, centerY, centerZ, radiusX, radiusY, radiusZ, l, m, n);
                    if (distance < radius + 0.4f && distance > radius - 0.6f)
                        list.add(new BlockPosition(l, m, n));
                }
            }
        }
    }

    private static float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
        return MathUtils.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    public static float calculateSpheroidRadius(float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, int x, int y, int z) {
        // twice ellipse radius
        float radiusXZ = Circle.calculateEllipseRadius(centerX, centerZ, radiusX, radiusZ, x, z);

        // TODO project x to plane

        return Circle.calculateEllipseRadius(centerX, centerY, radiusXZ, radiusY, x, y);
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