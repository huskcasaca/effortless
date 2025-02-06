package xaero.pac.common.server.parties.party.io.serialization.snapshot.member;

public class PartyInviteSnapshot {

	private final String UUID;
	private final String username;//needs to be updated when a member changes their name

	public PartyInviteSnapshot(String UUID, String username) {
		super();
		this.UUID = UUID;
		this.username = username;
	}

	public String getUUID() {
		return UUID;
	}

	public String getUsername() {
		return username;
	}

}
