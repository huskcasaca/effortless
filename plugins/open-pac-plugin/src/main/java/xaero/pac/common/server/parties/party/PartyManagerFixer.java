package xaero.pac.common.server.parties.party;

import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.parties.party.member.PartyMember;

import java.util.*;

public final class PartyManagerFixer {

	public void fix(PartyManager partyManager) {
		OpenPartiesAndClaims.LOGGER.info("Fixing party inconsistencies...");
		List<ServerParty> partiesToRemove = new ArrayList<>();
		partyManager.getTypedAllStream().forEach(party -> {
			List<UUID> membersToRemove = new ArrayList<>();
			party.getTypedMemberInfoStream().forEach(p -> fixPlayer(party, partyManager, p, partiesToRemove, membersToRemove));
			membersToRemove.forEach(party::removeMember);//doesn't mess up client sync because nobody is online yet
		});
		partiesToRemove.forEach(partyManager::removeTypedParty);
	}

	private void fixPlayer(ServerParty fixingParty, PartyManager partyManager, PartyMember player, List<ServerParty> partiesToRemove, List<UUID> membersToRemove) {
		UUID playerId = player.getUUID();
		ServerParty correctParty = partyManager.getPartyByMember(playerId);
		if(correctParty == fixingParty)
			return;
		if(fixingParty.getOwner().getUUID().equals(playerId)) {
			partiesToRemove.add(fixingParty);
			return;
		}
		membersToRemove.add(player.getUUID());
	}

}
