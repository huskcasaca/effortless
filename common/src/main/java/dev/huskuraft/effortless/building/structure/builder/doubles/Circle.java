package dev.huskuraft.effortless.building.structure.builder.doubles;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.DoubleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.math.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Circle extends DoubleClickBuilder {

    public static float distance(float a1, float b1, float a2, float b2) {
        return MathUtils.sqrt((a2 - a1) * (a2 - a1) + (b2 - b1) * (b2 - b1));
    }

    public static float calculateEllipseRadius(float centerA, float centerB, float radiusA, float radiusB, int a, int b) {
        float theta = (float) MathUtils.atan2(b - centerB, a - centerA);
        float part1 = radiusA * radiusA * MathUtils.sin(theta) * MathUtils.sin(theta);
        float part2 = radiusB * radiusB * MathUtils.cos(theta) * MathUtils.cos(theta);
        return radiusA * radiusB / MathUtils.sqrt(part1 + part2);
    }

    public static void addFullCircleBlocksX(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerZ, float radiusY, float radiusZ) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
                float distance = distance(y, z, centerY, centerZ);
                float radius = calculateEllipseRadius(centerY, centerZ, radiusY, radiusZ, y, z);
                if (distance < radius + 0.4f)
                    list.add(new BlockPosition(x1, y, z));
            }
        }
    }

    public static void addFullCircleBlocksY(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerZ, float radiusX, float radiusZ) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {
            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {
                float distance = distance(l, n, centerX, centerZ);
                float radius = calculateEllipseRadius(centerX, centerZ, radiusX, radiusZ, l, n);
                if (distance < radius + 0.4f)
                    list.add(new BlockPosition(l, y1, n));
            }
        }
    }

    public static void addFullCircleBlocksZ(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerX, float radiusY, float radiusX) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
                float distance = distance(y, x, centerY, centerX);
                float radius = calculateEllipseRadius(centerY, centerX, radiusY, radiusX, y, x);
                if (distance < radius + 0.4f)
                    list.add(new BlockPosition(x, y, z1));
            }
        }
    }

    public static void addHollowCircleBlocksX(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerZ, float radiusY, float radiusZ) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int z = z1; z1 < z2 ? z <= z2 : z >= z2; z += z1 < z2 ? 1 : -1) {
                float distance = distance(y, z, centerY, centerZ);
                float radius = calculateEllipseRadius(centerY, centerZ, radiusY, radiusZ, y, z);
                if (distance < radius + 0.4f && distance > radius - 0.6f)
                    list.add(new BlockPosition(x1, y, z));
            }
        }
    }

    public static void addHollowCircleBlocksY(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerZ, float radiusX, float radiusZ) {
        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {
            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {

                float distance = distance(l, n, centerX, centerZ);
                float radius = calculateEllipseRadius(centerX, centerZ, radiusX, radiusZ, l, n);
                if (distance < radius + 0.4f && distance > radius - 0.6f)
                    list.add(new BlockPosition(l, y1, n));
            }
        }
    }

    public static void addHollowCircleBlocksZ(List<BlockPosition> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerY, float centerX, float radiusY, float radiusX) {
        for (int y = y1; y1 < y2 ? y <= y2 : y >= y2; y += y1 < y2 ? 1 : -1) {
            for (int x = x1; x1 < x2 ? x <= x2 : x >= x2; x += x1 < x2 ? 1 : -1) {
                float distance = distance(y, x, centerY, centerX);
                float radius = calculateEllipseRadius(centerY, centerX, radiusY, radiusX, y, x);
                if (distance < radius + 0.4f && distance > radius - 0.6f)
                    list.add(new BlockPosition(x, y, z1));
            }
        }
    }

    public static Stream<BlockPosition> collectCircleBlocks(Context context) {
        var list = new ArrayList<BlockPosition>();

        var x1 = context.firstBlockPosition().x();
        var y1 = context.firstBlockPosition().y();
        var z1 = context.firstBlockPosition().z();
        var x2 = context.secondBlockPosition().x();
        var y2 = context.secondBlockPosition().y();
        var z2 = context.secondBlockPosition().z();

        float centerX = x1;
        float centerY = y1;
        float centerZ = z1;

        switch (context.circleStart()) {
            case CIRCLE_START_CORNER -> {
                centerX = x1 + (x2 - x1) / 2f;
                centerY = y1 + (y2 - y1) / 2f;
                centerZ = z1 + (z2 - z1) / 2f;
            }
            case CIRCLE_START_CENTER -> {
                x1 = (int) (centerX - (x2 - centerX));
                y1 = (int) (centerY - (y2 - centerY));
                z1 = (int) (centerZ - (z2 - centerZ));
            }
        }
        float radiusX = MathUtils.abs(x2 - centerX);
        float radiusY = MathUtils.abs(y2 - centerY);
        float radiusZ = MathUtils.abs(z2 - centerZ);

        if (y1 == y2) {
            switch (context.planeFilling()) {
                case PLANE_FULL ->
                        addFullCircleBlocksY(list, x1, y1, z1, x2, y2, z2, centerX, centerZ, radiusX, radiusZ);
                case PLANE_HOLLOW ->
                        addHollowCircleBlocksY(list, x1, y1, z1, x2, y2, z2, centerX, centerZ, radiusX, radiusZ);
            }
        } else if (x1 == x2) {
            switch (context.planeFilling()) {
                case PLANE_FULL ->
                        addFullCircleBlocksX(list, x1, y1, z1, x2, y2, z2, centerY, centerZ, radiusY, radiusZ);
                case PLANE_HOLLOW ->
                        addHollowCircleBlocksX(list, x1, y1, z1, x2, y2, z2, centerY, centerZ, radiusY, radiusZ);
            }
        } else if (z1 == z2) {
            switch (context.planeFilling()) {
                case PLANE_FULL ->
                        addFullCircleBlocksZ(list, x1, y1, z1, x2, y2, z2, centerY, centerX, radiusY, radiusX);
                case PLANE_HOLLOW ->
                        addHollowCircleBlocksZ(list, x1, y1, z1, x2, y2, z2, centerY, centerZ, radiusY, radiusZ);
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
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectCircleBlocks(context);
    }

}
