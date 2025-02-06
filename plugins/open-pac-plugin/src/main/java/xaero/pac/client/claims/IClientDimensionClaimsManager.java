package xaero.pac.client.claims;

import xaero.pac.client.claims.api.IClientDimensionClaimsManagerAPI;
import xaero.pac.client.claims.api.IClientRegionClaimsAPI;
import xaero.pac.common.claims.IDimensionClaimsManager;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public interface IClientDimensionClaimsManager
<
	WRC extends IClientRegionClaims
> extends IDimensionClaimsManager<WRC>, IClientDimensionClaimsManagerAPI {

	//internal api

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IClientRegionClaimsAPI> getRegionStream() {
		return (Stream<IClientRegionClaimsAPI>)(Object)getTypedRegionStream();
	}

}
