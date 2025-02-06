package xaero.pac.common.claims.player;

import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public interface IPlayerDimensionClaims<L extends IPlayerClaimPosList> extends IPlayerDimensionClaimsAPI {

	//internal api

	@Nonnull
	public Stream<L> getTypedStream();

	@Override
	@Nonnull
	@SuppressWarnings("unchecked")
	default Stream<IPlayerClaimPosListAPI> getStream(){
		return (Stream<IPlayerClaimPosListAPI>)(Object)getTypedStream();
	}

}
