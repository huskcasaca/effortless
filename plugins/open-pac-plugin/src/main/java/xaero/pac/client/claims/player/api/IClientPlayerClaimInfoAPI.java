package xaero.pac.client.claims.player.api;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;

/**
 * API for claim info of a player on the client side
 */
public interface IClientPlayerClaimInfoAPI extends IPlayerClaimInfoAPI {

    @Override
    int getClaimCount();

    @Override
    int getForceloadCount();

    @Nonnull
    @Override
    UUID getPlayerId();

    @Nonnull
    @Override
    String getPlayerUsername();

    @Override
    String getClaimsName();

    @Override
    int getClaimsColor();

    @Nullable
    @Override
    String getClaimsName(int subConfigIndex);

    @Nullable
    @Override
    Integer getClaimsColor(int subConfigIndex);

    /**
     * Gets a stream of all dimension claim info entries for the player.
     *
     * @return the stream of all dimension claim info entries, not null
     */
    @Nonnull
    Stream<Entry<ResourceLocation, IPlayerDimensionClaimsAPI>> getStream();

    @Nullable
    IPlayerDimensionClaimsAPI getDimension(@Nonnull ResourceLocation id);

}
