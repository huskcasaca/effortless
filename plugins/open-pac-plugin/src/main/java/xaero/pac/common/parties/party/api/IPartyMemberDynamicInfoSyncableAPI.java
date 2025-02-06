package xaero.pac.common.parties.party.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

/**
 * API for the dynamic info (location) of a party member
 */
public interface IPartyMemberDynamicInfoSyncableAPI {

    /**
     * Gets the party/ally player's UUID.
     *
     * @return the party/ally player's UUID, not null
     */
    @Nonnull
    UUID getPlayerId();

    /**
     * Gets the X coordinate of the current position of this player in the world.
     * <p>
     * The player position is 0,0,0 on the server side before the first update.
     *
     * @return the X coordinate of the player's position
     */
    double getX();

    /**
     * Gets the Y coordinate of the current position of this player in the world.
     * <p>
     * The player position is 0,0,0 on the server side before the first update.
     *
     * @return the Y coordinate of the player's position
     */
    double getY();

    /**
     * Gets the Z coordinate of the current position of this player in the world.
     * <p>
     * The player position is 0,0,0 on the server side before the first update.
     *
     * @return the Z coordinate of the player's position
     */
    double getZ();

    /**
     * Gets the ID of the dimension that this player is currently in.
     *
     * @return the dimension ID for this player, null on server side before the first update
     */
    @Nullable
    ResourceLocation getDimension();

}
