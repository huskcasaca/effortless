package xaero.pac.common.server.claims;

import xaero.pac.common.claims.IDimensionClaimsManager;
import xaero.pac.common.server.claims.api.IServerDimensionClaimsManagerAPI;
import xaero.pac.common.server.claims.api.IServerRegionClaimsAPI;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public interface IServerDimensionClaimsManager
<
	WRC extends IServerRegionClaims
> extends IDimensionClaimsManager<WRC>, IServerDimensionClaimsManagerAPI {

	//internal api

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IServerRegionClaimsAPI> getRegionStream() {
		return (Stream<IServerRegionClaimsAPI>)(Object)getTypedRegionStream();
	}

}
