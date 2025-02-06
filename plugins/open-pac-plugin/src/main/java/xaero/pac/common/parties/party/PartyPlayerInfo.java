package xaero.pac.common.parties.party;

import xaero.pac.common.util.linked.ILinkedChainNode;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PartyPlayerInfo<PI extends PartyPlayerInfo<PI>> implements IPartyPlayerInfo, ILinkedChainNode<PI> {

	private final UUID UUID;
	private String username;//needs to be updated when a member changes their name
	private PI next;
	private PI previous;
	private boolean destroyed;

	public PartyPlayerInfo(UUID playerUUID) {
		super();
		this.UUID = playerUUID;
	}

	@Nonnull
	@Override
	public UUID getUUID() {
		return UUID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Nonnull
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return UUID.hashCode();
	}

	@Override
	public void setNext(PI element) {
		next = element;
	}

	@Override
	public void setPrevious(PI element) {
		previous = element;
	}

	@Override
	public PI getNext() {
		return next;
	}

	@Override
	public PI getPrevious() {
		return previous;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void onDestroyed() {
		destroyed = true;
	}
}
