package xaero.pac.common.server.claims.api;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.api.IDimensionClaimsManagerAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * API for a dimension claims manager on the server side
 */
public interface IServerDimensionClaimsManagerAPI
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
	public Stream<IServerRegionClaimsAPI> getRegionStream();

	@Nullable
	@Override
	public IServerRegionClaimsAPI getRegion(int x, int z);

}
