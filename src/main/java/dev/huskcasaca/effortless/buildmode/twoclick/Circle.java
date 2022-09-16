package dev.huskcasaca.effortless.buildmode.twoclick;

import dev.huskcasaca.effortless.buildmode.BuildAction;
import dev.huskcasaca.effortless.buildmode.BuildActionHandler;
import dev.huskcasaca.effortless.buildmode.TwoClickBuildable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Circle extends TwoClickBuildable {

    public static List<BlockPos> getCircleBlocks(Player player, int x1, int y1, int z1, int x2, int y2, int z2) {
        List<BlockPos> list = new ArrayList<>();

        float centerX = x1;
        float centerZ = z1;

        //Adjust for CIRCLE_START
        if (BuildActionHandler.getCircleStart() == BuildAction.CIRCLE_START_CORNER) {
            centerX = x1 + (x2 - x1) / 2f;
            centerZ = z1 + (z2 - z1) / 2f;
        } else {
            x1 = (int) (centerX - (x2 - centerX));
            z1 = (int) (centerZ - (z2 - centerZ));
        }

        float radiusX = Mth.abs(x2 - centerX);
        float radiusZ = Mth.abs(z2 - centerZ);

        if (BuildActionHandler.getFill() == BuildAction.FULL)
            addCircleBlocks(list, x1, y1, z1, x2, y2, z2, centerX, centerZ, radiusX, radiusZ);
        else
            addHollowCircleBlocks(list, x1, y1, z1, x2, y2, z2, centerX, centerZ, radiusX, radiusZ);

        return list;
    }

    public static void addCircleBlocks(List<BlockPos> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerZ, float radiusX, float radiusZ) {

        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {

            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {

                float distance = distance(l, n, centerX, centerZ);
                float radius = calculateEllipseRadius(centerX, centerZ, radiusX, radiusZ, l, n);
                if (distance < radius + 0.4f)
                    list.add(new BlockPos(l, y1, n));
            }
        }
    }

    public static void addHollowCircleBlocks(List<BlockPos> list, int x1, int y1, int z1, int x2, int y2, int z2, float centerX, float centerZ, float radiusX, float radiusZ) {

        for (int l = x1; x1 < x2 ? l <= x2 : l >= x2; l += x1 < x2 ? 1 : -1) {

            for (int n = z1; z1 < z2 ? n <= z2 : n >= z2; n += z1 < z2 ? 1 : -1) {

                float distance = distance(l, n, centerX, centerZ);
                float radius = calculateEllipseRadius(centerX, centerZ, radiusX, radiusZ, l, n);
                if (distance < radius + 0.4f && distance > radius - 0.6f)
                    list.add(new BlockPos(l, y1, n));
            }
        }
    }

    private static float distance(float x1, float z1, float x2, float z2) {
        return Mth.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));
    }

    public static float calculateEllipseRadius(float centerX, float centerZ, float radiusX, float radiusZ, int x, int z) {
        //https://math.stackexchange.com/questions/432902/how-to-get-the-radius-of-an-ellipse-at-a-specific-angle-by-knowing-its-semi-majo
        float theta = (float) Mth.atan2(z - centerZ, x - centerX);
        float part1 = radiusX * radiusX * Mth.sin(theta) * Mth.sin(theta);
        float part2 = radiusZ * radiusZ * Mth.cos(theta) * Mth.cos(theta);
        return radiusX * radiusZ / Mth.sqrt(part1 + part2);
    }

    @Override
    protected BlockPos findSecondPos(Player player, BlockPos firstPos, boolean skipRaytrace) {
        return Floor.findFloor(player, firstPos, skipRaytrace);
    }

    @Override
    protected List<BlockPos> getAllBlocks(Player player, int x1, int y1, int z1, int x2, int y2, int z2) {
        return getCircleBlocks(player, x1, y1, z1, x2, y2, z2);
    }
}
