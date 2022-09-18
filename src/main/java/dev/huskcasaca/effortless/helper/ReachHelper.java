package dev.huskcasaca.effortless.helper;

import dev.huskcasaca.effortless.BuildConfig;
import net.minecraft.world.entity.player.Player;

public class ReachHelper {
    public static int getMaxReach(Player player) {
        return BuildConfig.reach.maxReachCreative;
    }

    public static int getPlacementReach(Player player) {
        return getMaxReach(player) / 4;
    }

    public static int getMaxBlocksPlacedAtOnce(Player player) {
        return 64 * 64 * 64;
    }

    public static int getMaxBlocksPerAxis(Player player) {
        return 64 * 4;
    }

    public static boolean canBreakFar(Player player) {
        return true;
    }
}
