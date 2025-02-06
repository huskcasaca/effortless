package xaero.pac.common.server.parties.party.io.serialization.snapshot;

import xaero.pac.common.server.io.serialization.human.gson.GsonSnapshot;
import xaero.pac.common.server.parties.party.io.serialization.snapshot.member.PartyInviteSnapshot;
import xaero.pac.common.server.parties.party.io.serialization.snapshot.member.PartyMemberSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PartySnapshot implements GsonSnapshot {

	private final PartyMemberSnapshot owner;
	private final List<PartyMemberSnapshot> members;
	private final List<PartyInviteSnapshot> invitedPlayers;
	private final List<String> allyParties;
	private long registeredActivity;

	public PartySnapshot(PartyMemberSnapshot owner) {
		super();
		this.owner = owner;
		this.members = new ArrayList<>();
		this.invitedPlayers = new ArrayList<>();
		this.allyParties = new ArrayList<>();
	}

	public void addMember(PartyMemberSnapshot member) {
		members.add(member);
	}

	public void addInvitedPlayer(PartyInviteSnapshot player) {
		invitedPlayers.add(player);
	}

	public void addAllyParty(String ally) {
		allyParties.add(ally);
	}

	public Iterable<PartyMemberSnapshot> getMembers() {
		return members;
	}

	public Iterable<PartyInviteSnapshot> getInvitedPlayers(){
		return invitedPlayers;
	}

	public Iterable<String> getAllyParties(){
		return allyParties;
	}

	public PartyMemberSnapshot getOwner() {
		return owner;
	}

	public void setRegisteredActivity(long registeredActivity) {
		this.registeredActivity = registeredActivity;
	}

	public long getRegisteredActivity() {
		return registeredActivity;
	}

}
