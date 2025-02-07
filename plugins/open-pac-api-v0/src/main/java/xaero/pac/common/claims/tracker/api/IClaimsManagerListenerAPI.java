package xaero.pac.common.claims.tracker.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

/**
 * The interface to be implemented by all claims manager listeners
 * <p>
 * Register your listeners in {@link IClaimsManagerTrackerAPI}.
 */
public interface IClaimsManagerListenerAPI {

    /**
     * Called after a whole 512x512 region of claim states is updated.
     * <p>
     * Override this method and register the listener in {@link IClaimsManagerTrackerAPI} to handle region claim state
     * updates however you'd like.
     * <p>
     * This method is only called on the client side at the time of writing this.
     *
     * @param dimension the dimension of the region, not null
     * @param regionX   the X coordinate of the region
     * @param regionZ   the Z coordinate of the region
     */
    void onWholeRegionChange(@Nonnull ResourceLocation dimension, int regionX, int regionZ);

    /**
     * Called after the claim state of a chunk is updated.
     * <p>
     * Override this method and register the listener in {@link IClaimsManagerTrackerAPI} to handle chunk claim state
     * updates however you'd like.
     *
     * @param dimension the dimension of the chunk, not null
     * @param chunkX    the X coordinate of the chunk
     * @param chunkZ    the Z coordinate of the chunk
     * @param claim     the new claim state, null when the chunk is unclaimed
     */
    void onChunkChange(@Nonnull ResourceLocation dimension, int chunkX, int chunkZ, @Nullable IPlayerChunkClaimAPI claim);

    /**
     * Called after claim changes are made throughout a dimension.
     * <p>
     * For example, this is called on the client side when a huge number of claims change color.
     * <p>
     * Override this method and register the listener in {@link IClaimsManagerTrackerAPI} to handle dimension-wide
     * updates however you'd like.
     *
     * @param dimension the dimension ID, not null
     */
    void onDimensionChange(ResourceLocation dimension);

}
