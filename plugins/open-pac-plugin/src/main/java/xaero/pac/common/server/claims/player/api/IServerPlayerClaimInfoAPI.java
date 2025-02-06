package xaero.pac.common.server.claims.player.api;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * API for claim info of a player on the server side
 */
public interface IServerPlayerClaimInfoAPI extends IPlayerClaimInfoAPI {

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

	@Override
	@Nullable
	public IPlayerDimensionClaimsAPI getDimension(@Nonnull ResourceLocation id);

	/**
	 * Gets the currently configured custom name of the player's sub-claim
	 * with a specified string ID.
	 * <p>
	 * Returns null if no such sub-claim exists or the name is inherited from
	 * the main config.
	 *
	 * @param subId  the string ID of the sub-config used by the sub-claim, not null
	 * @return the custom name of the sub-claim
	 */
	@Nullable
	public String getClaimsName(@Nonnull String subId);

	/**
	 * Gets the currently configured color of the player's sub-claim with
	 * a specified string ID.
	 * <p>
	 * Returns null if no such sub-claim exists or the color is inherited from
	 * the main config.
	 *
	 * @param subId  the string ID of the sub-config used by the sub-claim, not null
	 * @return the sub-claim color Integer, null if no such sub-claim exists
	 */
	@Nullable
	public Integer getClaimsColor(@Nonnull String subId);

}
