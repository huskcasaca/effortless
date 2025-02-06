package xaero.pac.common.server.claims.api;

import xaero.pac.common.claims.api.IRegionClaimsAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

import javax.annotation.Nullable;

/**
 * API for region claims on the server side
 */
public interface IServerRegionClaimsAPI extends IRegionClaimsAPI {

	@Nullable
	@Override
	public IPlayerChunkClaimAPI get(int x, int z);

	@Override
	public int getX();

	@Override
	public int getZ();

}
