package xaero.pac.client.parties.party.api;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

/**
 * API for dynamic info (locations) of ally players
 */
public interface IClientPartyMemberDynamicInfoSyncableStorageAPI {

    /**
     * Gets the dynamic info (e.g. position) for a specified player UUID.
     *
     * @param playerId the player UUID, not null
     * @return the dynamic info
     */
    @Nullable
    IPartyMemberDynamicInfoSyncableAPI getForPlayer(@Nonnull UUID playerId);

    /**
     * Gets a stream of all player dynamic info (e.g. positions).
     *
     * @return a {@link Stream} of all dynamic info
     */
    Stream<IPartyMemberDynamicInfoSyncableAPI> getAllStream();

}
