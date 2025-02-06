package xaero.pac.common.parties.party.ally;

import xaero.pac.common.util.linked.ILinkedChainNode;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PartyAlly implements IPartyAlly, ILinkedChainNode<PartyAlly> {

	private final UUID partyId;
	private PartyAlly previous;
	private PartyAlly next;
	private boolean destroyed;

	public PartyAlly(UUID partyId) {
		this.partyId = partyId;
	}

	@Override
	@Nonnull
	public UUID getPartyId() {
		return partyId;
	}

	@Override
	public void setNext(PartyAlly element) {
		this.next = element;
	}

	@Override
	public void setPrevious(PartyAlly element) {
		this.previous = element;
	}

	@Override
	public PartyAlly getNext() {
		return next;
	}

	@Override
	public PartyAlly getPrevious() {
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
