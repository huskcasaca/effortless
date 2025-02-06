package xaero.pac.common.claims.player;

import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface IPlayerChunkClaim extends IPlayerChunkClaimAPI {
	//internal api

	@Override
	public boolean isForceloadable();

	@Nonnull
	@Override
	public UUID getPlayerId();

}
