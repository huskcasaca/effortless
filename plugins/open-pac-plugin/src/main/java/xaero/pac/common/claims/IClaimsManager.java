package xaero.pac.common.claims;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import xaero.pac.common.claims.api.IClaimsManagerAPI;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimInfo;
import xaero.pac.common.claims.tracker.IClaimsManagerTracker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Stream;

public interface IClaimsManager
<
	PCI extends IPlayerClaimInfo<?>,
	WCM extends IDimensionClaimsManager<?>
> extends IClaimsManagerAPI {
	//internal API

	@Nonnull
	@Override
	public PCI getPlayerInfo(@Nonnull UUID playerId);

	@Nullable
	public IPlayerChunkClaim get(@Nonnull ResourceLocation dimension, int x, int z);

	@Nullable
	public IPlayerChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull ChunkPos chunkPos);

	@Nullable
	public IPlayerChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull BlockPos blockPos);

	@Nullable
	public WCM getDimension(@Nonnull ResourceLocation dimension);

	@Nonnull
	public IClaimsManagerTracker getTracker();

	@Nonnull
	public Stream<WCM> getTypedDimensionStream();

	@Nonnull
	public Stream<PCI> getTypedPlayerInfoStream();

}
