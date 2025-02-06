package xaero.pac.common.claims.api;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for a dimension claims manager
 */
public interface IDimensionClaimsManagerAPI {

	/**
	 * Gets the dimension ID.
	 *
	 * @return the dimension ID, not null
	 */
	@Nonnull
	public ResourceLocation getDimension();

	/**
	 * Gets the number of 512x512 regions that contain claims in this dimension.
	 *
	 * @return the region count
	 */
	public int getCount();

	/**
	 * Gets region claims at a specified 512x512 region location in this dimension,
	 * or null if there are no claims stored for the specified location.
	 *
	 * @param x  the X coordinate of the 512x512 region
	 * @param z  the Z coordinate of the 512x512 region
	 * @return the read-only region claims manager, null if it doesn't exist
	 */
	@Nullable
	public IRegionClaimsAPI getRegion(int x, int z);

}
