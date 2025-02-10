package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record Sphere(
        CircleStart circleStart,
        PlaneFacing planeFacing, PlaneFilling planeFilling,
        PlaneLength planeLength
) implements BlockStructure {

    public Sphere() {
        this(CircleStart.CORNER, PlaneFacing.BOTH, PlaneFilling.FILLED, PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case CIRCLE_START -> new Sphere((CircleStart) feature, planeFacing, planeFilling, planeLength);
            case PLANE_FACING -> new Sphere(circleStart, (PlaneFacing) feature, planeFilling, planeLength);
            case PLANE_FILLING -> new Sphere(circleStart, planeFacing, (PlaneFilling) feature, planeLength);
            case PLANE_LENGTH -> new Sphere(circleStart, planeFacing, planeFilling, (PlaneLength) feature);
            default -> this;
        };
    }

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
            return BlockStructure.lengthSq(xn, yn, zn) < 1;
        } else {
            return BlockStructure.lengthSq(xn, yn, zn) < 1 && !(BlockStructure.lengthSq(xn1, yn, zn) <= 1 && BlockStructure.lengthSq(xn, yn1, zn) <= 1 && BlockStructure.lengthSq(xn, yn, zn1) <= 1);
        }

    }

    public static void addSphereBlocks(Set<BlockPosition> set, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, boolean fill) {
        for (int x11 = x1; x1 < x2 ? x11 <= x2 : x11 >= x2; x11 += x1 < x2 ? 1 : -1) {
            for (int z11 = z1; z1 < z2 ? z11 <= z2 : z11 >= z2; z11 += z1 < z2 ? 1 : -1) {
                for (int y11 = y1; y1 < y2 ? y11 <= y2 : y11 >= y2; y11 += y1 < y2 ? 1 : -1) {
                    if (isPosInSphere(centerX, centerY, centerZ, radiusX, radiusY, radiusZ, x11, y11, z11, fill))
                        set.add(new BlockPosition(x11, y11, z11));
                }
            }
        }
    }

    public static Stream<BlockPosition> collectSphereBlocks(Context context, CircleStart circleStart, PlaneFilling planeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var isCenter = circleStart == CircleStart.CENTER;
        var isFull = planeFilling == PlaneFilling.FILLED;

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

        addSphereBlocks(set, minX, minY, minZ, maxX, maxY, maxZ, centerX, centerY, centerZ, radiusX, radiusY, radiusZ, isFull);

        return set.stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> switch (planeFacing()) {
                case BOTH -> Square.traceSquare(player, context, planeFacing, planeLength);
                case VERTICAL -> Wall.traceWall(player, context, planeLength);
                case HORIZONTAL -> Floor.traceFloor(player, context, planeLength);
            };
            case 2 -> switch (planeFacing()) {
                case BOTH -> Line.traceLineOnPlane(player, context.getPosition(0), context.getPosition(1));
                case VERTICAL -> Line.traceLineOnVerticalPlane(player, context.getPosition(0), context.getPosition(1));
                case HORIZONTAL -> Line.traceLineOnHorizontalPlane(player, context.getPosition(0), context.getPosition(1));
            };
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Circle.collectCircleBlocks(context, circleStart, planeFilling);
            case 3 -> Sphere.collectSphereBlocks(context, circleStart, planeFilling);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.SPHERE;
    }
}
