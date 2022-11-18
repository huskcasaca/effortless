package dev.huskcasaca.effortless.buildreach;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.EffortlessDataProvider;
import dev.huskcasaca.effortless.helper.ReachHelper;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.ClientboundPlayerReachPacket;
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

    public static BuildReachSettings getReachSettings(Player player) {
        return ((EffortlessDataProvider) player).getReachSettings();
    }

    public static void setReachSettings(Player player, BuildReachSettings reachSettings) {
        if (player == null) {
            Effortless.log("Cannot set reach settings, player is null");
            return;
        }
        ((EffortlessDataProvider) player).setReachSettings(reachSettings);
    }

    public static String getSanitizeMessage(BuildReachSettings modeSettings, Player player) {
        int maxReach = ReachHelper.getMaxReach(player);
        String error = "";
        //TODO sanitize
        return error;
    }

    public static BuildReachSettings sanitize(BuildReachSettings reachSettings, Player player) {
//        maxBlockPlaceAtOnce = Math.toIntExact(Math.round(maxBlockPlaceAtOnce / 1000.0)) * 1000;
        return new BuildReachSettings(
                Math.max(MIN_MAX_REACH_DISTANCE, Math.min(reachSettings.maxReachDistance, MAX_MAX_REACH_DISTANCE)),
                Math.max(MIN_MAX_BLOCK_PLACE_PER_AXIS, Math.min(reachSettings.maxBlockPlacePerAxis, MAX_MAX_BLOCK_PLACE_PER_AXIS)),
                Math.max(MIN_MAX_BLOCK_PLACE_AT_ONCE, Math.min(reachSettings.maxBlockPlaceAtOnce, MAX_MAX_BLOCK_PLACE_AT_ONCE)),
                reachSettings.canBreakFar,
                reachSettings.enableUndo,
                Math.max(MIN_UNDO_STACK_SIZE, Math.min(reachSettings.undoStackSize, MAX_UNDO_STACK_SIZE))
        );
    }

    public static void handleNewPlayer(ServerPlayer player) {
        if (!player.level.isClientSide) {
            Packets.sendToClient(new ClientboundPlayerReachPacket(((EffortlessDataProvider) player).getReachSettings()), (ServerPlayer) player);
        }
    }

    public record BuildReachSettings(
            int maxReachDistance,
            int maxBlockPlacePerAxis,
            int maxBlockPlaceAtOnce,
            boolean canBreakFar,
            boolean enableUndo,
            int undoStackSize
    ) {
        public BuildReachSettings() {
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
