package xaero.pac.common.parties.party.member.api;

import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.PartyMemberRank;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * API for info about a party member
 */
public interface IPartyMemberAPI extends IPartyPlayerInfoAPI {

	@Nonnull
	@Override
	public UUID getUUID();

	@Nonnull
	@Override
	public String getUsername();

	/**
	 * Gets the rank of this party member.
	 *
	 * @return the rank of this member, not null
	 */
	@Nonnull
	public PartyMemberRank getRank();

	/**
	 * Checks whether this party member is the owner of the party.
	 *
	 * @return true if this player is the owner of the party, otherwise false
	 */
	public boolean isOwner();

}
