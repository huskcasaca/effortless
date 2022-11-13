package dev.huskcasaca.effortless.buildconfig;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.EffortlessDataProvider;
import dev.huskcasaca.effortless.helper.ReachHelper;
import dev.huskcasaca.effortless.network.PacketHandler;
import dev.huskcasaca.effortless.network.ReachSettingsMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ReachSettingsManager {

    public static int MIN_MAX_REACH_DISTANCE = 0;
    public static int MAX_MAX_REACH_DISTANCE = 512;

    public static int MIN_MAX_BLOCK_PLACE_PER_AXIS = 0;
    public static int MAX_MAX_BLOCK_PLACE_PER_AXIS = 512;

    public static int MIN_MAX_BLOCK_PLACE_AT_ONCE = 0;
    public static int MAX_MAX_BLOCK_PLACE_AT_ONCE = 100_000;

    public static int MIN_UNDO_STACK_SIZE = 0;
    public static int MAX_UNDO_STACK_SIZE = 100;

    public static ReachSettings getReachSettings(Player player) {
        return ((EffortlessDataProvider) player).getReachSettings();
    }

    public static void setReachSettings(Player player, ReachSettings reachSettings) {
        if (player == null) {
            Effortless.log("Cannot set reach settings, player is null");
            return;
        }
        ((EffortlessDataProvider) player).setReachSettings(reachSettings);
    }

    public static String getSanitizeMessage(ReachSettings modeSettings, Player player) {
        int maxReach = ReachHelper.getMaxReach(player);
        String error = "";
        //TODO sanitize
        return error;
    }

    public static ReachSettings sanitize(ReachSettings reachSettings, Player player) {
//        maxBlockPlaceAtOnce = Math.toIntExact(Math.round(maxBlockPlaceAtOnce / 1000.0)) * 1000;
        return new ReachSettings(
                Math.max(MIN_MAX_REACH_DISTANCE, Math.min(reachSettings.maxReachDistance, MAX_MAX_REACH_DISTANCE)),
                Math.max(MIN_MAX_BLOCK_PLACE_PER_AXIS, Math.min(reachSettings.maxBlockPlacePerAxis, MAX_MAX_BLOCK_PLACE_PER_AXIS)),
                Math.max(MIN_MAX_BLOCK_PLACE_AT_ONCE, Math.min(reachSettings.maxBlockPlaceAtOnce, MAX_MAX_BLOCK_PLACE_AT_ONCE)),
                reachSettings.canBreakFar,
                reachSettings.enableUndo,
                Math.max(MIN_UNDO_STACK_SIZE, Math.min(reachSettings.undoStackSize, MAX_UNDO_STACK_SIZE))
        );
    }

    public static void handleNewPlayer(Player player) {
        if (!player.level.isClientSide) {
            ReachSettingsMessage msg = new ReachSettingsMessage(((EffortlessDataProvider) player).getReachSettings());
            PacketHandler.sendToClient(msg, (ServerPlayer) player);
        }
    }

    public record ReachSettings(
            int maxReachDistance,
            int maxBlockPlacePerAxis,
            int maxBlockPlaceAtOnce,
            boolean canBreakFar,
            boolean enableUndo,
            int undoStackSize
    ) {
        public ReachSettings() {
            this(
                    512,
                    128,
                    10_000,
                    true,
                    false,
                    10
            );
        }

    }
}
