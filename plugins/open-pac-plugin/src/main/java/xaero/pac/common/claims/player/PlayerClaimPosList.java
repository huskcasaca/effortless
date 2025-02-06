package xaero.pac.common.claims.player;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.ChunkPos;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public final class PlayerClaimPosList implements IPlayerClaimPosList {

	private final PlayerChunkClaim claimState;
	private final LongSet positions;

	private PlayerClaimPosList(PlayerChunkClaim claimState, LongSet positions) {
		super();
		this.claimState = claimState;
		this.positions = positions;
	}

	@Nonnull
	@Override
	public PlayerChunkClaim getClaimState() {
		return claimState;
	}

	@Nonnull
	@Override
	public Stream<ChunkPos> getStream(){
		return positions.longStream().mapToObj(key -> new ChunkPos(PlayerChunkClaim.getXFromLongCoordinates(key), PlayerChunkClaim.getZFromLongCoordinates(key)));
	}

	@Override
	public int getCount() {
		return positions.size();
	}

	public boolean remove(int x, int z) {
		long key = PlayerChunkClaim.getLongCoordinatesFor(x, z);
		return positions.remove(key);
	}

	public void add(int x, int z) {
		long key = PlayerChunkClaim.getLongCoordinatesFor(x, z);
		positions.add(key);
	}

	public static final class Builder {

		private PlayerChunkClaim claim;

		private Builder() {
		}

		private Builder setDefault() {
			return this;
		}

		public Builder setClaim(PlayerChunkClaim claim) {
			this.claim = claim;
			return this;
		}

		public PlayerClaimPosList build() {
			if (claim == null)
				throw new IllegalStateException();
			return new PlayerClaimPosList(claim, new LongOpenHashSet());
		}

		public static Builder begin() {
			return new Builder().setDefault();
		}

	}

}
