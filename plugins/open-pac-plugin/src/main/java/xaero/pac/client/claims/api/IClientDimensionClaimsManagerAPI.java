package xaero.pac.client.claims.api;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.api.IDimensionClaimsManagerAPI;

/**
 * API for a dimension claims manager on the client side
 */
public interface IClientDimensionClaimsManagerAPI
        extends IDimensionClaimsManagerAPI {

    @Nonnull
    @Override
    ResourceLocation getDimension();

    @Override
    int getCount();

    /**
     * Gets a {@link Stream} of all 512x512 regions that contain claims in this dimension.
     *
     * @return a stream of all regions, not null
     */
    @Nonnull
    Stream<IClientRegionClaimsAPI> getRegionStream();

    @Nullable
    @Override
    IClientRegionClaimsAPI getRegion(int x, int z);

}
