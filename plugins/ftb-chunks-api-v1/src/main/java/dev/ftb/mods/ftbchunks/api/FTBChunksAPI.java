package dev.ftb.mods.ftbchunks.api;

import javax.annotation.Nonnull;

import dev.ftb.mods.ftbchunks.api.client.FTBChunksClientAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class FTBChunksAPI {

    public static final String MOD_ID = "ftbchunks";

    /**
     * Retrieve the public API instance.
     *
     * @return the API handler
     * @throws NullPointerException if called before initialised
     */
    @Nonnull
    public static FTBChunksAPI.API api() {
        throw new RuntimeException("stub!");
    }

    /**
     * Retrieve the public client API instance.
     *
     * @return the client API handler
     * @throws NullPointerException if called before initialised, or on the server
     */
    @Nonnull
    public static FTBChunksClientAPI clientApi() {
        throw new RuntimeException("stub!");
    }

    /**
     * Convenience method to get a resource location in the FTB Teams namespace
     *
     * @param path the resource location path component
     * @return a new resource location
     */
    public static ResourceLocation rl(String path) {
        throw new RuntimeException("stub!");
    }

    /**
     * Top-level FTB Chunks API. Retrieve an instance of this via {FTBChunksAPI#api()}
     */
    public interface API {
        /**
         * Check if the server-side manager is loaded. This will always be the case on the server once the FTB Teams
         * manager object has been created ({TeamManagerEvent#CREATED} Architectury event has been fired), which
         * happens when the server is starting up.
         *
         * @return true if the manager is available for use
         */
        boolean isManagerLoaded();

        /**
         * Get the server-side team manager instance.
         *
         * @return the team manager
         * @throws NullPointerException if the manager is not yet loaded, or this is called from the client
         */
        ClaimedChunkManager getManager();

        /**
         * Convenience method to claim the given chunk for the given player; if the player is in a party team, the
         * chunk will be claimed for the team.
         *
         * @param player    the player making the claim
         * @param dimension the dimension
         * @param pos       the chunk positionm
         * @param checkOnly true if only checking that the chunk may be claimed
         * @return the result of making (or simulating) the claim
         */
        ClaimResult claimAsPlayer(ServerPlayer player, ResourceKey<Level> dimension, ChunkPos pos, boolean checkOnly);

        /**
         * Check if the given chunk is currently force-loaded, i.e. remains loaded when no player is near.
         *
         * @param chunkPos the chunk position
         * @return true if the chunk is force-loaded
         */
        boolean isChunkForceLoaded(ChunkDimPos chunkPos);
    }
}
