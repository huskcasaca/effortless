package xaero.pac.common.claims;

import xaero.pac.common.claims.api.IDimensionClaimsManagerAPI;

import java.util.stream.Stream;

public interface IDimensionClaimsManager
<
	WRC extends IRegionClaims
> extends IDimensionClaimsManagerAPI {

	//internal api

	Stream<WRC> getTypedRegionStream();

}
