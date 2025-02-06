package xaero.pac.common.claims.player;

import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;

public interface IPlayerClaimPosList extends IPlayerClaimPosListAPI {

	//internal api

	@Override
	public IPlayerChunkClaim getClaimState();

}
