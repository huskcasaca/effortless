package xaero.pac.common.parties.party.member.api;

import java.util.UUID;

import javax.annotation.Nonnull;

import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.PartyMemberRank;

/**
 * API for info about a party member
 */
public interface IPartyMemberAPI extends IPartyPlayerInfoAPI {

    @Nonnull
    @Override
    UUID getUUID();

    @Nonnull
    @Override
    String getUsername();

    /**
     * Gets the rank of this party member.
     *
     * @return the rank of this member, not null
     */
    @Nonnull
    PartyMemberRank getRank();

    /**
     * Checks whether this party member is the owner of the party.
     *
     * @return true if this player is the owner of the party, otherwise false
     */
    boolean isOwner();

}
