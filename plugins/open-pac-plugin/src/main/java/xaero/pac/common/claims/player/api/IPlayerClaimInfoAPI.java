package xaero.pac.common.claims.player.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

/**
 * API for claim info of a player
 */
public interface IPlayerClaimInfoAPI {

    /**
     * Gets the number of claims owned by the player.
     *
     * @return the claim count
     */
    int getClaimCount();

    /**
     * Gets the number of forceloadable claims owned by the player.
     *
     * @return the forceloadable claim count
     */
    int getForceloadCount();

    /**
     * Gets the UUID of the player.
     *
     * @return the UUID of the player, not null
     */
    @Nonnull
    UUID getPlayerId();

    /**
     * Gets the username of the player.
     * <p>
     * Can just be the UUID in string form until the player logs in and it's updated to the actual current username,
     * or before it is synced to the client.
     *
     * @return the username of the player, not null
     */
    @Nonnull
    String getPlayerUsername();

    /**
     * Gets the currently configured main custom name of the player's claims.
     * <p>
     * Can be empty if a custom name is not configured or null if the name hasn't been synced to the client yet.
     *
     * @return the main custom name of claimed chunks
     */
    @Nullable
    String getClaimsName();

    /**
     * Gets the currently configured main color of the player's claims.
     * <p>
     * Is 0 on the client side before it is synced from the server.
     *
     * @return the main claim color int
     */
    int getClaimsColor();

    /**
     * Gets the currently configured custom name of the player's sub-claim
     * with a specified index.
     * <p>
     * Returns null if no such sub-claim exists or the name is inherited from
     * the main config.
     *
     * @param subConfigIndex the index of the sub-config used by the sub-claim
     * @return the custom name of the sub-claim
     */
    @Nullable
    String getClaimsName(int subConfigIndex);

    /**
     * Gets the currently configured color of the player's sub-claim with
     * a specified index.
     * <p>
     * Returns null if no such sub-claim exists or the color is inherited from
     * the main config.
     *
     * @param subConfigIndex the index of the sub-config used by the sub-claim
     * @return the sub-claim color Integer, null if no such sub-claim exists
     */
    @Nullable
    Integer getClaimsColor(int subConfigIndex);

    /**
     * Gets claim info for a dimension with a specified ID.
     *
     * @param id the dimension ID, not null
     * @return the claim info of the dimension, null if no claims exist for the specified dimension ID
     */
    @Nullable
    IPlayerDimensionClaimsAPI getDimension(@Nonnull ResourceLocation id);


}
