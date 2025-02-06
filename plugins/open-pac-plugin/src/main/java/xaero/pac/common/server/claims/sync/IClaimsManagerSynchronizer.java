package xaero.pac.common.server.claims.sync;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.player.config.IPlayerConfig;

public interface IClaimsManagerSynchronizer {

	public void syncClaimLimits(IPlayerConfig config, ServerPlayer player);
	public void syncCurrentSubClaim(IPlayerConfig config, ServerPlayer player);
	public void syncToPlayersClaimOwnerPropertiesUpdate(IServerPlayerClaimInfo<?> playerInfo);
	public void syncToPlayersSubClaimPropertiesUpdate(IPlayerConfig subConfig);
	public void syncToPlayersSubClaimPropertiesRemove(IPlayerConfig subConfig);
	public void syncOnLogin(ServerPlayer player);
	public void onServerTick();
	public void onLazyPacketsDropped(ServerPlayer player);

}
