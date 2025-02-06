package xaero.pac.common.claims;

import xaero.pac.common.claims.player.PlayerChunkClaim;

public class PlayerChunkClaimHolder {

	private final PlayerChunkClaim claim;
	private short count;

	public PlayerChunkClaimHolder(PlayerChunkClaim claim) {
		super();
		this.claim = claim;
	}

	public int getCount() {
		return count & 0xFFFF;
	}

	public void increment() {
		count++;
	}

	public void decrement() {
		count--;
	}

	public PlayerChunkClaim getClaim() {
		return claim;
	}

}
