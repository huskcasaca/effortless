package xaero.pac.common.server.claims.api;

import javax.annotation.Nullable;

import xaero.pac.common.claims.api.IRegionClaimsAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

/**
 * API for region claims on the server side
 */
public interface IServerRegionClaimsAPI extends IRegionClaimsAPI {

    @Nullable
    @Override
    IPlayerChunkClaimAPI get(int x, int z);

    @Override
    int getX();

    @Override
    int getZ();

}
