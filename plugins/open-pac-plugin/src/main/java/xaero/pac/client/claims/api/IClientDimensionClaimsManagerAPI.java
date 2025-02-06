package xaero.pac.client.claims.api;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.api.IDimensionClaimsManagerAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * API for a dimension claims manager on the client side
 */
public interface IClientDimensionClaimsManagerAPI
		extends IDimensionClaimsManagerAPI {

	@Nonnull
	@Override
	public ResourceLocation getDimension();

	@Override
	public int getCount();

	/**
	 * Gets a {@link Stream} of all 512x512 regions that contain claims in this dimension.
	 *
	 * @return a stream of all regions, not null
	 */
	@Nonnull
	public Stream<IClientRegionClaimsAPI> getRegionStream();

	@Nullable
	@Override
	public IClientRegionClaimsAPI getRegion(int x, int z);

}
