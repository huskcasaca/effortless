package xaero.pac.client.claims.player.api;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * API for claim info of a player on the client side
 */
public interface IClientPlayerClaimInfoAPI extends IPlayerClaimInfoAPI {

	@Override
	public int getClaimCount();

	@Override
	public int getForceloadCount();

	@Nonnull
	@Override
	public UUID getPlayerId();

	@Nonnull
	@Override
	public String getPlayerUsername();

	@Override
	public String getClaimsName();

	@Override
	public int getClaimsColor();

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
	public Stream<Entry<ResourceLocation, IPlayerDimensionClaimsAPI>> getStream();

	@Nullable
	public IPlayerDimensionClaimsAPI getDimension(@Nonnull ResourceLocation id);

}
