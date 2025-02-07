package dev.ftb.mods.ftbchunks.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class FTBChunksAPI {

    public static ClaimedChunkManager getManager() {
        throw new RuntimeException("stub!");
    }

    public static boolean isManagerLoaded() {
        throw new RuntimeException("stub!");
    }

    public static ClaimResult claimAsPlayer(ServerPlayer player, ResourceKey<Level> dimension, ChunkPos pos, boolean checkOnly) {
        throw new RuntimeException("stub!");
    }

    public static void syncPlayer(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    public static boolean isChunkForceLoaded(ResourceKey<Level> dimension, int x, int z) {
        throw new RuntimeException("stub!");
    }

}
