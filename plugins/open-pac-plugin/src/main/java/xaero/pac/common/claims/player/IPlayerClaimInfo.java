package xaero.pac.common.claims.player;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerClaimInfoAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

public interface IPlayerClaimInfo<DC extends IPlayerDimensionClaims<?>> extends IPlayerClaimInfoAPI {
	//internal API

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

	@Nonnull
	public Stream<Entry<ResourceLocation, DC>> getTypedStream();

	@Override
	@Nullable
	public DC getDimension(@Nonnull ResourceLocation id);

}
