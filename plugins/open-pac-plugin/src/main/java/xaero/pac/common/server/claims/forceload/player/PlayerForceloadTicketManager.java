package xaero.pac.common.server.claims.forceload.player;

import xaero.pac.common.server.claims.forceload.ClaimTicket;

import java.util.HashMap;
import java.util.Map;

public final class PlayerForceloadTicketManager {

	private final Map<ClaimTicket, ClaimTicket> tickets;
	private boolean failedToEnableSome;//because of the forceload limit

	private PlayerForceloadTicketManager(Map<ClaimTicket, ClaimTicket> tickets) {
		this.tickets = tickets;
	}

	public void add(ClaimTicket ticket){
		tickets.put(ticket, ticket);
	}

	public ClaimTicket remove(ClaimTicket ticket){
		return tickets.remove(ticket);
	}

	public Iterable<ClaimTicket> values(){
		return tickets.values();
	}

	public int getCount(){
		return tickets.size();
	}

	public boolean failedToEnableSome() {
		return failedToEnableSome;
	}

	public void setFailedToEnableSome(boolean failedToEnableSome) {
		this.failedToEnableSome = failedToEnableSome;
	}

	public static final class Builder {

		private Builder(){}

		public Builder setDefault(){
			return this;
		}

		public PlayerForceloadTicketManager build(){
			return new PlayerForceloadTicketManager(new HashMap<>());
		}

		public static Builder begin(){
			return new Builder().setDefault();
		}

	}

}
