package xaero.pac.client.claims.api;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.client.claims.tracker.result.api.IClaimsManagerClaimResultTrackerAPI;
import xaero.pac.common.claims.api.IClaimsManagerAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.tracker.api.IClaimsManagerTrackerAPI;

/**
 * API for the claims manager on the client side
 */
public interface IClientClaimsManagerAPI
        extends IClaimsManagerAPI {

    @Override
    boolean hasPlayerInfo(@Nonnull UUID playerId);

    @Nonnull
    @Override
    IClientPlayerClaimInfoAPI getPlayerInfo(@Nonnull UUID playerId);

    /**
     * Gets a stream of all player claim info.
     *
     * @return a {@code Stream} of all player claim info
     */
    @Nonnull
    Stream<IClientPlayerClaimInfoAPI> getPlayerInfoStream();

    @Nullable
    @Override
    IPlayerChunkClaimAPI get(@Nonnull ResourceLocation dimension, int x, int z);

    @Nullable
    @Override
    IPlayerChunkClaimAPI get(@Nonnull ResourceLocation dimension, @Nonnull ChunkPos chunkPos);

    @Nullable
    @Override
    IPlayerChunkClaimAPI get(@Nonnull ResourceLocation dimension, @Nonnull BlockPos blockPos);

    @Nullable
    @Override
    IClientDimensionClaimsManagerAPI getDimension(@Nonnull ResourceLocation dimension);

    /**
     * Gets a stream of all read-only dimension claims managers.
     *
     * @return a {@code Stream} of all read-only dimension claims managers
     */
    @Nonnull
    Stream<IClientDimensionClaimsManagerAPI> getDimensionStream();

    @Nonnull
    @Override
    IClaimsManagerTrackerAPI getTracker();

    /**
     * Checks whether the initial server chunk claim sync is in progress.
     * <p>
     * Chunk claims start loading in the background when you join the server.
     *
     * @return true if the initial chunk claim sync is in progress, otherwise false
     */
    boolean isLoading();

    /**
     * Gets the local client player's chunk claim limit.
     *
     * @return the claim limit
     */
    int getClaimLimit();

    /**
     * Gets the local client player's chunk claim forceload limit.
     *
     * @return the forceload limit
     */
    int getForceloadLimit();

    /**
     * Gets the maximum distance for claiming/forceloading a chunk allowed by the server.
     *
     * @return the maximum claim distance
     */
    int getMaxClaimDistance();

    /**
     * Checks whether the local client player is in admin mode.
     *
     * @return true if the player is in admin mode, otherwise false
     */
    boolean isAdminMode();

    /**
     * Checks whether the local client player is in server claim mode.
     *
     * @return true if the player is in server claim mode, otherwise false
     */
    boolean isServerMode();

    /**
     * Gets the claim action result tracker that lets you register claim action result listeners.
     * <p>
     * The tracker notifies the registered listeners when the client receives a claim result from
     * the server. This happens after the player requests a claim action, e.g. to (un)claim some chunks.
     *
     * @return the claim action result tracker, not null
     */
    @Nonnull
    IClaimsManagerClaimResultTrackerAPI getClaimResultTracker();

    /**
     * Requests a new chunk claim by the local client player or by the server.
     * <p>
     * Only OPs can request claims by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param x        the X coordinate of the chunk
     * @param z        the Z coordinate of the chunk
     * @param byServer whether the claim should be made by the server
     */
    void requestClaim(int x, int z, boolean byServer);

    /**
     * Requests a chunk unclaim by the local client player or by the server.
     * <p>
     * Only OPs can request unclaims by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param x        the X coordinate of the chunk
     * @param z        the Z coordinate of the chunk
     * @param byServer whether the unclaim should be made by the server
     */
    void requestUnclaim(int x, int z, boolean byServer);

    /**
     * Requests a chunk (un)forceload by the local client player or by the server.
     * <p>
     * Only OPs can request (un)forceloads by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param x        the X coordinate of the chunk
     * @param z        the Z coordinate of the chunk
     * @param enable   true to forceload the chunk, false to unforceload
     * @param byServer whether the (un)forceload should be made by the server
     */
    void requestForceload(int x, int z, boolean enable, boolean byServer);

    /**
     * Requests new chunks claims over a specified area by the local client player or by the server.
     * <p>
     * Only OPs can request claims by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param left     the lowest X coordinate of the area
     * @param top      the lowest Z coordinate of the area
     * @param right    the highest X coordinate of the area
     * @param bottom   the highest Z coordinate of the area
     * @param byServer whether the claim should be made by the server
     */
    void requestAreaClaim(int left, int top, int right, int bottom, boolean byServer);

    /**
     * Requests chunk unclaims over a specified area by the local client player or by the server.
     * <p>
     * Only OPs can request unclaims by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param left     the lowest X coordinate of the area
     * @param top      the lowest Z coordinate of the area
     * @param right    the highest X coordinate of the area
     * @param bottom   the highest Z coordinate of the area
     * @param byServer whether the unclaim should be made by the server
     */
    void requestAreaUnclaim(int left, int top, int right, int bottom, boolean byServer);

    /**
     * Requests chunk (un)forceloads over a specified area by the local client player or by the server.
     * <p>
     * Only OPs can request (un)forceloads by the server.
     * <p>
     * Register a claim result listener with {@link IClaimsManagerClaimResultTrackerAPI} to receive the result of this request.
     *
     * @param left     the lowest X coordinate of the area
     * @param top      the lowest Z coordinate of the area
     * @param right    the highest X coordinate of the area
     * @param bottom   the highest Z coordinate of the area
     * @param enable   true to forceload the chunks, false to unforceload
     * @param byServer whether the (un)forceload should be made by the server
     */
    void requestAreaForceload(int left, int top, int right, int bottom, boolean enable, boolean byServer);

    /**
     * Gets a claim state of the same type (see {@link IPlayerChunkClaimAPI#isSameClaimType(IPlayerChunkClaimAPI)}) as
     * would be placed in the world when the player requests a new claim.
     * <p>
     * It's not the actual instance of the claim state that would be placed in the world but it's good enough
     * for comparisons against actual existing claim states.
     *
     * @return a claim state reflection of the same type as a new claim, null if unknown
     */
    @Nullable
    IPlayerChunkClaimAPI getPotentialClaimStateReflection();

    /**
     * Gets the index of the sub-config currently used for new claims.
     *
     * @return the current sub-config index
     */
    int getCurrentSubConfigIndex();

    /**
     * Gets the index of the sub-config currently used for new server claims.
     *
     * @return the current server sub-config index
     */
    int getCurrentServerSubConfigIndex();

    /**
     * Gets the string ID of the sub-config currently used for new claims.
     *
     * @return the current sub-config ID
     */
    String getCurrentSubConfigId();

    /**
     * Gets the string ID of the sub-config currently used for new server claims.
     *
     * @return the current server sub-config ID
     */
    String getCurrentServerSubConfigId();

}
