package xaero.pac.common.claims;

import xaero.pac.common.claims.api.IRegionClaimsAPI;
import xaero.pac.common.claims.player.IPlayerChunkClaim;

import javax.annotation.Nullable;

public interface IRegionClaims extends IRegionClaimsAPI {

	//internal api

	@Nullable
	@Override
	public IPlayerChunkClaim get(int x, int z);

}
