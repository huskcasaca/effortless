package xaero.pac.common.claims;

import xaero.pac.common.claims.player.PlayerChunkClaim;

public class ClaimStateHolder {

	private final PlayerChunkClaim state;

	public ClaimStateHolder(PlayerChunkClaim state) {
		this.state = state;
	}

	public PlayerChunkClaim getState() {
		return state;
	}

}
