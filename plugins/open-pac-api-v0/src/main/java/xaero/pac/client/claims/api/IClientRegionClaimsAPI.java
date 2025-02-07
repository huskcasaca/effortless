package xaero.pac.client.claims.api;

import javax.annotation.Nullable;

import xaero.pac.common.claims.api.IRegionClaimsAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

/**
 * API for region claims on the client side
 */
public interface IClientRegionClaimsAPI extends IRegionClaimsAPI {

    @Nullable
    @Override
    IPlayerChunkClaimAPI get(int x, int z);

    @Override
    int getX();

    @Override
    int getZ();

}
