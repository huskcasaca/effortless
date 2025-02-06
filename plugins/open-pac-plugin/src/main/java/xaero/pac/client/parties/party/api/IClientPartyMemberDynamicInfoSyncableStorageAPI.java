package xaero.pac.client.parties.party.api;

import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * API for dynamic info (locations) of ally players
 */
public interface IClientPartyMemberDynamicInfoSyncableStorageAPI {

	/**
	 * Gets the dynamic info (e.g. position) for a specified player UUID.
	 *
	 * @param playerId  the player UUID, not null
	 * @return the dynamic info
	 */
	@Nullable
	public IPartyMemberDynamicInfoSyncableAPI getForPlayer(@Nonnull UUID playerId);

	/**
	 * Gets a stream of all player dynamic info (e.g. positions).
	 *
	 * @return a {@link Stream} of all dynamic info
	 */
	public Stream<IPartyMemberDynamicInfoSyncableAPI> getAllStream();

}
