package dev.huskcasaca.effortless.helper;

import dev.huskcasaca.effortless.buildconfig.ReachSettingsManager;
import net.minecraft.world.entity.player.Player;

public class ReachHelper {
    public static int getMaxReach(Player player) {
        return ReachSettingsManager.getReachSettings(player).maxReachDistance();
    }

    public static int getPlacementReach(Player player) {
        return getMaxReach(player) / 4;
    }

    public static int getMaxBlocksPlacedAtOnce(Player player) {
        return ReachSettingsManager.getReachSettings(player).maxBlockPlaceAtOnce();
    }

    public static int getMaxBlocksPerAxis(Player player) {
        return ReachSettingsManager.getReachSettings(player).maxBlockPlacePerAxis();
    }

    public static boolean canBreakFar(Player player) {
        return ReachSettingsManager.getReachSettings(player).canBreakFar();
    }
}
